package org.codehaus.jackson.map.ser;

public abstract class FilterProvider {
    public abstract BeanPropertyFilter findFilter(Object obj);
}
