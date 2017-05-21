package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.type.JavaType;

public class NopAnnotationIntrospector extends AnnotationIntrospector {
    public static final NopAnnotationIntrospector instance = new NopAnnotationIntrospector();

    public boolean isHandled(Annotation ann) {
        return false;
    }

    public String findEnumValue(Enum<?> value) {
        return value.name();
    }

    public String findRootName(AnnotatedClass ac) {
        return null;
    }

    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        return null;
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        return null;
    }

    public boolean hasIgnoreMarker(AnnotatedMember member) {
        return false;
    }

    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return false;
    }

    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return false;
    }

    public boolean isIgnorableField(AnnotatedField f) {
        return false;
    }

    public Object findSerializer(Annotated am) {
        return null;
    }

    public Class<?> findSerializationType(Annotated a) {
        return null;
    }

    public Typing findSerializationTyping(Annotated a) {
        return null;
    }

    public Class<?>[] findSerializationViews(Annotated a) {
        return null;
    }

    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        return null;
    }

    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        return null;
    }

    public String findGettablePropertyName(AnnotatedMethod am) {
        return null;
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        return false;
    }

    public String findDeserializablePropertyName(AnnotatedField af) {
        return null;
    }

    public Class<?> findDeserializationContentType(Annotated am, JavaType t, String propName) {
        return null;
    }

    public Class<?> findDeserializationKeyType(Annotated am, JavaType t, String propName) {
        return null;
    }

    public Class<?> findDeserializationType(Annotated am, JavaType t, String propName) {
        return null;
    }

    public Object findDeserializer(Annotated am) {
        return null;
    }

    public Class<KeyDeserializer> findKeyDeserializer(Annotated am) {
        return null;
    }

    public Class<JsonDeserializer<?>> findContentDeserializer(Annotated am) {
        return null;
    }

    public String findPropertyNameForParam(AnnotatedParameter param) {
        return null;
    }

    public String findSerializablePropertyName(AnnotatedField af) {
        return null;
    }

    public String findSettablePropertyName(AnnotatedMethod am) {
        return null;
    }
}
