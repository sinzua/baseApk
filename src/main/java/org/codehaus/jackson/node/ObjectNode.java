package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public class ObjectNode extends ContainerNode {
    protected LinkedHashMap<String, JsonNode> _children = null;

    protected static class NoFieldsIterator implements Iterator<Entry<String, JsonNode>> {
        static final NoFieldsIterator instance = new NoFieldsIterator();

        private NoFieldsIterator() {
        }

        public boolean hasNext() {
            return false;
        }

        public Entry<String, JsonNode> next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }
    }

    public ObjectNode(JsonNodeFactory nc) {
        super(nc);
    }

    public JsonToken asToken() {
        return JsonToken.START_OBJECT;
    }

    public boolean isObject() {
        return true;
    }

    public int size() {
        return this._children == null ? 0 : this._children.size();
    }

    public Iterator<JsonNode> getElements() {
        return this._children == null ? NoNodesIterator.instance() : this._children.values().iterator();
    }

    public JsonNode get(int index) {
        return null;
    }

    public JsonNode get(String fieldName) {
        if (this._children != null) {
            return (JsonNode) this._children.get(fieldName);
        }
        return null;
    }

    public Iterator<String> getFieldNames() {
        return this._children == null ? NoStringsIterator.instance() : this._children.keySet().iterator();
    }

    public JsonNode path(int index) {
        return MissingNode.getInstance();
    }

    public JsonNode path(String fieldName) {
        if (this._children != null) {
            JsonNode n = (JsonNode) this._children.get(fieldName);
            if (n != null) {
                return n;
            }
        }
        return MissingNode.getInstance();
    }

    public Iterator<Entry<String, JsonNode>> getFields() {
        if (this._children == null) {
            return NoFieldsIterator.instance;
        }
        return this._children.entrySet().iterator();
    }

    public ObjectNode with(String propertyName) {
        if (this._children == null) {
            this._children = new LinkedHashMap();
        } else {
            JsonNode n = (JsonNode) this._children.get(propertyName);
            if (n != null) {
                if (n instanceof ObjectNode) {
                    return (ObjectNode) n;
                }
                throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ObjectNode (but " + n.getClass().getName() + ")");
            }
        }
        ObjectNode result = objectNode();
        this._children.put(propertyName, result);
        return result;
    }

    public JsonNode findValue(String fieldName) {
        if (this._children != null) {
            for (Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    return (JsonNode) entry.getValue();
                }
                JsonNode value = ((JsonNode) entry.getValue()).findValue(fieldName);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            for (Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList();
                    }
                    foundSoFar.add(entry.getValue());
                } else {
                    foundSoFar = ((JsonNode) entry.getValue()).findValues(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        if (this._children != null) {
            for (Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList();
                    }
                    foundSoFar.add(((JsonNode) entry.getValue()).asText());
                } else {
                    foundSoFar = ((JsonNode) entry.getValue()).findValuesAsText(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    public ObjectNode findParent(String fieldName) {
        if (this._children != null) {
            for (Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    return this;
                }
                JsonNode value = ((JsonNode) entry.getValue()).findParent(fieldName);
                if (value != null) {
                    return (ObjectNode) value;
                }
            }
        }
        return null;
    }

    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        if (this._children != null) {
            for (Entry<String, JsonNode> entry : this._children.entrySet()) {
                if (fieldName.equals(entry.getKey())) {
                    if (foundSoFar == null) {
                        foundSoFar = new ArrayList();
                    }
                    foundSoFar.add(this);
                } else {
                    foundSoFar = ((JsonNode) entry.getValue()).findParents(fieldName, foundSoFar);
                }
            }
        }
        return foundSoFar;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeStartObject();
        if (this._children != null) {
            for (Entry<String, JsonNode> en : this._children.entrySet()) {
                jg.writeFieldName((String) en.getKey());
                ((BaseJsonNode) en.getValue()).serialize(jg, provider);
            }
        }
        jg.writeEndObject();
    }

    public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
        typeSer.writeTypePrefixForObject(this, jg);
        if (this._children != null) {
            for (Entry<String, JsonNode> en : this._children.entrySet()) {
                jg.writeFieldName((String) en.getKey());
                ((BaseJsonNode) en.getValue()).serialize(jg, provider);
            }
        }
        typeSer.writeTypeSuffixForObject(this, jg);
    }

    public JsonNode put(String fieldName, JsonNode value) {
        if (value == null) {
            value = nullNode();
        }
        return _put(fieldName, value);
    }

    public JsonNode remove(String fieldName) {
        if (this._children != null) {
            return (JsonNode) this._children.remove(fieldName);
        }
        return null;
    }

    public ObjectNode remove(Collection<String> fieldNames) {
        if (this._children != null) {
            for (String fieldName : fieldNames) {
                this._children.remove(fieldName);
            }
        }
        return this;
    }

    public ObjectNode removeAll() {
        this._children = null;
        return this;
    }

    public JsonNode putAll(Map<String, JsonNode> properties) {
        if (this._children == null) {
            this._children = new LinkedHashMap(properties);
        } else {
            for (Entry<String, JsonNode> en : properties.entrySet()) {
                JsonNode n = (JsonNode) en.getValue();
                if (n == null) {
                    n = nullNode();
                }
                this._children.put(en.getKey(), n);
            }
        }
        return this;
    }

    public JsonNode putAll(ObjectNode other) {
        int len = other.size();
        if (len > 0) {
            if (this._children == null) {
                this._children = new LinkedHashMap(len);
            }
            other.putContentsTo(this._children);
        }
        return this;
    }

    public ObjectNode retain(Collection<String> fieldNames) {
        if (this._children != null) {
            Iterator<Entry<String, JsonNode>> entries = this._children.entrySet().iterator();
            while (entries.hasNext()) {
                if (!fieldNames.contains(((Entry) entries.next()).getKey())) {
                    entries.remove();
                }
            }
        }
        return this;
    }

    public ObjectNode retain(String... fieldNames) {
        return retain(Arrays.asList(fieldNames));
    }

    public ArrayNode putArray(String fieldName) {
        ArrayNode n = arrayNode();
        _put(fieldName, n);
        return n;
    }

    public ObjectNode putObject(String fieldName) {
        ObjectNode n = objectNode();
        _put(fieldName, n);
        return n;
    }

    public void putPOJO(String fieldName, Object pojo) {
        _put(fieldName, POJONode(pojo));
    }

    public void putNull(String fieldName) {
        _put(fieldName, nullNode());
    }

    public void put(String fieldName, int v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, Integer value) {
        if (value == null) {
            _put(fieldName, nullNode());
        } else {
            _put(fieldName, numberNode(value.intValue()));
        }
    }

    public void put(String fieldName, long v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, Long value) {
        if (value == null) {
            _put(fieldName, nullNode());
        } else {
            _put(fieldName, numberNode(value.longValue()));
        }
    }

    public void put(String fieldName, float v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, Float value) {
        if (value == null) {
            _put(fieldName, nullNode());
        } else {
            _put(fieldName, numberNode(value.floatValue()));
        }
    }

    public void put(String fieldName, double v) {
        _put(fieldName, numberNode(v));
    }

    public void put(String fieldName, Double value) {
        if (value == null) {
            _put(fieldName, nullNode());
        } else {
            _put(fieldName, numberNode(value.doubleValue()));
        }
    }

    public void put(String fieldName, BigDecimal v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, numberNode(v));
        }
    }

    public void put(String fieldName, String v) {
        if (v == null) {
            putNull(fieldName);
        } else {
            _put(fieldName, textNode(v));
        }
    }

    public void put(String fieldName, boolean v) {
        _put(fieldName, booleanNode(v));
    }

    public void put(String fieldName, Boolean value) {
        if (value == null) {
            _put(fieldName, nullNode());
        } else {
            _put(fieldName, booleanNode(value.booleanValue()));
        }
    }

    public void put(String fieldName, byte[] v) {
        if (v == null) {
            _put(fieldName, nullNode());
        } else {
            _put(fieldName, binaryNode(v));
        }
    }

    protected void putContentsTo(Map<String, JsonNode> dst) {
        if (this._children != null) {
            for (Entry<String, JsonNode> en : this._children.entrySet()) {
                dst.put(en.getKey(), en.getValue());
            }
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        ObjectNode other = (ObjectNode) o;
        if (other.size() != size()) {
            return false;
        }
        if (this._children == null) {
            return true;
        }
        for (Entry<String, JsonNode> en : this._children.entrySet()) {
            JsonNode value = (JsonNode) en.getValue();
            JsonNode otherValue = other.get((String) en.getKey());
            if (otherValue != null) {
                if (!otherValue.equals(value)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this._children == null ? -1 : this._children.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder((size() << 4) + 32);
        sb.append("{");
        if (this._children != null) {
            int count = 0;
            for (Entry<String, JsonNode> en : this._children.entrySet()) {
                if (count > 0) {
                    sb.append(",");
                }
                count++;
                TextNode.appendQuoted(sb, (String) en.getKey());
                sb.append(':');
                sb.append(((JsonNode) en.getValue()).toString());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private final JsonNode _put(String fieldName, JsonNode value) {
        if (this._children == null) {
            this._children = new LinkedHashMap();
        }
        return (JsonNode) this._children.put(fieldName, value);
    }
}
