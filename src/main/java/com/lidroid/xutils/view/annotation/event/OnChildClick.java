package com.lidroid.xutils.view.annotation.event;

import android.widget.ExpandableListView.OnChildClickListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnChildClickListener", listenerType = OnChildClickListener.class, methodName = "onChildClick")
public @interface OnChildClick {
    int[] parentId() default {0};

    int[] value();
}
