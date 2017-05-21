package com.parse;

import bolts.Continuation;
import bolts.Task;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class OfflineQueryLogic {
    private final OfflineStore store;

    abstract class ConstraintMatcher<T extends ParseObject> {
        final ParseUser user;

        abstract Task<Boolean> matchesAsync(T t, ParseSQLiteDatabase parseSQLiteDatabase);

        public ConstraintMatcher(ParseUser user) {
            this.user = user;
        }
    }

    private interface Decider {
        boolean decide(Object obj, Object obj2);
    }

    private abstract class SubQueryMatcher<T extends ParseObject> extends ConstraintMatcher<T> {
        private final State<T> subQuery;
        private Task<List<T>> subQueryResults = null;

        protected abstract boolean matches(T t, List<T> list) throws ParseException;

        public SubQueryMatcher(ParseUser user, State<T> subQuery) {
            super(user);
            this.subQuery = subQuery;
        }

        public Task<Boolean> matchesAsync(final T object, ParseSQLiteDatabase db) {
            if (this.subQueryResults == null) {
                this.subQueryResults = OfflineQueryLogic.this.store.findAsync(this.subQuery, this.user, null, db);
            }
            return this.subQueryResults.onSuccess(new Continuation<List<T>, Boolean>() {
                public Boolean then(Task<List<T>> task) throws ParseException {
                    return Boolean.valueOf(SubQueryMatcher.this.matches(object, (List) task.getResult()));
                }
            });
        }
    }

    OfflineQueryLogic(OfflineStore store) {
        this.store = store;
    }

    private static Object getValue(Object container, String key) throws ParseException {
        return getValue(container, key, 0);
    }

    private static Object getValue(Object container, String key, int depth) throws ParseException {
        if (key.contains(".")) {
            String[] parts = key.split("\\.", 2);
            Object value = getValue(container, parts[0], depth + 1);
            if (value == null || value == JSONObject.NULL || (value instanceof Map) || (value instanceof JSONObject)) {
                return getValue(value, parts[1], depth + 1);
            }
            if (depth > 0) {
                Object restFormat = null;
                try {
                    restFormat = PointerEncoder.get().encode(value);
                } catch (Exception e) {
                }
                if (restFormat instanceof JSONObject) {
                    return getValue(restFormat, parts[1], depth + 1);
                }
            }
            throw new ParseException(102, String.format("Key %s is invalid.", new Object[]{key}));
        } else if (container instanceof ParseObject) {
            ParseObject object = (ParseObject) container;
            if (object.isDataAvailable()) {
                int i = -1;
                switch (key.hashCode()) {
                    case -1949194674:
                        if (key.equals("updatedAt")) {
                            i = 3;
                            break;
                        }
                        break;
                    case -1836974455:
                        if (key.equals("_created_at")) {
                            i = 2;
                            break;
                        }
                        break;
                    case 90495162:
                        if (key.equals("objectId")) {
                            i = 0;
                            break;
                        }
                        break;
                    case 598371643:
                        if (key.equals("createdAt")) {
                            i = 1;
                            break;
                        }
                        break;
                    case 792848342:
                        if (key.equals("_updated_at")) {
                            i = 4;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                        return object.getObjectId();
                    case 1:
                    case 2:
                        return object.getCreatedAt();
                    case 3:
                    case 4:
                        return object.getUpdatedAt();
                    default:
                        return object.get(key);
                }
            }
            throw new ParseException(ParseException.INVALID_NESTED_KEY, String.format("Bad key: %s", new Object[]{key}));
        } else if (container instanceof JSONObject) {
            return ((JSONObject) container).opt(key);
        } else {
            if (container instanceof Map) {
                return ((Map) container).get(key);
            }
            if (container == JSONObject.NULL || container == null) {
                return null;
            }
            throw new ParseException(ParseException.INVALID_NESTED_KEY, String.format("Bad key: %s", new Object[]{key}));
        }
    }

    private static int compareTo(Object lhs, Object rhs) {
        boolean lhsIsNullOrUndefined;
        boolean rhsIsNullOrUndefined;
        if (lhs == JSONObject.NULL || lhs == null) {
            lhsIsNullOrUndefined = true;
        } else {
            lhsIsNullOrUndefined = false;
        }
        if (rhs == JSONObject.NULL || rhs == null) {
            rhsIsNullOrUndefined = true;
        } else {
            rhsIsNullOrUndefined = false;
        }
        if (lhsIsNullOrUndefined || rhsIsNullOrUndefined) {
            if (lhsIsNullOrUndefined) {
                return !rhsIsNullOrUndefined ? -1 : 0;
            } else {
                return 1;
            }
        } else if ((lhs instanceof Date) && (rhs instanceof Date)) {
            return ((Date) lhs).compareTo((Date) rhs);
        } else {
            if ((lhs instanceof String) && (rhs instanceof String)) {
                return ((String) lhs).compareTo((String) rhs);
            }
            if ((lhs instanceof Number) && (rhs instanceof Number)) {
                return Numbers.compare((Number) lhs, (Number) rhs);
            }
            throw new IllegalArgumentException(String.format("Cannot compare %s against %s", new Object[]{lhs, rhs}));
        }
    }

    private static boolean compareList(Object constraint, List<?> values, Decider decider) {
        for (Object value : values) {
            if (decider.decide(constraint, value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean compareArray(Object constraint, JSONArray values, Decider decider) {
        int i = 0;
        while (i < values.length()) {
            try {
                if (decider.decide(constraint, values.get(i))) {
                    return true;
                }
                i++;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private static boolean compare(Object constraint, Object value, Decider decider) {
        if (value instanceof List) {
            return compareList(constraint, (List) value, decider);
        }
        if (value instanceof JSONArray) {
            return compareArray(constraint, (JSONArray) value, decider);
        }
        return decider.decide(constraint, value);
    }

    private static boolean matchesEqualConstraint(Object constraint, Object value) {
        if (constraint == null || value == null) {
            if (constraint == value) {
                return true;
            }
            return false;
        } else if ((constraint instanceof Number) && (value instanceof Number)) {
            if (compareTo(constraint, value) != 0) {
                return false;
            }
            return true;
        } else if (!(constraint instanceof ParseGeoPoint) || !(value instanceof ParseGeoPoint)) {
            return compare(constraint, value, new Decider() {
                public boolean decide(Object constraint, Object value) {
                    return constraint.equals(value);
                }
            });
        } else {
            ParseGeoPoint lhs = (ParseGeoPoint) constraint;
            ParseGeoPoint rhs = (ParseGeoPoint) value;
            if (lhs.getLatitude() == rhs.getLatitude() && lhs.getLongitude() == rhs.getLongitude()) {
                return true;
            }
            return false;
        }
    }

    private static boolean matchesNotEqualConstraint(Object constraint, Object value) {
        return !matchesEqualConstraint(constraint, value);
    }

    private static boolean matchesLessThanConstraint(Object constraint, Object value) {
        return compare(constraint, value, new Decider() {
            public boolean decide(Object constraint, Object value) {
                if (value == null || value == JSONObject.NULL || OfflineQueryLogic.compareTo(constraint, value) <= 0) {
                    return false;
                }
                return true;
            }
        });
    }

    private static boolean matchesLessThanOrEqualToConstraint(Object constraint, Object value) {
        return compare(constraint, value, new Decider() {
            public boolean decide(Object constraint, Object value) {
                if (value == null || value == JSONObject.NULL || OfflineQueryLogic.compareTo(constraint, value) < 0) {
                    return false;
                }
                return true;
            }
        });
    }

    private static boolean matchesGreaterThanConstraint(Object constraint, Object value) {
        return compare(constraint, value, new Decider() {
            public boolean decide(Object constraint, Object value) {
                if (value == null || value == JSONObject.NULL || OfflineQueryLogic.compareTo(constraint, value) >= 0) {
                    return false;
                }
                return true;
            }
        });
    }

    private static boolean matchesGreaterThanOrEqualToConstraint(Object constraint, Object value) {
        return compare(constraint, value, new Decider() {
            public boolean decide(Object constraint, Object value) {
                if (value == null || value == JSONObject.NULL || OfflineQueryLogic.compareTo(constraint, value) > 0) {
                    return false;
                }
                return true;
            }
        });
    }

    private static boolean matchesInConstraint(Object constraint, Object value) {
        if (constraint instanceof Collection) {
            for (Object requiredItem : (Collection) constraint) {
                if (matchesEqualConstraint(requiredItem, value)) {
                    return true;
                }
            }
            return false;
        }
        throw new IllegalArgumentException("Constraint type not supported for $in queries.");
    }

    private static boolean matchesNotInConstraint(Object constraint, Object value) {
        return !matchesInConstraint(constraint, value);
    }

    private static boolean matchesAllConstraint(Object constraint, Object value) {
        if (value == null || value == JSONObject.NULL) {
            return false;
        }
        if (!(value instanceof Collection)) {
            throw new IllegalArgumentException("Value type not supported for $all queries.");
        } else if (constraint instanceof Collection) {
            for (Object requiredItem : (Collection) constraint) {
                if (!matchesEqualConstraint(requiredItem, value)) {
                    return false;
                }
            }
            return true;
        } else {
            throw new IllegalArgumentException("Constraint type not supported for $all queries.");
        }
    }

    private static boolean matchesRegexConstraint(Object constraint, Object value, String options) throws ParseException {
        if (value == null || value == JSONObject.NULL) {
            return false;
        }
        if (options == null) {
            options = "";
        }
        if (options.matches("^[imxs]*$")) {
            int flags = 0;
            if (options.contains("i")) {
                flags = 0 | 2;
            }
            if (options.contains("m")) {
                flags |= 8;
            }
            if (options.contains("x")) {
                flags |= 4;
            }
            if (options.contains("s")) {
                flags |= 32;
            }
            return Pattern.compile((String) constraint, flags).matcher((String) value).find();
        }
        throw new ParseException(102, String.format("Invalid regex options: %s", new Object[]{options}));
    }

    private static boolean matchesExistsConstraint(Object constraint, Object value) {
        boolean z = false;
        if (constraint == null || !((Boolean) constraint).booleanValue()) {
            if (value == null || value == JSONObject.NULL) {
                z = true;
            }
            return z;
        } else if (value == null || value == JSONObject.NULL) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean matchesNearSphereConstraint(Object constraint, Object value, Double maxDistance) {
        if (value == null || value == JSONObject.NULL) {
            return false;
        }
        if (maxDistance == null || ((ParseGeoPoint) constraint).distanceInRadiansTo((ParseGeoPoint) value) <= maxDistance.doubleValue()) {
            return true;
        }
        return false;
    }

    private static boolean matchesWithinConstraint(Object constraint, Object value) throws ParseException {
        if (value == null || value == JSONObject.NULL) {
            return false;
        }
        ArrayList<ParseGeoPoint> box = (ArrayList) ((HashMap) constraint).get("$box");
        ParseGeoPoint southwest = (ParseGeoPoint) box.get(0);
        ParseGeoPoint northeast = (ParseGeoPoint) box.get(1);
        ParseGeoPoint target = (ParseGeoPoint) value;
        if (northeast.getLongitude() < southwest.getLongitude()) {
            throw new ParseException(102, "whereWithinGeoBox queries cannot cross the International Date Line.");
        } else if (northeast.getLatitude() < southwest.getLatitude()) {
            throw new ParseException(102, "The southwest corner of a geo box must be south of the northeast corner.");
        } else if (northeast.getLongitude() - southwest.getLongitude() > 180.0d) {
            throw new ParseException(102, "Geo box queries larger than 180 degrees in longitude are not supported. Please check point order.");
        } else if (target.getLatitude() < southwest.getLatitude() || target.getLatitude() > northeast.getLatitude() || target.getLongitude() < southwest.getLongitude() || target.getLongitude() > northeast.getLongitude()) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean matchesStatelessConstraint(String operator, Object constraint, Object value, KeyConstraints allKeyConstraints) throws ParseException {
        int i = -1;
        switch (operator.hashCode()) {
            case -1622199595:
                if (operator.equals("$maxDistance")) {
                    i = 12;
                    break;
                }
                break;
            case -443727559:
                if (operator.equals("$nearSphere")) {
                    i = 11;
                    break;
                }
                break;
            case 37905:
                if (operator.equals("$gt")) {
                    i = 3;
                    break;
                }
                break;
            case 37961:
                if (operator.equals("$in")) {
                    i = 5;
                    break;
                }
                break;
            case 38060:
                if (operator.equals("$lt")) {
                    boolean z = true;
                    break;
                }
                break;
            case 38107:
                if (operator.equals("$ne")) {
                    i = 0;
                    break;
                }
                break;
            case 1169149:
                if (operator.equals("$all")) {
                    i = 7;
                    break;
                }
                break;
            case 1175156:
                if (operator.equals("$gte")) {
                    i = 4;
                    break;
                }
                break;
            case 1179961:
                if (operator.equals("$lte")) {
                    i = 2;
                    break;
                }
                break;
            case 1181551:
                if (operator.equals("$nin")) {
                    i = 6;
                    break;
                }
                break;
            case 596003200:
                if (operator.equals("$exists")) {
                    i = 10;
                    break;
                }
                break;
            case 1097791887:
                if (operator.equals("$within")) {
                    i = 13;
                    break;
                }
                break;
            case 1139041955:
                if (operator.equals("$regex")) {
                    i = 8;
                    break;
                }
                break;
            case 1362155002:
                if (operator.equals("$options")) {
                    i = 9;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                return matchesNotEqualConstraint(constraint, value);
            case 1:
                return matchesLessThanConstraint(constraint, value);
            case 2:
                return matchesLessThanOrEqualToConstraint(constraint, value);
            case 3:
                return matchesGreaterThanConstraint(constraint, value);
            case 4:
                return matchesGreaterThanOrEqualToConstraint(constraint, value);
            case 5:
                return matchesInConstraint(constraint, value);
            case 6:
                return matchesNotInConstraint(constraint, value);
            case 7:
                return matchesAllConstraint(constraint, value);
            case 8:
                return matchesRegexConstraint(constraint, value, (String) allKeyConstraints.get("$options"));
            case 9:
            case 12:
                return true;
            case 10:
                return matchesExistsConstraint(constraint, value);
            case 11:
                return matchesNearSphereConstraint(constraint, value, (Double) allKeyConstraints.get("$maxDistance"));
            case 13:
                return matchesWithinConstraint(constraint, value);
            default:
                throw new UnsupportedOperationException(String.format("The offline store does not yet support the %s operator.", new Object[]{operator}));
        }
    }

    private <T extends ParseObject> ConstraintMatcher<T> createInQueryMatcher(ParseUser user, Object constraint, final String key) {
        return new SubQueryMatcher<T>(user, ((Builder) constraint).build()) {
            protected boolean matches(T object, List<T> results) throws ParseException {
                return OfflineQueryLogic.matchesInConstraint(results, OfflineQueryLogic.getValue(object, key));
            }
        };
    }

    private <T extends ParseObject> ConstraintMatcher<T> createNotInQueryMatcher(ParseUser user, Object constraint, String key) {
        final ConstraintMatcher<T> inQueryMatcher = createInQueryMatcher(user, constraint, key);
        return new ConstraintMatcher<T>(user) {
            public Task<Boolean> matchesAsync(T object, ParseSQLiteDatabase db) {
                return inQueryMatcher.matchesAsync(object, db).onSuccess(new Continuation<Boolean, Boolean>() {
                    public Boolean then(Task<Boolean> task) throws Exception {
                        return Boolean.valueOf(!((Boolean) task.getResult()).booleanValue());
                    }
                });
            }
        };
    }

    private <T extends ParseObject> ConstraintMatcher<T> createSelectMatcher(ParseUser user, Object constraint, String key) {
        Map<?, ?> constraintMap = (Map) constraint;
        final String resultKey = (String) constraintMap.get(ParametersKeys.KEY);
        final String str = key;
        return new SubQueryMatcher<T>(user, ((Builder) constraintMap.get("query")).build()) {
            protected boolean matches(T object, List<T> results) throws ParseException {
                Object value = OfflineQueryLogic.getValue(object, str);
                for (T result : results) {
                    if (OfflineQueryLogic.matchesEqualConstraint(value, OfflineQueryLogic.getValue(result, resultKey))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private <T extends ParseObject> ConstraintMatcher<T> createDontSelectMatcher(ParseUser user, Object constraint, String key) {
        final ConstraintMatcher<T> selectMatcher = createSelectMatcher(user, constraint, key);
        return new ConstraintMatcher<T>(user) {
            public Task<Boolean> matchesAsync(T object, ParseSQLiteDatabase db) {
                return selectMatcher.matchesAsync(object, db).onSuccess(new Continuation<Boolean, Boolean>() {
                    public Boolean then(Task<Boolean> task) throws Exception {
                        return Boolean.valueOf(!((Boolean) task.getResult()).booleanValue());
                    }
                });
            }
        };
    }

    private <T extends ParseObject> ConstraintMatcher<T> createMatcher(ParseUser user, String operator, Object constraint, String key, KeyConstraints allKeyConstraints) {
        Object obj = -1;
        switch (operator.hashCode()) {
            case -721570031:
                if (operator.equals("$dontSelect")) {
                    obj = 3;
                    break;
                }
                break;
            case 242866687:
                if (operator.equals("$inQuery")) {
                    obj = null;
                    break;
                }
                break;
            case 427054964:
                if (operator.equals("$notInQuery")) {
                    obj = 1;
                    break;
                }
                break;
            case 979339808:
                if (operator.equals("$select")) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return createInQueryMatcher(user, constraint, key);
            case 1:
                return createNotInQueryMatcher(user, constraint, key);
            case 2:
                return createSelectMatcher(user, constraint, key);
            case 3:
                return createDontSelectMatcher(user, constraint, key);
            default:
                final String str = key;
                final String str2 = operator;
                final Object obj2 = constraint;
                final KeyConstraints keyConstraints = allKeyConstraints;
                return new ConstraintMatcher<T>(user) {
                    public Task<Boolean> matchesAsync(T object, ParseSQLiteDatabase db) {
                        try {
                            return Task.forResult(Boolean.valueOf(OfflineQueryLogic.matchesStatelessConstraint(str2, obj2, OfflineQueryLogic.getValue(object, str), keyConstraints)));
                        } catch (ParseException e) {
                            return Task.forError(e);
                        }
                    }
                };
        }
    }

    private <T extends ParseObject> ConstraintMatcher<T> createOrMatcher(ParseUser user, ArrayList<QueryConstraints> queries) {
        final ArrayList<ConstraintMatcher<T>> matchers = new ArrayList();
        Iterator i$ = queries.iterator();
        while (i$.hasNext()) {
            matchers.add(createMatcher(user, (QueryConstraints) i$.next()));
        }
        return new ConstraintMatcher<T>(user) {
            public Task<Boolean> matchesAsync(final T object, final ParseSQLiteDatabase db) {
                Task<Boolean> task = Task.forResult(Boolean.valueOf(false));
                Iterator i$ = matchers.iterator();
                while (i$.hasNext()) {
                    final ConstraintMatcher<T> matcher = (ConstraintMatcher) i$.next();
                    task = task.onSuccessTask(new Continuation<Boolean, Task<Boolean>>() {
                        public Task<Boolean> then(Task<Boolean> task) throws Exception {
                            return ((Boolean) task.getResult()).booleanValue() ? task : matcher.matchesAsync(object, db);
                        }
                    });
                }
                return task;
            }
        };
    }

    private <T extends ParseObject> ConstraintMatcher<T> createMatcher(ParseUser user, QueryConstraints queryConstraints) {
        final ArrayList<ConstraintMatcher<T>> matchers = new ArrayList();
        for (final String key : queryConstraints.keySet()) {
            final KeyConstraints queryConstraintValue = queryConstraints.get(key);
            if (key.equals("$or")) {
                matchers.add(createOrMatcher(user, (ArrayList) queryConstraintValue));
            } else if (queryConstraintValue instanceof KeyConstraints) {
                KeyConstraints keyConstraints = queryConstraintValue;
                for (String operator : keyConstraints.keySet()) {
                    matchers.add(createMatcher(user, operator, keyConstraints.get(operator), key, keyConstraints));
                }
            } else if (queryConstraintValue instanceof RelationConstraint) {
                final RelationConstraint relation = (RelationConstraint) queryConstraintValue;
                matchers.add(new ConstraintMatcher<T>(user) {
                    public Task<Boolean> matchesAsync(T object, ParseSQLiteDatabase db) {
                        return Task.forResult(Boolean.valueOf(relation.getRelation().hasKnownObject(object)));
                    }
                });
            } else {
                matchers.add(new ConstraintMatcher<T>(user) {
                    public Task<Boolean> matchesAsync(T object, ParseSQLiteDatabase db) {
                        try {
                            return Task.forResult(Boolean.valueOf(OfflineQueryLogic.matchesEqualConstraint(queryConstraintValue, OfflineQueryLogic.getValue(object, key))));
                        } catch (ParseException e) {
                            return Task.forError(e);
                        }
                    }
                });
            }
        }
        return new ConstraintMatcher<T>(user) {
            public Task<Boolean> matchesAsync(final T object, final ParseSQLiteDatabase db) {
                Task<Boolean> task = Task.forResult(Boolean.valueOf(true));
                Iterator i$ = matchers.iterator();
                while (i$.hasNext()) {
                    final ConstraintMatcher<T> matcher = (ConstraintMatcher) i$.next();
                    task = task.onSuccessTask(new Continuation<Boolean, Task<Boolean>>() {
                        public Task<Boolean> then(Task<Boolean> task) throws Exception {
                            return !((Boolean) task.getResult()).booleanValue() ? task : matcher.matchesAsync(object, db);
                        }
                    });
                }
                return task;
            }
        };
    }

    static <T extends ParseObject> boolean hasReadAccess(ParseUser user, T object) {
        if (user == object) {
            return true;
        }
        ParseACL acl = object.getACL();
        if (acl == null || acl.getPublicReadAccess()) {
            return true;
        }
        if (user == null || !acl.getReadAccess(user)) {
            return false;
        }
        return true;
    }

    static <T extends ParseObject> boolean hasWriteAccess(ParseUser user, T object) {
        if (user == object) {
            return true;
        }
        ParseACL acl = object.getACL();
        if (acl == null || acl.getPublicWriteAccess()) {
            return true;
        }
        if (user == null || !acl.getWriteAccess(user)) {
            return false;
        }
        return true;
    }

    <T extends ParseObject> ConstraintMatcher<T> createMatcher(State<T> state, ParseUser user) {
        final boolean ignoreACLs = state.ignoreACLs();
        final ConstraintMatcher<T> constraintMatcher = createMatcher(user, state.constraints());
        return new ConstraintMatcher<T>(user) {
            public Task<Boolean> matchesAsync(T object, ParseSQLiteDatabase db) {
                if (ignoreACLs || OfflineQueryLogic.hasReadAccess(this.user, object)) {
                    return constraintMatcher.matchesAsync(object, db);
                }
                return Task.forResult(Boolean.valueOf(false));
            }
        };
    }

    static <T extends ParseObject> void sort(List<T> results, State<T> state) throws ParseException {
        final List<String> keys = state.order();
        for (String key : state.order()) {
            if (!key.matches("^-?[A-Za-z][A-Za-z0-9_]*$") && !"_created_at".equals(key) && !"_updated_at".equals(key)) {
                throw new ParseException(105, String.format("Invalid key name: \"%s\".", new Object[]{key}));
            }
        }
        String mutableNearSphereKey = null;
        ParseGeoPoint mutableNearSphereValue = null;
        for (String queryKey : state.constraints().keySet()) {
            KeyConstraints queryKeyConstraints = state.constraints().get(queryKey);
            if (queryKeyConstraints instanceof KeyConstraints) {
                KeyConstraints keyConstraints = queryKeyConstraints;
                if (keyConstraints.containsKey("$nearSphere")) {
                    mutableNearSphereKey = queryKey;
                    mutableNearSphereValue = (ParseGeoPoint) keyConstraints.get("$nearSphere");
                }
            }
        }
        final String nearSphereKey = mutableNearSphereKey;
        final ParseGeoPoint nearSphereValue = mutableNearSphereValue;
        if (keys.size() != 0 || mutableNearSphereKey != null) {
            Collections.sort(results, new Comparator<T>() {
                public int compare(T lhs, T rhs) {
                    String key;
                    if (nearSphereKey != null) {
                        try {
                            ParseGeoPoint rhsPoint = (ParseGeoPoint) OfflineQueryLogic.getValue(rhs, nearSphereKey);
                            double lhsDistance = ((ParseGeoPoint) OfflineQueryLogic.getValue(lhs, nearSphereKey)).distanceInRadiansTo(nearSphereValue);
                            double rhsDistance = rhsPoint.distanceInRadiansTo(nearSphereValue);
                            if (lhsDistance != rhsDistance) {
                                if (lhsDistance - rhsDistance > 0.0d) {
                                    return 1;
                                }
                                return -1;
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    for (String key2 : keys) {
                        boolean descending = false;
                        if (key2.startsWith("-")) {
                            descending = true;
                            key2 = key2.substring(1);
                        }
                        try {
                            try {
                                int result = OfflineQueryLogic.compareTo(OfflineQueryLogic.getValue(lhs, key2), OfflineQueryLogic.getValue(rhs, key2));
                                if (result != 0) {
                                    if (descending) {
                                        result = -result;
                                    }
                                    return result;
                                }
                            } catch (IllegalArgumentException e2) {
                                throw new IllegalArgumentException(String.format("Unable to sort by key %s.", new Object[]{key2}), e2);
                            }
                        } catch (ParseException e3) {
                            throw new RuntimeException(e3);
                        }
                    }
                    return 0;
                }
            });
        }
    }

    private Task<Void> fetchIncludeAsync(Object container, String path, ParseSQLiteDatabase db) throws ParseException {
        if (container == null) {
            return Task.forResult(null);
        }
        Task<Void> task;
        final String str;
        final ParseSQLiteDatabase parseSQLiteDatabase;
        if (container instanceof Collection) {
            Collection<?> collection = (Collection) container;
            task = Task.forResult(null);
            for (final Object item : collection) {
                str = path;
                parseSQLiteDatabase = db;
                task = task.onSuccessTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        return OfflineQueryLogic.this.fetchIncludeAsync(item, str, parseSQLiteDatabase);
                    }
                });
            }
            return task;
        } else if (container instanceof JSONArray) {
            final JSONArray array = (JSONArray) container;
            task = Task.forResult(null);
            for (int i = 0; i < array.length(); i++) {
                final int index = i;
                final String str2 = path;
                final ParseSQLiteDatabase parseSQLiteDatabase2 = db;
                task = task.onSuccessTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        return OfflineQueryLogic.this.fetchIncludeAsync(array.get(index), str2, parseSQLiteDatabase2);
                    }
                });
            }
            return task;
        } else if (path != null) {
            String[] parts = path.split("\\.", 2);
            final String key = parts[0];
            final Object obj = container;
            parseSQLiteDatabase = db;
            str = parts.length > 1 ? parts[1] : null;
            parseSQLiteDatabase = db;
            return Task.forResult(null).continueWithTask(new Continuation<Void, Task<Object>>() {
                public Task<Object> then(Task<Void> task) throws Exception {
                    if (obj instanceof ParseObject) {
                        return OfflineQueryLogic.this.fetchIncludeAsync(obj, null, parseSQLiteDatabase).onSuccess(new Continuation<Void, Object>() {
                            public Object then(Task<Void> task) throws Exception {
                                return ((ParseObject) obj).get(key);
                            }
                        });
                    }
                    if (obj instanceof Map) {
                        return Task.forResult(((Map) obj).get(key));
                    }
                    if (obj instanceof JSONObject) {
                        return Task.forResult(((JSONObject) obj).opt(key));
                    }
                    if (JSONObject.NULL.equals(obj)) {
                        return null;
                    }
                    return Task.forError(new IllegalStateException("include is invalid"));
                }
            }).onSuccessTask(new Continuation<Object, Task<Void>>() {
                public Task<Void> then(Task<Object> task) throws Exception {
                    return OfflineQueryLogic.this.fetchIncludeAsync(task.getResult(), str, parseSQLiteDatabase);
                }
            });
        } else if (JSONObject.NULL.equals(container)) {
            return Task.forResult(null);
        } else {
            if (!(container instanceof ParseObject)) {
                return Task.forError(new ParseException(ParseException.INVALID_NESTED_KEY, "include is invalid for non-ParseObjects"));
            }
            return this.store.fetchLocallyAsync((ParseObject) container, db).makeVoid();
        }
    }

    <T extends ParseObject> Task<Void> fetchIncludesAsync(final T object, State<T> state, final ParseSQLiteDatabase db) {
        Set<String> includes = state.includes();
        Task<Void> task = Task.forResult(null);
        for (final String include : includes) {
            task = task.onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return OfflineQueryLogic.this.fetchIncludeAsync(object, include, db);
                }
            });
        }
        return task;
    }
}
