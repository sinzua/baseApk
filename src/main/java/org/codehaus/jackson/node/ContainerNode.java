package org.codehaus.jackson.node;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonToken;

public abstract class ContainerNode extends BaseJsonNode {
    JsonNodeFactory _nodeFactory;

    protected static class NoNodesIterator implements Iterator<JsonNode> {
        static final NoNodesIterator instance = new NoNodesIterator();

        private NoNodesIterator() {
        }

        public static NoNodesIterator instance() {
            return instance;
        }

        public boolean hasNext() {
            return false;
        }

        public JsonNode next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }
    }

    protected static class NoStringsIterator implements Iterator<String> {
        static final NoStringsIterator instance = new NoStringsIterator();

        private NoStringsIterator() {
        }

        public static NoStringsIterator instance() {
            return instance;
        }

        public boolean hasNext() {
            return false;
        }

        public String next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }
    }

    public abstract JsonToken asToken();

    public abstract ObjectNode findParent(String str);

    public abstract List<JsonNode> findParents(String str, List<JsonNode> list);

    public abstract JsonNode findValue(String str);

    public abstract List<JsonNode> findValues(String str, List<JsonNode> list);

    public abstract List<String> findValuesAsText(String str, List<String> list);

    public abstract JsonNode get(int i);

    public abstract JsonNode get(String str);

    public abstract ContainerNode removeAll();

    public abstract int size();

    protected ContainerNode(JsonNodeFactory nc) {
        this._nodeFactory = nc;
    }

    public boolean isContainerNode() {
        return true;
    }

    public String getValueAsText() {
        return null;
    }

    public String asText() {
        return "";
    }

    public final ArrayNode arrayNode() {
        return this._nodeFactory.arrayNode();
    }

    public final ObjectNode objectNode() {
        return this._nodeFactory.objectNode();
    }

    public final NullNode nullNode() {
        return this._nodeFactory.nullNode();
    }

    public final BooleanNode booleanNode(boolean v) {
        return this._nodeFactory.booleanNode(v);
    }

    public final NumericNode numberNode(byte v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(short v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(int v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(long v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(float v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(double v) {
        return this._nodeFactory.numberNode(v);
    }

    public final NumericNode numberNode(BigDecimal v) {
        return this._nodeFactory.numberNode(v);
    }

    public final TextNode textNode(String text) {
        return this._nodeFactory.textNode(text);
    }

    public final BinaryNode binaryNode(byte[] data) {
        return this._nodeFactory.binaryNode(data);
    }

    public final BinaryNode binaryNode(byte[] data, int offset, int length) {
        return this._nodeFactory.binaryNode(data, offset, length);
    }

    public final POJONode POJONode(Object pojo) {
        return this._nodeFactory.POJONode(pojo);
    }
}
