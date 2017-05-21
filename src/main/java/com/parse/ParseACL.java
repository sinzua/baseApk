package com.parse;

import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseACL {
    private static final String KEY_ROLE_PREFIX = "role:";
    private static final String PUBLIC_KEY = "*";
    private static final String READ_PERMISSION = "read";
    private static final String UNRESOLVED_KEY = "*unresolved";
    private static final String WRITE_PERMISSION = "write";
    private JSONObject permissionsById;
    private boolean shared;
    private ParseUser unresolvedUser;

    private static class UserResolutionListener implements GetCallback<ParseObject> {
        private final WeakReference<ParseACL> parent;

        public UserResolutionListener(ParseACL parent) {
            this.parent = new WeakReference(parent);
        }

        public void done(ParseObject object, ParseException e) {
            try {
                ParseACL parent = (ParseACL) this.parent.get();
                if (parent != null) {
                    parent.resolveUser((ParseUser) object);
                }
                object.unregisterSaveListener(this);
            } catch (Throwable th) {
                object.unregisterSaveListener(this);
            }
        }
    }

    private static ParseDefaultACLController getDefaultACLController() {
        return ParseCorePlugins.getInstance().getDefaultACLController();
    }

    public static void setDefaultACL(ParseACL acl, boolean withAccessForCurrentUser) {
        getDefaultACLController().set(acl, withAccessForCurrentUser);
    }

    static ParseACL getDefaultACL() {
        return getDefaultACLController().get();
    }

    public ParseACL() {
        this.permissionsById = new JSONObject();
    }

    ParseACL copy() {
        ParseACL copy = new ParseACL();
        try {
            copy.permissionsById = new JSONObject(this.permissionsById.toString());
            copy.unresolvedUser = this.unresolvedUser;
            if (this.unresolvedUser != null) {
                this.unresolvedUser.registerSaveListener(new UserResolutionListener(copy));
            }
            return copy;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isShared() {
        return this.shared;
    }

    void setShared(boolean shared) {
        this.shared = shared;
    }

    JSONObject toJSONObject(ParseEncoder objectEncoder) {
        try {
            JSONObject json = new JSONObject(this.permissionsById.toString());
            if (this.unresolvedUser != null) {
                json.put("unresolvedUser", objectEncoder.encode(this.unresolvedUser));
            }
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    static ParseACL createACLFromJSONObject(JSONObject object, ParseDecoder decoder) {
        ParseACL acl = new ParseACL();
        for (String key : ParseJSONUtils.keys(object)) {
            if (key.equals("unresolvedUser")) {
                try {
                    acl.unresolvedUser = (ParseUser) decoder.decode(object.getJSONObject(key));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            String userId = key;
            try {
                for (String accessType : ParseJSONUtils.keys(object.getJSONObject(userId))) {
                    acl.setAccess(accessType, userId, true);
                }
            } catch (JSONException e2) {
                throw new RuntimeException("could not decode ACL: " + e2.getMessage());
            }
        }
        return acl;
    }

    public ParseACL(ParseUser owner) {
        this();
        setReadAccess(owner, true);
        setWriteAccess(owner, true);
    }

    void resolveUser(ParseUser user) {
        if (user == this.unresolvedUser) {
            try {
                if (this.permissionsById.has(UNRESOLVED_KEY)) {
                    this.permissionsById.put(user.getObjectId(), this.permissionsById.get(UNRESOLVED_KEY));
                    this.permissionsById.remove(UNRESOLVED_KEY);
                }
                this.unresolvedUser = null;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    boolean hasUnresolvedUser() {
        return this.unresolvedUser != null;
    }

    ParseUser getUnresolvedUser() {
        return this.unresolvedUser;
    }

    private void setAccess(String accessType, String userId, boolean allowed) {
        try {
            JSONObject permissions = this.permissionsById.optJSONObject(userId);
            if (permissions == null) {
                if (allowed) {
                    permissions = new JSONObject();
                    this.permissionsById.put(userId, permissions);
                } else {
                    return;
                }
            }
            if (allowed) {
                permissions.put(accessType, true);
                return;
            }
            permissions.remove(accessType);
            if (permissions.length() == 0) {
                this.permissionsById.remove(userId);
            }
        } catch (JSONException e) {
            throw new RuntimeException("JSON failure with ACL: " + e.getMessage());
        }
    }

    private boolean getAccess(String accessType, String userId) {
        boolean z = false;
        try {
            JSONObject permissions = this.permissionsById.optJSONObject(userId);
            if (permissions != null && permissions.has(accessType)) {
                z = permissions.getBoolean(accessType);
            }
            return z;
        } catch (JSONException e) {
            throw new RuntimeException("JSON failure with ACL: " + e.getMessage());
        }
    }

    public void setPublicReadAccess(boolean allowed) {
        setReadAccess(PUBLIC_KEY, allowed);
    }

    public boolean getPublicReadAccess() {
        return getReadAccess(PUBLIC_KEY);
    }

    public void setPublicWriteAccess(boolean allowed) {
        setWriteAccess(PUBLIC_KEY, allowed);
    }

    public boolean getPublicWriteAccess() {
        return getWriteAccess(PUBLIC_KEY);
    }

    public void setReadAccess(String userId, boolean allowed) {
        if (userId == null) {
            throw new IllegalArgumentException("cannot setReadAccess for null userId");
        }
        setAccess(READ_PERMISSION, userId, allowed);
    }

    public boolean getReadAccess(String userId) {
        if (userId != null) {
            return getAccess(READ_PERMISSION, userId);
        }
        throw new IllegalArgumentException("cannot getReadAccess for null userId");
    }

    public void setWriteAccess(String userId, boolean allowed) {
        if (userId == null) {
            throw new IllegalArgumentException("cannot setWriteAccess for null userId");
        }
        setAccess(WRITE_PERMISSION, userId, allowed);
    }

    public boolean getWriteAccess(String userId) {
        if (userId != null) {
            return getAccess(WRITE_PERMISSION, userId);
        }
        throw new IllegalArgumentException("cannot getWriteAccess for null userId");
    }

    public void setReadAccess(ParseUser user, boolean allowed) {
        if (user.getObjectId() != null) {
            setReadAccess(user.getObjectId(), allowed);
        } else if (user.isLazy()) {
            setUnresolvedReadAccess(user, allowed);
        } else {
            throw new IllegalArgumentException("cannot setReadAccess for a user with null id");
        }
    }

    private void setUnresolvedReadAccess(ParseUser user, boolean allowed) {
        prepareUnresolvedUser(user);
        setReadAccess(UNRESOLVED_KEY, allowed);
    }

    private void setUnresolvedWriteAccess(ParseUser user, boolean allowed) {
        prepareUnresolvedUser(user);
        setWriteAccess(UNRESOLVED_KEY, allowed);
    }

    private void prepareUnresolvedUser(ParseUser user) {
        if (this.unresolvedUser != user) {
            this.permissionsById.remove(UNRESOLVED_KEY);
            this.unresolvedUser = user;
            user.registerSaveListener(new UserResolutionListener(this));
        }
    }

    public boolean getReadAccess(ParseUser user) {
        if (user == this.unresolvedUser) {
            return getReadAccess(UNRESOLVED_KEY);
        }
        if (user.isLazy()) {
            return false;
        }
        if (user.getObjectId() != null) {
            return getReadAccess(user.getObjectId());
        }
        throw new IllegalArgumentException("cannot getReadAccess for a user with null id");
    }

    public void setWriteAccess(ParseUser user, boolean allowed) {
        if (user.getObjectId() != null) {
            setWriteAccess(user.getObjectId(), allowed);
        } else if (user.isLazy()) {
            setUnresolvedWriteAccess(user, allowed);
        } else {
            throw new IllegalArgumentException("cannot setWriteAccess for a user with null id");
        }
    }

    public boolean getWriteAccess(ParseUser user) {
        if (user == this.unresolvedUser) {
            return getWriteAccess(UNRESOLVED_KEY);
        }
        if (user.isLazy()) {
            return false;
        }
        if (user.getObjectId() != null) {
            return getWriteAccess(user.getObjectId());
        }
        throw new IllegalArgumentException("cannot getWriteAccess for a user with null id");
    }

    public boolean getRoleReadAccess(String roleName) {
        return getReadAccess(KEY_ROLE_PREFIX + roleName);
    }

    public void setRoleReadAccess(String roleName, boolean allowed) {
        setReadAccess(KEY_ROLE_PREFIX + roleName, allowed);
    }

    public boolean getRoleWriteAccess(String roleName) {
        return getWriteAccess(KEY_ROLE_PREFIX + roleName);
    }

    public void setRoleWriteAccess(String roleName, boolean allowed) {
        setWriteAccess(KEY_ROLE_PREFIX + roleName, allowed);
    }

    private static void validateRoleState(ParseRole role) {
        if (role == null || role.getObjectId() == null) {
            throw new IllegalArgumentException("Roles must be saved to the server before they can be used in an ACL.");
        }
    }

    public boolean getRoleReadAccess(ParseRole role) {
        validateRoleState(role);
        return getRoleReadAccess(role.getName());
    }

    public void setRoleReadAccess(ParseRole role, boolean allowed) {
        validateRoleState(role);
        setRoleReadAccess(role.getName(), allowed);
    }

    public boolean getRoleWriteAccess(ParseRole role) {
        validateRoleState(role);
        return getRoleWriteAccess(role.getName());
    }

    public void setRoleWriteAccess(ParseRole role, boolean allowed) {
        validateRoleState(role);
        setRoleWriteAccess(role.getName(), allowed);
    }

    JSONObject getPermissionsById() {
        return this.permissionsById;
    }
}
