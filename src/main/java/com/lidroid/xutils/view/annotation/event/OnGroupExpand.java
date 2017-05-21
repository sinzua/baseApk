package com.lidroid.xutils.view.annotation.event;

import android.widget.ExpandableListView.OnGroupExpandListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnGroupExpandListener", listenerType = OnGroupExpandListener.class, methodName = "onGroupExpand")
public @interface OnGroupExpand {
    int[] parentId() default {0};

    int[] value();
}
