package org.codehaus.jackson.map.exc;

import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;

public class UnrecognizedPropertyException extends JsonMappingException {
    private static final long serialVersionUID = 1;
    protected final Class<?> _referringClass;
    protected final String _unrecognizedPropertyName;

    public UnrecognizedPropertyException(String msg, JsonLocation loc, Class<?> referringClass, String propName) {
        super(msg, loc);
        this._referringClass = referringClass;
        this._unrecognizedPropertyName = propName;
    }

    public static UnrecognizedPropertyException from(JsonParser jp, Object fromObjectOrClass, String propertyName) {
        if (fromObjectOrClass == null) {
            throw new IllegalArgumentException();
        }
        Class<?> ref;
        if (fromObjectOrClass instanceof Class) {
            ref = (Class) fromObjectOrClass;
        } else {
            ref = fromObjectOrClass.getClass();
        }
        UnrecognizedPropertyException e = new UnrecognizedPropertyException("Unrecognized field \"" + propertyName + "\" (Class " + ref.getName() + "), not marked as ignorable", jp.getCurrentLocation(), ref, propertyName);
        e.prependPath(fromObjectOrClass, propertyName);
        return e;
    }

    public Class<?> getReferringClass() {
        return this._referringClass;
    }

    public String getUnrecognizedPropertyName() {
        return this._unrecognizedPropertyName;
    }
}
