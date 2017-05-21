package com.lidroid.xutils.view.annotation.event;

import android.widget.ExpandableListView.OnGroupClickListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnGroupClickListener", listenerType = OnGroupClickListener.class, methodName = "onGroupClick")
public @interface OnGroupClick {
    int[] parentId() default {0};

    int[] value();
}
