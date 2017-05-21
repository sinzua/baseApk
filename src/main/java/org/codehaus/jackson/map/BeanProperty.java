package org.codehaus.jackson.map;

import java.lang.annotation.Annotation;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.Named;
import org.codehaus.jackson.type.JavaType;

public interface BeanProperty extends Named {

    public static class Std implements BeanProperty {
        protected final Annotations _contextAnnotations;
        protected final AnnotatedMember _member;
        protected final String _name;
        protected final JavaType _type;

        public Std(String name, JavaType type, Annotations contextAnnotations, AnnotatedMember member) {
            this._name = name;
            this._type = type;
            this._member = member;
            this._contextAnnotations = contextAnnotations;
        }

        public Std withType(JavaType type) {
            return new Std(this._name, type, this._contextAnnotations, this._member);
        }

        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return this._member.getAnnotation(acls);
        }

        public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
            return this._contextAnnotations == null ? null : this._contextAnnotations.get(acls);
        }

        public String getName() {
            return this._name;
        }

        public JavaType getType() {
            return this._type;
        }

        public AnnotatedMember getMember() {
            return this._member;
        }
    }

    <A extends Annotation> A getAnnotation(Class<A> cls);

    <A extends Annotation> A getContextAnnotation(Class<A> cls);

    AnnotatedMember getMember();

    String getName();

    JavaType getType();
}
