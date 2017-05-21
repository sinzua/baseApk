package com.lidroid.xutils.view.annotation.event;

import android.widget.AdapterView.OnItemClickListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemClickListener", listenerType = OnItemClickListener.class, methodName = "onItemClick")
public @interface OnItemClick {
    int[] parentId() default {0};

    int[] value();
}
