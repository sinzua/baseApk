package org.codehaus.jackson.map.ext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.map.deser.std.StdScalarDeserializer;
import org.codehaus.jackson.map.util.Provider;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class JodaDeserializers implements Provider<StdDeserializer<?>> {

    static abstract class JodaDeserializer<T> extends StdScalarDeserializer<T> {
        static final DateTimeFormatter _localDateTimeFormat = ISODateTimeFormat.localDateOptionalTimeParser();

        protected JodaDeserializer(Class<T> cls) {
            super((Class) cls);
        }

        protected DateTime parseLocal(JsonParser jp) throws IOException, JsonProcessingException {
            String str = jp.getText().trim();
            if (str.length() == 0) {
                return null;
            }
            return _localDateTimeFormat.parseDateTime(str);
        }
    }

    public static class DateMidnightDeserializer extends JodaDeserializer<DateMidnight> {
        public DateMidnightDeserializer() {
            super(DateMidnight.class);
        }

        public DateMidnight deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                jp.nextToken();
                int year = jp.getIntValue();
                jp.nextToken();
                int month = jp.getIntValue();
                jp.nextToken();
                int day = jp.getIntValue();
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return new DateMidnight(year, month, day);
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after DateMidnight ints");
            }
            switch (jp.getCurrentToken()) {
                case VALUE_NUMBER_INT:
                    return new DateMidnight(jp.getLongValue());
                case VALUE_STRING:
                    DateTime local = parseLocal(jp);
                    if (local == null) {
                        return null;
                    }
                    return local.toDateMidnight();
                default:
                    throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, Number or String");
            }
        }
    }

    public static class DateTimeDeserializer<T extends ReadableInstant> extends JodaDeserializer<T> {
        public DateTimeDeserializer(Class<T> cls) {
            super(cls);
        }

        public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT) {
                return new DateTime(jp.getLongValue(), DateTimeZone.UTC);
            }
            if (t == JsonToken.VALUE_STRING) {
                String str = jp.getText().trim();
                if (str.length() == 0) {
                    return null;
                }
                return new DateTime(str, DateTimeZone.UTC);
            }
            throw ctxt.mappingException(getValueClass());
        }
    }

    public static class LocalDateDeserializer extends JodaDeserializer<LocalDate> {
        public LocalDateDeserializer() {
            super(LocalDate.class);
        }

        public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                jp.nextToken();
                int year = jp.getIntValue();
                jp.nextToken();
                int month = jp.getIntValue();
                jp.nextToken();
                int day = jp.getIntValue();
                if (jp.nextToken() == JsonToken.END_ARRAY) {
                    return new LocalDate(year, month, day);
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after LocalDate ints");
            }
            switch (jp.getCurrentToken()) {
                case VALUE_NUMBER_INT:
                    return new LocalDate(jp.getLongValue());
                case VALUE_STRING:
                    DateTime local = parseLocal(jp);
                    if (local == null) {
                        return null;
                    }
                    return local.toLocalDate();
                default:
                    throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, String or Number");
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JodaDeserializer<LocalDateTime> {
        public LocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                jp.nextToken();
                int year = jp.getIntValue();
                jp.nextToken();
                int month = jp.getIntValue();
                jp.nextToken();
                int day = jp.getIntValue();
                jp.nextToken();
                int hour = jp.getIntValue();
                jp.nextToken();
                int minute = jp.getIntValue();
                jp.nextToken();
                int second = jp.getIntValue();
                int millisecond = 0;
                if (jp.nextToken() != JsonToken.END_ARRAY) {
                    millisecond = jp.getIntValue();
                    jp.nextToken();
                }
                if (jp.getCurrentToken() == JsonToken.END_ARRAY) {
                    return new LocalDateTime(year, month, day, hour, minute, second, millisecond);
                }
                throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after LocalDateTime ints");
            }
            switch (jp.getCurrentToken()) {
                case VALUE_NUMBER_INT:
                    return new LocalDateTime(jp.getLongValue());
                case VALUE_STRING:
                    DateTime local = parseLocal(jp);
                    if (local == null) {
                        return null;
                    }
                    return local.toLocalDateTime();
                default:
                    throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array or Number");
            }
        }
    }

    public static class PeriodDeserializer extends JodaDeserializer<ReadablePeriod> {
        public PeriodDeserializer() {
            super(ReadablePeriod.class);
        }

        public ReadablePeriod deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            switch (jp.getCurrentToken()) {
                case VALUE_NUMBER_INT:
                    return new Period(jp.getLongValue());
                case VALUE_STRING:
                    return new Period(jp.getText());
                default:
                    throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Number or String");
            }
        }
    }

    public Collection<StdDeserializer<?>> provide() {
        return Arrays.asList(new StdDeserializer[]{new DateTimeDeserializer(DateTime.class), new DateTimeDeserializer(ReadableDateTime.class), new DateTimeDeserializer(ReadableInstant.class), new LocalDateDeserializer(), new LocalDateTimeDeserializer(), new DateMidnightDeserializer(), new PeriodDeserializer()});
    }
}
