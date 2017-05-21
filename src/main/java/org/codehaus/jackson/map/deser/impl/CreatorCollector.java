package org.codehaus.jackson.map.deser.impl;

import java.lang.reflect.Member;
import java.util.HashMap;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.deser.ValueInstantiator;
import org.codehaus.jackson.map.deser.std.StdValueInstantiator;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedWithParams;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class CreatorCollector {
    final BasicBeanDescription _beanDesc;
    protected AnnotatedWithParams _booleanCreator;
    final boolean _canFixAccess;
    protected AnnotatedConstructor _defaultConstructor;
    protected AnnotatedWithParams _delegateCreator;
    protected AnnotatedWithParams _doubleCreator;
    protected AnnotatedWithParams _intCreator;
    protected AnnotatedWithParams _longCreator;
    protected CreatorProperty[] _propertyBasedArgs = null;
    protected AnnotatedWithParams _propertyBasedCreator;
    protected AnnotatedWithParams _stringCreator;

    public CreatorCollector(BasicBeanDescription beanDesc, boolean canFixAccess) {
        this._beanDesc = beanDesc;
        this._canFixAccess = canFixAccess;
    }

    public ValueInstantiator constructValueInstantiator(DeserializationConfig config) {
        JavaType delegateType;
        StdValueInstantiator inst = new StdValueInstantiator(config, this._beanDesc.getType());
        if (this._delegateCreator == null) {
            delegateType = null;
        } else {
            delegateType = this._beanDesc.bindingsForBeanType().resolveType(this._delegateCreator.getParameterType(0));
        }
        inst.configureFromObjectSettings(this._defaultConstructor, this._delegateCreator, delegateType, this._propertyBasedCreator, this._propertyBasedArgs);
        inst.configureFromStringCreator(this._stringCreator);
        inst.configureFromIntCreator(this._intCreator);
        inst.configureFromLongCreator(this._longCreator);
        inst.configureFromDoubleCreator(this._doubleCreator);
        inst.configureFromBooleanCreator(this._booleanCreator);
        return inst;
    }

    public void setDefaultConstructor(AnnotatedConstructor ctor) {
        this._defaultConstructor = ctor;
    }

    public void addStringCreator(AnnotatedWithParams creator) {
        this._stringCreator = verifyNonDup(creator, this._stringCreator, "String");
    }

    public void addIntCreator(AnnotatedWithParams creator) {
        this._intCreator = verifyNonDup(creator, this._intCreator, "int");
    }

    public void addLongCreator(AnnotatedWithParams creator) {
        this._longCreator = verifyNonDup(creator, this._longCreator, "long");
    }

    public void addDoubleCreator(AnnotatedWithParams creator) {
        this._doubleCreator = verifyNonDup(creator, this._doubleCreator, "double");
    }

    public void addBooleanCreator(AnnotatedWithParams creator) {
        this._booleanCreator = verifyNonDup(creator, this._booleanCreator, "boolean");
    }

    public void addDelegatingCreator(AnnotatedWithParams creator) {
        this._delegateCreator = verifyNonDup(creator, this._delegateCreator, "delegate");
    }

    public void addPropertyCreator(AnnotatedWithParams creator, CreatorProperty[] properties) {
        this._propertyBasedCreator = verifyNonDup(creator, this._propertyBasedCreator, "property-based");
        if (properties.length > 1) {
            HashMap<String, Integer> names = new HashMap();
            int i = 0;
            int len = properties.length;
            while (i < len) {
                String name = properties[i].getName();
                if (name.length() != 0 || properties[i].getInjectableValueId() == null) {
                    Integer old = (Integer) names.put(name, Integer.valueOf(i));
                    if (old != null) {
                        throw new IllegalArgumentException("Duplicate creator property \"" + name + "\" (index " + old + " vs " + i + ")");
                    }
                }
                i++;
            }
        }
        this._propertyBasedArgs = properties;
    }

    protected AnnotatedWithParams verifyNonDup(AnnotatedWithParams newOne, AnnotatedWithParams oldOne, String type) {
        if (oldOne == null || oldOne.getClass() != newOne.getClass()) {
            if (this._canFixAccess) {
                ClassUtil.checkAndFixAccess((Member) newOne.getAnnotated());
            }
            return newOne;
        }
        throw new IllegalArgumentException("Conflicting " + type + " creators: already had " + oldOne + ", encountered " + newOne);
    }
}
