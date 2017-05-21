package org.codehaus.jackson.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonAutoDetect {

    public enum Visibility {
        ANY,
        NON_PRIVATE,
        PROTECTED_AND_PUBLIC,
        PUBLIC_ONLY,
        NONE,
        DEFAULT;

        public boolean isVisible(Member m) {
            switch (this) {
                case ANY:
                    return true;
                case NONE:
                    return false;
                case NON_PRIVATE:
                    if (Modifier.isPrivate(m.getModifiers())) {
                        return false;
                    }
                    return true;
                case PROTECTED_AND_PUBLIC:
                    if (Modifier.isProtected(m.getModifiers())) {
                        return true;
                    }
                    break;
                case PUBLIC_ONLY:
                    break;
                default:
                    return false;
            }
            return Modifier.isPublic(m.getModifiers());
        }
    }

    Visibility creatorVisibility() default Visibility.DEFAULT;

    Visibility fieldVisibility() default Visibility.DEFAULT;

    Visibility getterVisibility() default Visibility.DEFAULT;

    Visibility isGetterVisibility() default Visibility.DEFAULT;

    Visibility setterVisibility() default Visibility.DEFAULT;

    JsonMethod[] value() default {JsonMethod.ALL};
}
