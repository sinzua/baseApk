package org.codehaus.jackson.map.ser.impl;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.FilterProvider;

public class SimpleFilterProvider extends FilterProvider {
    protected boolean _cfgFailOnUnknownId;
    protected BeanPropertyFilter _defaultFilter;
    protected final Map<String, BeanPropertyFilter> _filtersById;

    public SimpleFilterProvider() {
        this(new HashMap());
    }

    public SimpleFilterProvider(Map<String, BeanPropertyFilter> mapping) {
        this._cfgFailOnUnknownId = true;
        this._filtersById = mapping;
    }

    public SimpleFilterProvider setDefaultFilter(BeanPropertyFilter f) {
        this._defaultFilter = f;
        return this;
    }

    public BeanPropertyFilter getDefaultFilter() {
        return this._defaultFilter;
    }

    public SimpleFilterProvider setFailOnUnknownId(boolean state) {
        this._cfgFailOnUnknownId = state;
        return this;
    }

    public boolean willFailOnUnknownId() {
        return this._cfgFailOnUnknownId;
    }

    public SimpleFilterProvider addFilter(String id, BeanPropertyFilter filter) {
        this._filtersById.put(id, filter);
        return this;
    }

    public BeanPropertyFilter removeFilter(String id) {
        return (BeanPropertyFilter) this._filtersById.remove(id);
    }

    public BeanPropertyFilter findFilter(Object filterId) {
        BeanPropertyFilter f = (BeanPropertyFilter) this._filtersById.get(filterId);
        if (f == null) {
            f = this._defaultFilter;
            if (f == null && this._cfgFailOnUnknownId) {
                throw new IllegalArgumentException("No filter configured with id '" + filterId + "' (type " + filterId.getClass().getName() + ")");
            }
        }
        return f;
    }
}
