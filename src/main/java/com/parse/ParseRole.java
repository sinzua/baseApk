package com.parse;

import java.util.regex.Pattern;

@ParseClassName("_Role")
public class ParseRole extends ParseObject {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[0-9a-zA-Z_\\- ]+$");

    ParseRole() {
    }

    public ParseRole(String name) {
        this();
        setName(name);
    }

    public ParseRole(String name, ParseACL acl) {
        this(name);
        setACL(acl);
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public ParseRelation<ParseUser> getUsers() {
        return getRelation("users");
    }

    public ParseRelation<ParseRole> getRoles() {
        return getRelation("roles");
    }

    void validateSave() {
        synchronized (this.mutex) {
            if (getObjectId() == null && getName() == null) {
                throw new IllegalStateException("New roles must specify a name.");
            }
            super.validateSave();
        }
    }

    public void put(String key, Object value) {
        if ("name".equals(key)) {
            if (getObjectId() != null) {
                throw new IllegalArgumentException("A role's name can only be set before it has been saved.");
            } else if (!(value instanceof String)) {
                throw new IllegalArgumentException("A role's name must be a String.");
            } else if (!NAME_PATTERN.matcher((String) value).matches()) {
                throw new IllegalArgumentException("A role's name can only contain alphanumeric characters, _, -, and spaces.");
            }
        }
        super.put(key, value);
    }

    public static ParseQuery<ParseRole> getQuery() {
        return ParseQuery.getQuery(ParseRole.class);
    }
}
