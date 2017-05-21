package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Member;
import org.codehaus.jackson.map.util.ClassUtil;

public abstract class AnnotatedMember extends Annotated {
    protected final AnnotationMap _annotations;

    public abstract Class<?> getDeclaringClass();

    public abstract Member getMember();

    public abstract void setValue(Object obj, Object obj2) throws UnsupportedOperationException, IllegalArgumentException;

    protected AnnotatedMember(AnnotationMap annotations) {
        this._annotations = annotations;
    }

    protected AnnotationMap getAllAnnotations() {
        return this._annotations;
    }

    public final void fixAccess() {
        ClassUtil.checkAndFixAccess(getMember());
    }
}
