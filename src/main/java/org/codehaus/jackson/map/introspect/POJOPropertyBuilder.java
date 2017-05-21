package org.codehaus.jackson.map.introspect;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import org.codehaus.jackson.map.BeanPropertyDefinition;

public class POJOPropertyBuilder extends BeanPropertyDefinition implements Comparable<POJOPropertyBuilder> {
    protected Node<AnnotatedParameter> _ctorParameters;
    protected Node<AnnotatedField> _fields;
    protected Node<AnnotatedMethod> _getters;
    protected final String _internalName;
    protected final String _name;
    protected Node<AnnotatedMethod> _setters;

    private static final class Node<T> {
        public final String explicitName;
        public final boolean isMarkedIgnored;
        public final boolean isVisible;
        public final Node<T> next;
        public final T value;

        public Node(T v, Node<T> n, String explName, boolean visible, boolean ignored) {
            this.value = v;
            this.next = n;
            if (explName == null) {
                this.explicitName = null;
            } else {
                if (explName.length() == 0) {
                    explName = null;
                }
                this.explicitName = explName;
            }
            this.isVisible = visible;
            this.isMarkedIgnored = ignored;
        }

        public Node<T> withValue(T newValue) {
            if (newValue == this.value) {
                return this;
            }
            return new Node(newValue, this.next, this.explicitName, this.isVisible, this.isMarkedIgnored);
        }

        public Node<T> withNext(Node<T> newNext) {
            if (newNext == this.next) {
                return this;
            }
            return new Node(this.value, newNext, this.explicitName, this.isVisible, this.isMarkedIgnored);
        }

        public Node<T> withoutIgnored() {
            if (this.isMarkedIgnored) {
                return this.next == null ? null : this.next.withoutIgnored();
            } else {
                if (this.next != null) {
                    Node<T> newNext = this.next.withoutIgnored();
                    if (newNext != this.next) {
                        return withNext(newNext);
                    }
                }
                return this;
            }
        }

        public Node<T> withoutNonVisible() {
            Node<T> newNext = this.next == null ? null : this.next.withoutNonVisible();
            return this.isVisible ? withNext(newNext) : newNext;
        }

        private Node<T> append(Node<T> appendable) {
            if (this.next == null) {
                return withNext(appendable);
            }
            return withNext(this.next.append(appendable));
        }

        public Node<T> trimByVisibility() {
            if (this.next == null) {
                return this;
            }
            Node<T> newNext = this.next.trimByVisibility();
            if (this.explicitName != null) {
                if (newNext.explicitName == null) {
                    return withNext(null);
                }
                return withNext(newNext);
            } else if (newNext.explicitName != null) {
                return newNext;
            } else {
                if (this.isVisible == newNext.isVisible) {
                    return withNext(newNext);
                }
                return this.isVisible ? withNext(null) : newNext;
            }
        }

        public String toString() {
            String msg = this.value.toString() + "[visible=" + this.isVisible + RequestParameters.RIGHT_BRACKETS;
            if (this.next != null) {
                return msg + ", " + this.next.toString();
            }
            return msg;
        }
    }

    public POJOPropertyBuilder(String internalName) {
        this._internalName = internalName;
        this._name = internalName;
    }

    public POJOPropertyBuilder(POJOPropertyBuilder src, String newName) {
        this._internalName = src._internalName;
        this._name = newName;
        this._fields = src._fields;
        this._ctorParameters = src._ctorParameters;
        this._getters = src._getters;
        this._setters = src._setters;
    }

    public POJOPropertyBuilder withName(String newName) {
        return new POJOPropertyBuilder(this, newName);
    }

    public int compareTo(POJOPropertyBuilder other) {
        if (this._ctorParameters != null) {
            if (other._ctorParameters == null) {
                return -1;
            }
        } else if (other._ctorParameters != null) {
            return 1;
        }
        return getName().compareTo(other.getName());
    }

    public String getName() {
        return this._name;
    }

    public String getInternalName() {
        return this._internalName;
    }

    public boolean isExplicitlyIncluded() {
        return anyExplicitNames();
    }

    public boolean hasGetter() {
        return this._getters != null;
    }

    public boolean hasSetter() {
        return this._setters != null;
    }

    public boolean hasField() {
        return this._fields != null;
    }

    public boolean hasConstructorParameter() {
        return this._ctorParameters != null;
    }

    public AnnotatedMember getAccessor() {
        AnnotatedMember m = getGetter();
        if (m == null) {
            return getField();
        }
        return m;
    }

    public AnnotatedMember getMutator() {
        AnnotatedMember m = getConstructorParameter();
        if (m != null) {
            return m;
        }
        m = getSetter();
        if (m == null) {
            return getField();
        }
        return m;
    }

    public boolean couldSerialize() {
        return (this._getters == null && this._fields == null) ? false : true;
    }

    public AnnotatedMethod getGetter() {
        if (this._getters == null) {
            return null;
        }
        AnnotatedMethod getter = this._getters.value;
        Node<AnnotatedMethod> next = this._getters.next;
        while (next != null) {
            AnnotatedMethod nextGetter = next.value;
            Class<?> getterClass = getter.getDeclaringClass();
            Class<?> nextClass = nextGetter.getDeclaringClass();
            if (getterClass != nextClass) {
                if (getterClass.isAssignableFrom(nextClass)) {
                    getter = nextGetter;
                } else if (nextClass.isAssignableFrom(getterClass)) {
                }
                next = next.next;
            }
            throw new IllegalArgumentException("Conflicting getter definitions for property \"" + getName() + "\": " + getter.getFullName() + " vs " + nextGetter.getFullName());
        }
        return getter;
    }

    public AnnotatedMethod getSetter() {
        if (this._setters == null) {
            return null;
        }
        AnnotatedMethod setter = this._setters.value;
        Node<AnnotatedMethod> next = this._setters.next;
        while (next != null) {
            AnnotatedMethod nextSetter = next.value;
            Class<?> setterClass = setter.getDeclaringClass();
            Class<?> nextClass = nextSetter.getDeclaringClass();
            if (setterClass != nextClass) {
                if (setterClass.isAssignableFrom(nextClass)) {
                    setter = nextSetter;
                } else if (nextClass.isAssignableFrom(setterClass)) {
                }
                next = next.next;
            }
            throw new IllegalArgumentException("Conflicting setter definitions for property \"" + getName() + "\": " + setter.getFullName() + " vs " + nextSetter.getFullName());
        }
        return setter;
    }

    public AnnotatedField getField() {
        if (this._fields == null) {
            return null;
        }
        AnnotatedField field = this._fields.value;
        Node<AnnotatedField> next = this._fields.next;
        while (next != null) {
            AnnotatedField nextField = next.value;
            Class<?> fieldClass = field.getDeclaringClass();
            Class<?> nextClass = nextField.getDeclaringClass();
            if (fieldClass != nextClass) {
                if (fieldClass.isAssignableFrom(nextClass)) {
                    field = nextField;
                } else if (nextClass.isAssignableFrom(fieldClass)) {
                }
                next = next.next;
            }
            throw new IllegalArgumentException("Multiple fields representing property \"" + getName() + "\": " + field.getFullName() + " vs " + nextField.getFullName());
        }
        return field;
    }

    public AnnotatedParameter getConstructorParameter() {
        if (this._ctorParameters == null) {
            return null;
        }
        Node<AnnotatedParameter> curr = this._ctorParameters;
        while (!(((AnnotatedParameter) curr.value).getOwner() instanceof AnnotatedConstructor)) {
            curr = curr.next;
            if (curr == null) {
                return (AnnotatedParameter) this._ctorParameters.value;
            }
        }
        return (AnnotatedParameter) curr.value;
    }

    public void addField(AnnotatedField a, String ename, boolean visible, boolean ignored) {
        this._fields = new Node(a, this._fields, ename, visible, ignored);
    }

    public void addCtor(AnnotatedParameter a, String ename, boolean visible, boolean ignored) {
        this._ctorParameters = new Node(a, this._ctorParameters, ename, visible, ignored);
    }

    public void addGetter(AnnotatedMethod a, String ename, boolean visible, boolean ignored) {
        this._getters = new Node(a, this._getters, ename, visible, ignored);
    }

    public void addSetter(AnnotatedMethod a, String ename, boolean visible, boolean ignored) {
        this._setters = new Node(a, this._setters, ename, visible, ignored);
    }

    public void addAll(POJOPropertyBuilder src) {
        this._fields = merge(this._fields, src._fields);
        this._ctorParameters = merge(this._ctorParameters, src._ctorParameters);
        this._getters = merge(this._getters, src._getters);
        this._setters = merge(this._setters, src._setters);
    }

    private static <T> Node<T> merge(Node<T> chain1, Node<T> chain2) {
        if (chain1 == null) {
            return chain2;
        }
        if (chain2 == null) {
            return chain1;
        }
        return chain1.append(chain2);
    }

    public void removeIgnored() {
        this._fields = _removeIgnored(this._fields);
        this._getters = _removeIgnored(this._getters);
        this._setters = _removeIgnored(this._setters);
        this._ctorParameters = _removeIgnored(this._ctorParameters);
    }

    public void removeNonVisible() {
        this._getters = _removeNonVisible(this._getters);
        this._ctorParameters = _removeNonVisible(this._ctorParameters);
        if (this._getters == null) {
            this._fields = _removeNonVisible(this._fields);
            this._setters = _removeNonVisible(this._setters);
        }
    }

    public void trimByVisibility() {
        this._fields = _trimByVisibility(this._fields);
        this._getters = _trimByVisibility(this._getters);
        this._setters = _trimByVisibility(this._setters);
        this._ctorParameters = _trimByVisibility(this._ctorParameters);
    }

    public void mergeAnnotations(boolean forSerialization) {
        if (forSerialization) {
            if (this._getters != null) {
                this._getters = this._getters.withValue(((AnnotatedMethod) this._getters.value).withAnnotations(_mergeAnnotations(0, this._getters, this._fields, this._ctorParameters, this._setters)));
            } else if (this._fields != null) {
                this._fields = this._fields.withValue(((AnnotatedField) this._fields.value).withAnnotations(_mergeAnnotations(0, this._fields, this._ctorParameters, this._setters)));
            }
        } else if (this._ctorParameters != null) {
            this._ctorParameters = this._ctorParameters.withValue(((AnnotatedParameter) this._ctorParameters.value).withAnnotations(_mergeAnnotations(0, this._ctorParameters, this._setters, this._fields, this._getters)));
        } else if (this._setters != null) {
            this._setters = this._setters.withValue(((AnnotatedMethod) this._setters.value).withAnnotations(_mergeAnnotations(0, this._setters, this._fields, this._getters)));
        } else if (this._fields != null) {
            this._fields = this._fields.withValue(((AnnotatedField) this._fields.value).withAnnotations(_mergeAnnotations(0, this._fields, this._getters)));
        }
    }

    private AnnotationMap _mergeAnnotations(int index, Node<? extends AnnotatedMember>... nodes) {
        AnnotationMap ann = ((AnnotatedMember) nodes[index].value).getAllAnnotations();
        for (index++; index < nodes.length; index++) {
            if (nodes[index] != null) {
                return AnnotationMap.merge(ann, _mergeAnnotations(index, nodes));
            }
        }
        return ann;
    }

    private <T> Node<T> _removeIgnored(Node<T> node) {
        return node == null ? node : node.withoutIgnored();
    }

    private <T> Node<T> _removeNonVisible(Node<T> node) {
        return node == null ? node : node.withoutNonVisible();
    }

    private <T> Node<T> _trimByVisibility(Node<T> node) {
        return node == null ? node : node.trimByVisibility();
    }

    public boolean anyExplicitNames() {
        return _anyExplicitNames(this._fields) || _anyExplicitNames(this._getters) || _anyExplicitNames(this._setters) || _anyExplicitNames(this._ctorParameters);
    }

    private <T> boolean _anyExplicitNames(Node<T> n) {
        while (n != null) {
            if (n.explicitName != null && n.explicitName.length() > 0) {
                return true;
            }
            n = n.next;
        }
        return false;
    }

    public boolean anyVisible() {
        return _anyVisible(this._fields) || _anyVisible(this._getters) || _anyVisible(this._setters) || _anyVisible(this._ctorParameters);
    }

    private <T> boolean _anyVisible(Node<T> n) {
        while (n != null) {
            if (n.isVisible) {
                return true;
            }
            n = n.next;
        }
        return false;
    }

    public boolean anyIgnorals() {
        return _anyIgnorals(this._fields) || _anyIgnorals(this._getters) || _anyIgnorals(this._setters) || _anyIgnorals(this._ctorParameters);
    }

    public boolean anyDeserializeIgnorals() {
        return _anyIgnorals(this._fields) || _anyIgnorals(this._setters) || _anyIgnorals(this._ctorParameters);
    }

    public boolean anySerializeIgnorals() {
        return _anyIgnorals(this._fields) || _anyIgnorals(this._getters);
    }

    private <T> boolean _anyIgnorals(Node<T> n) {
        while (n != null) {
            if (n.isMarkedIgnored) {
                return true;
            }
            n = n.next;
        }
        return false;
    }

    public String findNewName() {
        Node<? extends AnnotatedMember> renamed = findRenamed(this._ctorParameters, findRenamed(this._setters, findRenamed(this._getters, findRenamed(this._fields, null))));
        return renamed == null ? null : renamed.explicitName;
    }

    private Node<? extends AnnotatedMember> findRenamed(Node<? extends AnnotatedMember> node, Node<? extends AnnotatedMember> renamed) {
        while (node != null) {
            String explName = node.explicitName;
            if (!(explName == null || explName.equals(this._name))) {
                if (renamed == null) {
                    renamed = node;
                } else if (!explName.equals(renamed.explicitName)) {
                    throw new IllegalStateException("Conflicting property name definitions: '" + renamed.explicitName + "' (for " + renamed.value + ") vs '" + node.explicitName + "' (for " + node.value + ")");
                }
            }
            node = node.next;
        }
        return renamed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Property '").append(this._name).append("'; ctors: ").append(this._ctorParameters).append(", field(s): ").append(this._fields).append(", getter(s): ").append(this._getters).append(", setter(s): ").append(this._setters);
        sb.append(RequestParameters.RIGHT_BRACKETS);
        return sb.toString();
    }
}
