package org.codehaus.jackson.map;

import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;

public abstract class PropertyNamingStrategy {
    public static final PropertyNamingStrategy CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES = new LowerCaseWithUnderscoresStrategy();

    public static abstract class PropertyNamingStrategyBase extends PropertyNamingStrategy {
        public abstract String translate(String str);

        public String nameForField(MapperConfig<?> mapperConfig, AnnotatedField field, String defaultName) {
            return translate(defaultName);
        }

        public String nameForGetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
            return translate(defaultName);
        }

        public String nameForSetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
            return translate(defaultName);
        }

        public String nameForConstructorParameter(MapperConfig<?> mapperConfig, AnnotatedParameter ctorParam, String defaultName) {
            return translate(defaultName);
        }
    }

    public static class LowerCaseWithUnderscoresStrategy extends PropertyNamingStrategyBase {
        public String translate(String input) {
            if (input == null) {
                return input;
            }
            int length = input.length();
            StringBuilder result = new StringBuilder(length * 2);
            int resultLength = 0;
            boolean wasPrevTranslated = false;
            for (int i = 0; i < length; i++) {
                char c = input.charAt(i);
                if (i > 0 || c != '_') {
                    if (Character.isUpperCase(c)) {
                        if (!(wasPrevTranslated || resultLength <= 0 || result.charAt(resultLength - 1) == '_')) {
                            result.append('_');
                            resultLength++;
                        }
                        c = Character.toLowerCase(c);
                        wasPrevTranslated = true;
                    } else {
                        wasPrevTranslated = false;
                    }
                    result.append(c);
                    resultLength++;
                }
            }
            return resultLength > 0 ? result.toString() : input;
        }
    }

    public String nameForField(MapperConfig<?> mapperConfig, AnnotatedField field, String defaultName) {
        return defaultName;
    }

    public String nameForGetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
        return defaultName;
    }

    public String nameForSetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
        return defaultName;
    }

    public String nameForConstructorParameter(MapperConfig<?> mapperConfig, AnnotatedParameter ctorParam, String defaultName) {
        return defaultName;
    }
}
