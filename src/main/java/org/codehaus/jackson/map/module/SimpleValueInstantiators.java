package org.codehaus.jackson.map.module;

import java.util.HashMap;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.deser.ValueInstantiator;
import org.codehaus.jackson.map.deser.ValueInstantiators.Base;
import org.codehaus.jackson.map.type.ClassKey;

public class SimpleValueInstantiators extends Base {
    protected HashMap<ClassKey, ValueInstantiator> _classMappings = new HashMap();

    public SimpleValueInstantiators addValueInstantiator(Class<?> forType, ValueInstantiator inst) {
        this._classMappings.put(new ClassKey(forType), inst);
        return this;
    }

    public ValueInstantiator findValueInstantiator(DeserializationConfig config, BeanDescription beanDesc, ValueInstantiator defaultInstantiator) {
        ValueInstantiator inst = (ValueInstantiator) this._classMappings.get(new ClassKey(beanDesc.getBeanClass()));
        return inst == null ? defaultInstantiator : inst;
    }
}
