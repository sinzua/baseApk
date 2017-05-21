package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.map.annotate.JacksonStdImpl;

@Deprecated
@JacksonStdImpl
public final class ToStringSerializer extends org.codehaus.jackson.map.ser.std.ToStringSerializer {
    public static final ToStringSerializer instance = new ToStringSerializer();
}
