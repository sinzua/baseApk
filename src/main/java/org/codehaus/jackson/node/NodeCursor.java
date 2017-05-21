package org.codehaus.jackson.node;

import java.util.Iterator;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;

abstract class NodeCursor extends JsonStreamContext {
    final NodeCursor _parent;

    protected static final class Array extends NodeCursor {
        Iterator<JsonNode> _contents;
        JsonNode _currentNode;

        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public Array(JsonNode n, NodeCursor p) {
            super(1, p);
            this._contents = n.getElements();
        }

        public String getCurrentName() {
            return null;
        }

        public JsonToken nextToken() {
            if (this._contents.hasNext()) {
                this._currentNode = (JsonNode) this._contents.next();
                return this._currentNode.asToken();
            }
            this._currentNode = null;
            return null;
        }

        public JsonToken nextValue() {
            return nextToken();
        }

        public JsonToken endToken() {
            return JsonToken.END_ARRAY;
        }

        public JsonNode currentNode() {
            return this._currentNode;
        }

        public boolean currentHasChildren() {
            return ((ContainerNode) currentNode()).size() > 0;
        }
    }

    protected static final class Object extends NodeCursor {
        Iterator<Entry<String, JsonNode>> _contents;
        Entry<String, JsonNode> _current;
        boolean _needEntry = true;

        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public Object(JsonNode n, NodeCursor p) {
            super(2, p);
            this._contents = ((ObjectNode) n).getFields();
        }

        public String getCurrentName() {
            return this._current == null ? null : (String) this._current.getKey();
        }

        public JsonToken nextToken() {
            if (!this._needEntry) {
                this._needEntry = true;
                return ((JsonNode) this._current.getValue()).asToken();
            } else if (this._contents.hasNext()) {
                this._needEntry = false;
                this._current = (Entry) this._contents.next();
                return JsonToken.FIELD_NAME;
            } else {
                this._current = null;
                return null;
            }
        }

        public JsonToken nextValue() {
            JsonToken t = nextToken();
            if (t == JsonToken.FIELD_NAME) {
                return nextToken();
            }
            return t;
        }

        public JsonToken endToken() {
            return JsonToken.END_OBJECT;
        }

        public JsonNode currentNode() {
            return this._current == null ? null : (JsonNode) this._current.getValue();
        }

        public boolean currentHasChildren() {
            return ((ContainerNode) currentNode()).size() > 0;
        }
    }

    protected static final class RootValue extends NodeCursor {
        protected boolean _done = false;
        JsonNode _node;

        public /* bridge */ /* synthetic */ JsonStreamContext getParent() {
            return super.getParent();
        }

        public RootValue(JsonNode n, NodeCursor p) {
            super(0, p);
            this._node = n;
        }

        public String getCurrentName() {
            return null;
        }

        public JsonToken nextToken() {
            if (this._done) {
                this._node = null;
                return null;
            }
            this._done = true;
            return this._node.asToken();
        }

        public JsonToken nextValue() {
            return nextToken();
        }

        public JsonToken endToken() {
            return null;
        }

        public JsonNode currentNode() {
            return this._node;
        }

        public boolean currentHasChildren() {
            return false;
        }
    }

    public abstract boolean currentHasChildren();

    public abstract JsonNode currentNode();

    public abstract JsonToken endToken();

    public abstract String getCurrentName();

    public abstract JsonToken nextToken();

    public abstract JsonToken nextValue();

    public NodeCursor(int contextType, NodeCursor p) {
        this._type = contextType;
        this._index = -1;
        this._parent = p;
    }

    public final NodeCursor getParent() {
        return this._parent;
    }

    public final NodeCursor iterateChildren() {
        JsonNode n = currentNode();
        if (n == null) {
            throw new IllegalStateException("No current node");
        } else if (n.isArray()) {
            return new Array(n, this);
        } else {
            if (n.isObject()) {
                return new Object(n, this);
            }
            throw new IllegalStateException("Current node of type " + n.getClass().getName());
        }
    }
}
