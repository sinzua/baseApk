package com.lidroid.xutils.task;

/* compiled from: PriorityObjectBlockingQueue */
class Node<T> {
    Node<T> next;
    private PriorityObject<?> value;
    private boolean valueAsT = false;

    Node(T value) {
        setValue(value);
    }

    public Priority getPriority() {
        return this.value.priority;
    }

    public T getValue() {
        if (this.value == null) {
            return null;
        }
        if (this.valueAsT) {
            return this.value;
        }
        return this.value.obj;
    }

    public void setValue(T value) {
        if (value == null) {
            this.value = null;
        } else if (value instanceof PriorityObject) {
            this.value = (PriorityObject) value;
            this.valueAsT = true;
        } else {
            this.value = new PriorityObject(Priority.DEFAULT, value);
        }
    }
}
