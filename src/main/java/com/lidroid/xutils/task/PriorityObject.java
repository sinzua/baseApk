package com.lidroid.xutils.task;

public class PriorityObject<E> {
    public final E obj;
    public final Priority priority;

    public PriorityObject(Priority priority, E obj) {
        if (priority == null) {
            priority = Priority.DEFAULT;
        }
        this.priority = priority;
        this.obj = obj;
    }
}
