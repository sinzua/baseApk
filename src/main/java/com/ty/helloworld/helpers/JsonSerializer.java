package com.ty.helloworld.helpers;

import android.util.Log;
import java.util.Collection;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonSerializer {
    private static final String TAG = JsonSerializer.class.getName();
    private static JsonSerializer instance = new JsonSerializer();
    private ObjectMapper impl = new ObjectMapper();

    public static JsonSerializer getInstance() {
        return instance;
    }

    private JsonSerializer() {
        this.impl.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.impl.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String serialize(Object object) {
        try {
            return this.impl.writeValueAsString(object);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        }
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return this.impl.readValue(json, (Class) clazz);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        }
    }

    public <T extends Collection<?>, V> Object deserialize(String json, Class<T> collection, Class<V> data) {
        try {
            return this.impl.readValue(json, this.impl.getTypeFactory().constructCollectionType((Class) collection, (Class) data));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        }
    }
}
