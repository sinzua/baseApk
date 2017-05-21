package org.codehaus.jackson.map.introspect;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.ClassUtil;

public final class AnnotatedClass extends Annotated {
    private static final AnnotationMap[] NO_ANNOTATION_MAPS = new AnnotationMap[0];
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final Class<?> _class;
    protected AnnotationMap _classAnnotations;
    protected List<AnnotatedConstructor> _constructors;
    protected List<AnnotatedMethod> _creatorMethods;
    protected AnnotatedConstructor _defaultConstructor;
    protected List<AnnotatedField> _fields;
    protected AnnotatedMethodMap _memberMethods;
    protected final MixInResolver _mixInResolver;
    protected final Class<?> _primaryMixIn;
    protected final List<Class<?>> _superTypes;

    private AnnotatedClass(Class<?> cls, List<Class<?>> superTypes, AnnotationIntrospector aintr, MixInResolver mir, AnnotationMap classAnnotations) {
        this._class = cls;
        this._superTypes = superTypes;
        this._annotationIntrospector = aintr;
        this._mixInResolver = mir;
        this._primaryMixIn = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(this._class);
        this._classAnnotations = classAnnotations;
    }

    public AnnotatedClass withAnnotations(AnnotationMap ann) {
        return new AnnotatedClass(this._class, this._superTypes, this._annotationIntrospector, this._mixInResolver, ann);
    }

    public static AnnotatedClass construct(Class<?> cls, AnnotationIntrospector aintr, MixInResolver mir) {
        AnnotatedClass ac = new AnnotatedClass(cls, ClassUtil.findSuperTypes(cls, null), aintr, mir, null);
        ac.resolveClassAnnotations();
        return ac;
    }

    public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, AnnotationIntrospector aintr, MixInResolver mir) {
        AnnotatedClass ac = new AnnotatedClass(cls, Collections.emptyList(), aintr, mir, null);
        ac.resolveClassAnnotations();
        return ac;
    }

    public Class<?> getAnnotated() {
        return this._class;
    }

    public int getModifiers() {
        return this._class.getModifiers();
    }

    public String getName() {
        return this._class.getName();
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        if (this._classAnnotations == null) {
            return null;
        }
        return this._classAnnotations.get(acls);
    }

    public Type getGenericType() {
        return this._class;
    }

    public Class<?> getRawType() {
        return this._class;
    }

    protected AnnotationMap getAllAnnotations() {
        return this._classAnnotations;
    }

    public Annotations getAnnotations() {
        return this._classAnnotations;
    }

    public boolean hasAnnotations() {
        return this._classAnnotations.size() > 0;
    }

    public AnnotatedConstructor getDefaultConstructor() {
        return this._defaultConstructor;
    }

    public List<AnnotatedConstructor> getConstructors() {
        if (this._constructors == null) {
            return Collections.emptyList();
        }
        return this._constructors;
    }

    public List<AnnotatedMethod> getStaticMethods() {
        if (this._creatorMethods == null) {
            return Collections.emptyList();
        }
        return this._creatorMethods;
    }

    public Iterable<AnnotatedMethod> memberMethods() {
        return this._memberMethods;
    }

    public int getMemberMethodCount() {
        return this._memberMethods.size();
    }

    public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
        return this._memberMethods.find(name, paramTypes);
    }

    public int getFieldCount() {
        return this._fields == null ? 0 : this._fields.size();
    }

    public Iterable<AnnotatedField> fields() {
        if (this._fields == null) {
            return Collections.emptyList();
        }
        return this._fields;
    }

    public void resolveClassAnnotations() {
        this._classAnnotations = new AnnotationMap();
        if (this._annotationIntrospector != null) {
            if (this._primaryMixIn != null) {
                _addClassMixIns(this._classAnnotations, this._class, this._primaryMixIn);
            }
            for (Annotation a : this._class.getDeclaredAnnotations()) {
                if (this._annotationIntrospector.isHandled(a)) {
                    this._classAnnotations.addIfNotPresent(a);
                }
            }
            for (Class<?> cls : this._superTypes) {
                _addClassMixIns(this._classAnnotations, cls);
                for (Annotation a2 : cls.getDeclaredAnnotations()) {
                    if (this._annotationIntrospector.isHandled(a2)) {
                        this._classAnnotations.addIfNotPresent(a2);
                    }
                }
            }
            _addClassMixIns(this._classAnnotations, Object.class);
        }
    }

    public void resolveCreators(boolean includeAll) {
        int i;
        this._constructors = null;
        Constructor<?>[] declaredCtors = this._class.getDeclaredConstructors();
        for (Constructor<?> ctor : declaredCtors) {
            if (ctor.getParameterTypes().length == 0) {
                this._defaultConstructor = _constructConstructor(ctor, true);
            } else if (includeAll) {
                if (this._constructors == null) {
                    this._constructors = new ArrayList(Math.max(10, declaredCtors.length));
                }
                this._constructors.add(_constructConstructor(ctor, false));
            }
        }
        if (!(this._primaryMixIn == null || (this._defaultConstructor == null && this._constructors == null))) {
            _addConstructorMixIns(this._primaryMixIn);
        }
        if (this._annotationIntrospector != null) {
            if (this._defaultConstructor != null && this._annotationIntrospector.isIgnorableConstructor(this._defaultConstructor)) {
                this._defaultConstructor = null;
            }
            if (this._constructors != null) {
                i = this._constructors.size();
                while (true) {
                    i--;
                    if (i < 0) {
                        break;
                    } else if (this._annotationIntrospector.isIgnorableConstructor((AnnotatedConstructor) this._constructors.get(i))) {
                        this._constructors.remove(i);
                    }
                }
            }
        }
        this._creatorMethods = null;
        if (includeAll) {
            for (Method m : this._class.getDeclaredMethods()) {
                if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length >= 1) {
                    if (this._creatorMethods == null) {
                        this._creatorMethods = new ArrayList(8);
                    }
                    this._creatorMethods.add(_constructCreatorMethod(m));
                }
            }
            if (!(this._primaryMixIn == null || this._creatorMethods == null)) {
                _addFactoryMixIns(this._primaryMixIn);
            }
            if (this._annotationIntrospector != null && this._creatorMethods != null) {
                i = this._creatorMethods.size();
                while (true) {
                    i--;
                    if (i < 0) {
                        return;
                    }
                    if (this._annotationIntrospector.isIgnorableMethod((AnnotatedMethod) this._creatorMethods.get(i))) {
                        this._creatorMethods.remove(i);
                    }
                }
            }
        }
    }

    public void resolveMemberMethods(MethodFilter methodFilter) {
        this._memberMethods = new AnnotatedMethodMap();
        AnnotatedMethodMap mixins = new AnnotatedMethodMap();
        _addMemberMethods(this._class, methodFilter, this._memberMethods, this._primaryMixIn, mixins);
        for (Class<?> cls : this._superTypes) {
            _addMemberMethods(cls, methodFilter, this._memberMethods, this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(cls), mixins);
        }
        if (this._mixInResolver != null) {
            Class<?> mixin = this._mixInResolver.findMixInClassFor(Object.class);
            if (mixin != null) {
                _addMethodMixIns(this._class, methodFilter, this._memberMethods, mixin, mixins);
            }
        }
        if (this._annotationIntrospector != null && !mixins.isEmpty()) {
            Iterator<AnnotatedMethod> it = mixins.iterator();
            while (it.hasNext()) {
                AnnotatedMethod mixIn = (AnnotatedMethod) it.next();
                try {
                    Method m = Object.class.getDeclaredMethod(mixIn.getName(), mixIn.getParameterClasses());
                    if (m != null) {
                        AnnotatedMethod am = _constructMethod(m);
                        _addMixOvers(mixIn.getAnnotated(), am, false);
                        this._memberMethods.add(am);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void resolveFields() {
        LinkedHashMap<String, AnnotatedField> foundFields = new LinkedHashMap();
        _addFields(foundFields, this._class);
        if (foundFields.isEmpty()) {
            this._fields = Collections.emptyList();
            return;
        }
        this._fields = new ArrayList(foundFields.size());
        this._fields.addAll(foundFields.values());
    }

    @Deprecated
    public void resolveMemberMethods(MethodFilter methodFilter, boolean collectIgnored) {
        resolveMemberMethods(methodFilter);
    }

    @Deprecated
    public void resolveFields(boolean collectIgnored) {
        resolveFields();
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask) {
        if (this._mixInResolver != null) {
            _addClassMixIns(annotations, toMask, this._mixInResolver.findMixInClassFor(toMask));
        }
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask, Class<?> mixin) {
        if (mixin != null) {
            for (Annotation a : mixin.getDeclaredAnnotations()) {
                if (this._annotationIntrospector.isHandled(a)) {
                    annotations.addIfNotPresent(a);
                }
            }
            for (Class<?> parent : ClassUtil.findSuperTypes(mixin, toMask)) {
                for (Annotation a2 : parent.getDeclaredAnnotations()) {
                    if (this._annotationIntrospector.isHandled(a2)) {
                        annotations.addIfNotPresent(a2);
                    }
                }
            }
        }
    }

    protected void _addConstructorMixIns(Class<?> mixin) {
        MemberKey[] ctorKeys = null;
        int ctorCount = this._constructors == null ? 0 : this._constructors.size();
        for (Constructor ctor : mixin.getDeclaredConstructors()) {
            if (ctor.getParameterTypes().length != 0) {
                int i;
                if (ctorKeys == null) {
                    ctorKeys = new MemberKey[ctorCount];
                    for (i = 0; i < ctorCount; i++) {
                        ctorKeys[i] = new MemberKey(((AnnotatedConstructor) this._constructors.get(i)).getAnnotated());
                    }
                }
                MemberKey key = new MemberKey(ctor);
                for (i = 0; i < ctorCount; i++) {
                    if (key.equals(ctorKeys[i])) {
                        _addMixOvers(ctor, (AnnotatedConstructor) this._constructors.get(i), true);
                        break;
                    }
                }
            } else if (this._defaultConstructor != null) {
                _addMixOvers(ctor, this._defaultConstructor, false);
            }
        }
    }

    protected void _addFactoryMixIns(Class<?> mixin) {
        MemberKey[] methodKeys = null;
        int methodCount = this._creatorMethods.size();
        for (Method m : mixin.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length != 0) {
                int i;
                if (methodKeys == null) {
                    methodKeys = new MemberKey[methodCount];
                    for (i = 0; i < methodCount; i++) {
                        methodKeys[i] = new MemberKey(((AnnotatedMethod) this._creatorMethods.get(i)).getAnnotated());
                    }
                }
                MemberKey key = new MemberKey(m);
                for (i = 0; i < methodCount; i++) {
                    if (key.equals(methodKeys[i])) {
                        _addMixOvers(m, (AnnotatedMethod) this._creatorMethods.get(i), true);
                        break;
                    }
                }
            }
        }
    }

    protected void _addMemberMethods(Class<?> cls, MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        if (mixInCls != null) {
            _addMethodMixIns(cls, methodFilter, methods, mixInCls, mixIns);
        }
        if (cls != null) {
            for (Method m : cls.getDeclaredMethods()) {
                if (_isIncludableMethod(m, methodFilter)) {
                    AnnotatedMethod old = methods.find(m);
                    if (old == null) {
                        AnnotatedMethod newM = _constructMethod(m);
                        methods.add(newM);
                        old = mixIns.remove(m);
                        if (old != null) {
                            _addMixOvers(old.getAnnotated(), newM, false);
                        }
                    } else {
                        _addMixUnders(m, old);
                        if (old.getDeclaringClass().isInterface() && !m.getDeclaringClass().isInterface()) {
                            methods.add(old.withMethod(m));
                        }
                    }
                }
            }
        }
    }

    protected void _addMethodMixIns(Class<?> targetClass, MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        List<Class<?>> parents = new ArrayList();
        parents.add(mixInCls);
        ClassUtil.findSuperTypes(mixInCls, targetClass, parents);
        for (Class<?> mixin : parents) {
            for (Method m : mixin.getDeclaredMethods()) {
                if (_isIncludableMethod(m, methodFilter)) {
                    AnnotatedMethod am = methods.find(m);
                    if (am != null) {
                        _addMixUnders(m, am);
                    } else {
                        mixIns.add(_constructMethod(m));
                    }
                }
            }
        }
    }

    protected void _addFields(Map<String, AnnotatedField> fields, Class<?> c) {
        Class<?> parent = c.getSuperclass();
        if (parent != null) {
            _addFields(fields, parent);
            for (Field f : c.getDeclaredFields()) {
                if (_isIncludableField(f)) {
                    fields.put(f.getName(), _constructField(f));
                }
            }
            if (this._mixInResolver != null) {
                Class<?> mixin = this._mixInResolver.findMixInClassFor(c);
                if (mixin != null) {
                    _addFieldMixIns(parent, mixin, fields);
                }
            }
        }
    }

    protected void _addFieldMixIns(Class<?> targetClass, Class<?> mixInCls, Map<String, AnnotatedField> fields) {
        List<Class<?>> parents = new ArrayList();
        parents.add(mixInCls);
        ClassUtil.findSuperTypes(mixInCls, targetClass, parents);
        for (Class<?> mixin : parents) {
            for (Field mixinField : mixin.getDeclaredFields()) {
                if (_isIncludableField(mixinField)) {
                    AnnotatedField maskedField = (AnnotatedField) fields.get(mixinField.getName());
                    if (maskedField != null) {
                        for (Annotation a : mixinField.getDeclaredAnnotations()) {
                            if (this._annotationIntrospector.isHandled(a)) {
                                maskedField.addOrOverride(a);
                            }
                        }
                    }
                }
            }
        }
    }

    protected AnnotatedMethod _constructMethod(Method m) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedMethod(m, _emptyAnnotationMap(), null);
        }
        return new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), null);
    }

    protected AnnotatedConstructor _constructConstructor(Constructor<?> ctor, boolean defaultCtor) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedConstructor(ctor, _emptyAnnotationMap(), _emptyAnnotationMaps(ctor.getParameterTypes().length));
        }
        if (defaultCtor) {
            return new AnnotatedConstructor(ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), null);
        }
        Annotation[][] paramAnns = ctor.getParameterAnnotations();
        int paramCount = ctor.getParameterTypes().length;
        AnnotationMap[] resolvedAnnotations = null;
        if (paramCount != paramAnns.length) {
            Class<?> dc = ctor.getDeclaringClass();
            Annotation[][] old;
            if (dc.isEnum() && paramCount == paramAnns.length + 2) {
                old = paramAnns;
                paramAnns = new Annotation[(old.length + 2)][];
                System.arraycopy(old, 0, paramAnns, 2, old.length);
                resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
            } else if (dc.isMemberClass() && paramCount == paramAnns.length + 1) {
                old = paramAnns;
                paramAnns = new Annotation[(old.length + 1)][];
                System.arraycopy(old, 0, paramAnns, 1, old.length);
                resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
            }
            if (resolvedAnnotations == null) {
                throw new IllegalStateException("Internal error: constructor for " + ctor.getDeclaringClass().getName() + " has mismatch: " + paramCount + " parameters; " + paramAnns.length + " sets of annotations");
            }
        }
        resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
        return new AnnotatedConstructor(ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), resolvedAnnotations);
    }

    protected AnnotatedMethod _constructCreatorMethod(Method m) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedMethod(m, _emptyAnnotationMap(), _emptyAnnotationMaps(m.getParameterTypes().length));
        }
        return new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), _collectRelevantAnnotations(m.getParameterAnnotations()));
    }

    protected AnnotatedField _constructField(Field f) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedField(f, _emptyAnnotationMap());
        }
        return new AnnotatedField(f, _collectRelevantAnnotations(f.getDeclaredAnnotations()));
    }

    protected AnnotationMap[] _collectRelevantAnnotations(Annotation[][] anns) {
        int len = anns.length;
        AnnotationMap[] result = new AnnotationMap[len];
        for (int i = 0; i < len; i++) {
            result[i] = _collectRelevantAnnotations(anns[i]);
        }
        return result;
    }

    protected AnnotationMap _collectRelevantAnnotations(Annotation[] anns) {
        AnnotationMap annMap = new AnnotationMap();
        if (anns != null) {
            for (Annotation a : anns) {
                if (this._annotationIntrospector.isHandled(a)) {
                    annMap.add(a);
                }
            }
        }
        return annMap;
    }

    private AnnotationMap _emptyAnnotationMap() {
        return new AnnotationMap();
    }

    private AnnotationMap[] _emptyAnnotationMaps(int count) {
        if (count == 0) {
            return NO_ANNOTATION_MAPS;
        }
        AnnotationMap[] maps = new AnnotationMap[count];
        for (int i = 0; i < count; i++) {
            maps[i] = _emptyAnnotationMap();
        }
        return maps;
    }

    protected boolean _isIncludableMethod(Method m, MethodFilter filter) {
        if ((filter != null && !filter.includeMethod(m)) || m.isSynthetic() || m.isBridge()) {
            return false;
        }
        return true;
    }

    private boolean _isIncludableField(Field f) {
        if (f.isSynthetic()) {
            return false;
        }
        int mods = f.getModifiers();
        if (Modifier.isStatic(mods) || Modifier.isTransient(mods)) {
            return false;
        }
        return true;
    }

    protected void _addMixOvers(Constructor<?> mixin, AnnotatedConstructor target, boolean addParamAnnotations) {
        for (Annotation a : mixin.getDeclaredAnnotations()) {
            if (this._annotationIntrospector.isHandled(a)) {
                target.addOrOverride(a);
            }
        }
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int len = pa.length;
            for (int i = 0; i < len; i++) {
                for (Annotation a2 : pa[i]) {
                    target.addOrOverrideParam(i, a2);
                }
            }
        }
    }

    protected void _addMixOvers(Method mixin, AnnotatedMethod target, boolean addParamAnnotations) {
        for (Annotation a : mixin.getDeclaredAnnotations()) {
            if (this._annotationIntrospector.isHandled(a)) {
                target.addOrOverride(a);
            }
        }
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int len = pa.length;
            for (int i = 0; i < len; i++) {
                for (Annotation a2 : pa[i]) {
                    target.addOrOverrideParam(i, a2);
                }
            }
        }
    }

    protected void _addMixUnders(Method src, AnnotatedMethod target) {
        for (Annotation a : src.getDeclaredAnnotations()) {
            if (this._annotationIntrospector.isHandled(a)) {
                target.addIfNotPresent(a);
            }
        }
    }

    public String toString() {
        return "[AnnotedClass " + this._class.getName() + RequestParameters.RIGHT_BRACKETS;
    }
}
