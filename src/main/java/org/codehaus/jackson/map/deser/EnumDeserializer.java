package org.codehaus.jackson.map.deser;

import org.codehaus.jackson.map.util.EnumResolver;

@Deprecated
public class EnumDeserializer extends org.codehaus.jackson.map.deser.std.EnumDeserializer {
    public EnumDeserializer(EnumResolver<?> res) {
        super(res);
    }
}
