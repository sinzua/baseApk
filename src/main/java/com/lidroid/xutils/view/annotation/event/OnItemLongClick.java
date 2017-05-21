package com.lidroid.xutils.view.annotation.event;

import android.widget.AdapterView.OnItemLongClickListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemLongClickListener", listenerType = OnItemLongClickListener.class, methodName = "onItemLongClick")
public @interface OnItemLongClick {
    int[] parentId() default {0};

    int[] value();
}
