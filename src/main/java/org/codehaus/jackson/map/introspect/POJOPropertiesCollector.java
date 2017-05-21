package org.codehaus.jackson.map.introspect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanPropertyDefinition;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.util.BeanUtil;
import org.codehaus.jackson.type.JavaType;

public class POJOPropertiesCollector {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected LinkedList<AnnotatedMethod> _anyGetters = null;
    protected LinkedList<AnnotatedMethod> _anySetters = null;
    protected final AnnotatedClass _classDef;
    protected final MapperConfig<?> _config;
    protected LinkedList<POJOPropertyBuilder> _creatorProperties = null;
    protected final boolean _forSerialization;
    protected Set<String> _ignoredPropertyNames;
    protected Set<String> _ignoredPropertyNamesForDeser;
    protected LinkedHashMap<Object, AnnotatedMember> _injectables;
    protected LinkedList<AnnotatedMethod> _jsonValueGetters = null;
    protected final LinkedHashMap<String, POJOPropertyBuilder> _properties = new LinkedHashMap();
    protected final JavaType _type;
    protected final VisibilityChecker<?> _visibilityChecker;

    protected POJOPropertiesCollector(MapperConfig<?> config, boolean forSerialization, JavaType type, AnnotatedClass classDef) {
        AnnotationIntrospector annotationIntrospector = null;
        this._config = config;
        this._forSerialization = forSerialization;
        this._type = type;
        this._classDef = classDef;
        if (config.isAnnotationProcessingEnabled()) {
            annotationIntrospector = this._config.getAnnotationIntrospector();
        }
        this._annotationIntrospector = annotationIntrospector;
        if (this._annotationIntrospector == null) {
            this._visibilityChecker = this._config.getDefaultVisibilityChecker();
        } else {
            this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(classDef, this._config.getDefaultVisibilityChecker());
        }
    }

    public MapperConfig<?> getConfig() {
        return this._config;
    }

    public JavaType getType() {
        return this._type;
    }

    public AnnotatedClass getClassDef() {
        return this._classDef;
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        return this._annotationIntrospector;
    }

    public List<BeanPropertyDefinition> getProperties() {
        return new ArrayList(this._properties.values());
    }

    public Map<Object, AnnotatedMember> getInjectables() {
        return this._injectables;
    }

    public AnnotatedMethod getJsonValueMethod() {
        if (this._jsonValueGetters == null) {
            return null;
        }
        if (this._jsonValueGetters.size() > 1) {
            reportProblem("Multiple value properties defined (" + this._jsonValueGetters.get(0) + " vs " + this._jsonValueGetters.get(1) + ")");
        }
        return (AnnotatedMethod) this._jsonValueGetters.get(0);
    }

    public AnnotatedMethod getAnyGetterMethod() {
        if (this._anyGetters == null) {
            return null;
        }
        if (this._anyGetters.size() > 1) {
            reportProblem("Multiple 'any-getters' defined (" + this._anyGetters.get(0) + " vs " + this._anyGetters.get(1) + ")");
        }
        return (AnnotatedMethod) this._anyGetters.getFirst();
    }

    public AnnotatedMethod getAnySetterMethod() {
        if (this._anySetters == null) {
            return null;
        }
        if (this._anySetters.size() > 1) {
            reportProblem("Multiple 'any-setters' defined (" + this._anySetters.get(0) + " vs " + this._anySetters.get(1) + ")");
        }
        return (AnnotatedMethod) this._anySetters.getFirst();
    }

    public Set<String> getIgnoredPropertyNames() {
        return this._ignoredPropertyNames;
    }

    public Set<String> getIgnoredPropertyNamesForDeser() {
        return this._ignoredPropertyNamesForDeser;
    }

    protected Map<String, POJOPropertyBuilder> getPropertyMap() {
        return this._properties;
    }

    public POJOPropertiesCollector collect() {
        this._properties.clear();
        _addFields();
        _addMethods();
        _addCreators();
        _addInjectables();
        _removeUnwantedProperties();
        _renameProperties();
        PropertyNamingStrategy naming = this._config.getPropertyNamingStrategy();
        if (naming != null) {
            _renameUsing(naming);
        }
        for (POJOPropertyBuilder property : this._properties.values()) {
            property.trimByVisibility();
        }
        for (POJOPropertyBuilder property2 : this._properties.values()) {
            property2.mergeAnnotations(this._forSerialization);
        }
        _sortProperties();
        return this;
    }

    protected void _sortProperties() {
        boolean sort;
        AnnotationIntrospector intr = this._config.getAnnotationIntrospector();
        Boolean alpha = intr.findSerializationSortAlphabetically(this._classDef);
        if (alpha == null) {
            sort = this._config.shouldSortPropertiesAlphabetically();
        } else {
            sort = alpha.booleanValue();
        }
        String[] propertyOrder = intr.findSerializationPropertyOrder(this._classDef);
        if (sort || this._creatorProperties != null || propertyOrder != null) {
            Map<String, POJOPropertyBuilder> all;
            Iterator i$;
            POJOPropertyBuilder prop;
            int size = this._properties.size();
            if (sort) {
                all = new TreeMap();
            } else {
                all = new LinkedHashMap(size + size);
            }
            for (POJOPropertyBuilder prop2 : this._properties.values()) {
                all.put(prop2.getName(), prop2);
            }
            Map<String, POJOPropertyBuilder> ordered = new LinkedHashMap(size + size);
            if (propertyOrder != null) {
                for (String name : propertyOrder) {
                    String name2;
                    POJOPropertyBuilder w = (POJOPropertyBuilder) all.get(name2);
                    if (w == null) {
                        for (POJOPropertyBuilder prop22 : this._properties.values()) {
                            if (name2.equals(prop22.getInternalName())) {
                                w = prop22;
                                name2 = prop22.getName();
                                break;
                            }
                        }
                    }
                    if (w != null) {
                        ordered.put(name2, w);
                    }
                }
            }
            if (this._creatorProperties != null) {
                i$ = this._creatorProperties.iterator();
                while (i$.hasNext()) {
                    prop22 = (POJOPropertyBuilder) i$.next();
                    ordered.put(prop22.getName(), prop22);
                }
            }
            ordered.putAll(all);
            this._properties.clear();
            this._properties.putAll(ordered);
        }
    }

    protected void _addFields() {
        AnnotationIntrospector ai = this._annotationIntrospector;
        for (AnnotatedField f : this._classDef.fields()) {
            String explName;
            boolean visible;
            boolean ignored;
            String implName = f.getName();
            if (ai == null) {
                explName = null;
            } else if (this._forSerialization) {
                explName = ai.findSerializablePropertyName(f);
            } else {
                explName = ai.findDeserializablePropertyName(f);
            }
            if ("".equals(explName)) {
                explName = implName;
            }
            if (explName != null) {
                visible = true;
            } else {
                visible = false;
            }
            if (!visible) {
                visible = this._visibilityChecker.isFieldVisible(f);
            }
            if (ai == null || !ai.hasIgnoreMarker(f)) {
                ignored = false;
            } else {
                ignored = true;
            }
            _property(implName).addField(f, explName, visible, ignored);
        }
    }

    protected void _addCreators() {
        AnnotationIntrospector ai = this._annotationIntrospector;
        if (ai != null) {
            int len;
            int i;
            AnnotatedParameter param;
            String name;
            POJOPropertyBuilder prop;
            for (AnnotatedConstructor ctor : this._classDef.getConstructors()) {
                if (this._creatorProperties == null) {
                    this._creatorProperties = new LinkedList();
                }
                len = ctor.getParameterCount();
                for (i = 0; i < len; i++) {
                    param = ctor.getParameter(i);
                    name = ai.findPropertyNameForParam(param);
                    if (name != null) {
                        prop = _property(name);
                        prop.addCtor(param, name, true, false);
                        this._creatorProperties.add(prop);
                    }
                }
            }
            for (AnnotatedMethod factory : this._classDef.getStaticMethods()) {
                if (this._creatorProperties == null) {
                    this._creatorProperties = new LinkedList();
                }
                len = factory.getParameterCount();
                for (i = 0; i < len; i++) {
                    param = factory.getParameter(i);
                    name = ai.findPropertyNameForParam(param);
                    if (name != null) {
                        prop = _property(name);
                        prop.addCtor(param, name, true, false);
                        this._creatorProperties.add(prop);
                    }
                }
            }
        }
    }

    protected void _addMethods() {
        AnnotationIntrospector ai = this._annotationIntrospector;
        for (AnnotatedMethod m : this._classDef.memberMethods()) {
            int argCount = m.getParameterCount();
            String explName;
            String implName;
            boolean visible;
            if (argCount == 0) {
                if (ai != null) {
                    if (ai.hasAnyGetterAnnotation(m)) {
                        if (this._anyGetters == null) {
                            this._anyGetters = new LinkedList();
                        }
                        this._anyGetters.add(m);
                    } else if (ai.hasAsValueAnnotation(m)) {
                        if (this._jsonValueGetters == null) {
                            this._jsonValueGetters = new LinkedList();
                        }
                        this._jsonValueGetters.add(m);
                    }
                }
                explName = ai == null ? null : ai.findGettablePropertyName(m);
                if (explName == null) {
                    implName = BeanUtil.okNameForRegularGetter(m, m.getName());
                    if (implName == null) {
                        implName = BeanUtil.okNameForIsGetter(m, m.getName());
                        if (implName != null) {
                            visible = this._visibilityChecker.isIsGetterVisible(m);
                        }
                    } else {
                        visible = this._visibilityChecker.isGetterVisible(m);
                    }
                } else {
                    implName = BeanUtil.okNameForGetter(m);
                    if (implName == null) {
                        implName = m.getName();
                    }
                    if (explName.length() == 0) {
                        explName = implName;
                    }
                    visible = true;
                }
                _property(implName).addGetter(m, explName, visible, ai == null ? false : ai.hasIgnoreMarker(m));
            } else if (argCount == 1) {
                explName = ai == null ? null : ai.findSettablePropertyName(m);
                if (explName == null) {
                    implName = BeanUtil.okNameForSetter(m);
                    if (implName != null) {
                        visible = this._visibilityChecker.isSetterVisible(m);
                    }
                } else {
                    implName = BeanUtil.okNameForSetter(m);
                    if (implName == null) {
                        implName = m.getName();
                    }
                    if (explName.length() == 0) {
                        explName = implName;
                    }
                    visible = true;
                }
                _property(implName).addSetter(m, explName, visible, ai == null ? false : ai.hasIgnoreMarker(m));
            } else if (argCount == 2 && ai != null && ai.hasAnySetterAnnotation(m)) {
                if (this._anySetters == null) {
                    this._anySetters = new LinkedList();
                }
                this._anySetters.add(m);
            }
        }
    }

    protected void _addInjectables() {
        AnnotationIntrospector ai = this._annotationIntrospector;
        if (ai != null) {
            for (AnnotatedField f : this._classDef.fields()) {
                _doAddInjectable(ai.findInjectableValueId(f), f);
            }
            for (AnnotatedMethod m : this._classDef.memberMethods()) {
                if (m.getParameterCount() == 1) {
                    _doAddInjectable(ai.findInjectableValueId(m), m);
                }
            }
        }
    }

    protected void _doAddInjectable(Object id, AnnotatedMember m) {
        if (id != null) {
            if (this._injectables == null) {
                this._injectables = new LinkedHashMap();
            }
            if (((AnnotatedMember) this._injectables.put(id, m)) != null) {
                throw new IllegalArgumentException("Duplicate injectable value with id '" + String.valueOf(id) + "' (of type " + (id == null ? "[null]" : id.getClass().getName()) + ")");
            }
        }
    }

    protected void _removeUnwantedProperties() {
        Iterator<Entry<String, POJOPropertyBuilder>> it = this._properties.entrySet().iterator();
        while (it.hasNext()) {
            POJOPropertyBuilder prop = (POJOPropertyBuilder) ((Entry) it.next()).getValue();
            if (prop.anyVisible()) {
                if (prop.anyIgnorals()) {
                    _addIgnored(prop);
                    if (prop.anyExplicitNames()) {
                        prop.removeIgnored();
                    } else {
                        it.remove();
                    }
                }
                prop.removeNonVisible();
            } else {
                it.remove();
            }
        }
    }

    private void _addIgnored(POJOPropertyBuilder prop) {
        if (!this._forSerialization) {
            String name = prop.getName();
            this._ignoredPropertyNames = addToSet(this._ignoredPropertyNames, name);
            if (prop.anyDeserializeIgnorals()) {
                this._ignoredPropertyNamesForDeser = addToSet(this._ignoredPropertyNamesForDeser, name);
            }
        }
    }

    protected void _renameProperties() {
        Iterator<Entry<String, POJOPropertyBuilder>> it = this._properties.entrySet().iterator();
        LinkedList<POJOPropertyBuilder> renamed = null;
        while (it.hasNext()) {
            POJOPropertyBuilder prop = (POJOPropertyBuilder) ((Entry) it.next()).getValue();
            String newName = prop.findNewName();
            if (newName != null) {
                if (renamed == null) {
                    renamed = new LinkedList();
                }
                renamed.add(prop.withName(newName));
                it.remove();
            }
        }
        if (renamed != null) {
            Iterator i$ = renamed.iterator();
            while (i$.hasNext()) {
                prop = (POJOPropertyBuilder) i$.next();
                String name = prop.getName();
                POJOPropertyBuilder old = (POJOPropertyBuilder) this._properties.get(name);
                if (old == null) {
                    this._properties.put(name, prop);
                } else {
                    old.addAll(prop);
                }
            }
        }
    }

    protected void _renameUsing(PropertyNamingStrategy naming) {
        POJOPropertyBuilder[] props = (POJOPropertyBuilder[]) this._properties.values().toArray(new POJOPropertyBuilder[this._properties.size()]);
        this._properties.clear();
        for (POJOPropertyBuilder prop : props) {
            POJOPropertyBuilder prop2;
            String name = prop2.getName();
            if (this._forSerialization) {
                if (prop2.hasGetter()) {
                    name = naming.nameForGetterMethod(this._config, prop2.getGetter(), name);
                } else if (prop2.hasField()) {
                    name = naming.nameForField(this._config, prop2.getField(), name);
                }
            } else if (prop2.hasSetter()) {
                name = naming.nameForSetterMethod(this._config, prop2.getSetter(), name);
            } else if (prop2.hasConstructorParameter()) {
                name = naming.nameForConstructorParameter(this._config, prop2.getConstructorParameter(), name);
            } else if (prop2.hasField()) {
                name = naming.nameForField(this._config, prop2.getField(), name);
            } else if (prop2.hasGetter()) {
                name = naming.nameForGetterMethod(this._config, prop2.getGetter(), name);
            }
            if (!name.equals(prop2.getName())) {
                prop2 = prop2.withName(name);
            }
            POJOPropertyBuilder old = (POJOPropertyBuilder) this._properties.get(name);
            if (old == null) {
                this._properties.put(name, prop2);
            } else {
                old.addAll(prop2);
            }
        }
    }

    protected void reportProblem(String msg) {
        throw new IllegalArgumentException("Problem with definition of " + this._classDef + ": " + msg);
    }

    protected POJOPropertyBuilder _property(String implName) {
        POJOPropertyBuilder prop = (POJOPropertyBuilder) this._properties.get(implName);
        if (prop != null) {
            return prop;
        }
        prop = new POJOPropertyBuilder(implName);
        this._properties.put(implName, prop);
        return prop;
    }

    private Set<String> addToSet(Set<String> set, String str) {
        if (set == null) {
            set = new HashSet();
        }
        set.add(str);
        return set;
    }
}
