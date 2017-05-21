package org.codehaus.jackson.map.deser;

import java.util.GregorianCalendar;
import java.util.HashMap;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.deser.std.AtomicBooleanDeserializer;
import org.codehaus.jackson.map.deser.std.CalendarDeserializer;
import org.codehaus.jackson.map.deser.std.ClassDeserializer;
import org.codehaus.jackson.map.deser.std.DateDeserializer;
import org.codehaus.jackson.map.deser.std.FromStringDeserializer;
import org.codehaus.jackson.map.deser.std.JavaTypeDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigDecimalDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigIntegerDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BooleanDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.ByteDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.CharacterDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.DoubleDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.FloatDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.IntegerDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.LongDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.NumberDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.ShortDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.SqlDateDeserializer;
import org.codehaus.jackson.map.deser.std.StdDeserializer.StackTraceElementDeserializer;
import org.codehaus.jackson.map.deser.std.StringDeserializer;
import org.codehaus.jackson.map.deser.std.TimestampDeserializer;
import org.codehaus.jackson.map.deser.std.TokenBufferDeserializer;
import org.codehaus.jackson.map.deser.std.UntypedObjectDeserializer;
import org.codehaus.jackson.map.type.ClassKey;

class StdDeserializers {
    final HashMap<ClassKey, JsonDeserializer<Object>> _deserializers = new HashMap();

    private StdDeserializers() {
        add(new UntypedObjectDeserializer());
        StdDeserializer<?> strDeser = new StringDeserializer();
        add(strDeser, String.class);
        add(strDeser, CharSequence.class);
        add(new ClassDeserializer());
        add(new BooleanDeserializer(Boolean.class, null));
        add(new ByteDeserializer(Byte.class, null));
        add(new ShortDeserializer(Short.class, null));
        add(new CharacterDeserializer(Character.class, null));
        add(new IntegerDeserializer(Integer.class, null));
        add(new LongDeserializer(Long.class, null));
        add(new FloatDeserializer(Float.class, null));
        add(new DoubleDeserializer(Double.class, null));
        add(new BooleanDeserializer(Boolean.TYPE, Boolean.FALSE));
        add(new ByteDeserializer(Byte.TYPE, Byte.valueOf((byte) 0)));
        add(new ShortDeserializer(Short.TYPE, Short.valueOf((short) 0)));
        add(new CharacterDeserializer(Character.TYPE, Character.valueOf('\u0000')));
        add(new IntegerDeserializer(Integer.TYPE, Integer.valueOf(0)));
        add(new LongDeserializer(Long.TYPE, Long.valueOf(0)));
        add(new FloatDeserializer(Float.TYPE, Float.valueOf(0.0f)));
        add(new DoubleDeserializer(Double.TYPE, Double.valueOf(0.0d)));
        add(new NumberDeserializer());
        add(new BigDecimalDeserializer());
        add(new BigIntegerDeserializer());
        add(new CalendarDeserializer());
        add(new DateDeserializer());
        add(new CalendarDeserializer(GregorianCalendar.class), GregorianCalendar.class);
        add(new SqlDateDeserializer());
        add(new TimestampDeserializer());
        for (StdDeserializer<?> deser : FromStringDeserializer.all()) {
            add(deser);
        }
        add(new StackTraceElementDeserializer());
        add(new AtomicBooleanDeserializer());
        add(new TokenBufferDeserializer());
        add(new JavaTypeDeserializer());
    }

    public static HashMap<ClassKey, JsonDeserializer<Object>> constructAll() {
        return new StdDeserializers()._deserializers;
    }

    private void add(StdDeserializer<?> stdDeser) {
        add(stdDeser, stdDeser.getValueClass());
    }

    private void add(StdDeserializer<?> stdDeser, Class<?> valueClass) {
        this._deserializers.put(new ClassKey(valueClass), stdDeser);
    }
}
