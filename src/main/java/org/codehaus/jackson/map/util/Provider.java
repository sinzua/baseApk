package org.codehaus.jackson.map.util;

import java.util.Collection;

public interface Provider<T> {
    Collection<T> provide();
}
