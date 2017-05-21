package com.lidroid.xutils.view.annotation.event;

import android.view.View.OnLongClickListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnLongClickListener", listenerType = OnLongClickListener.class, methodName = "onLongClick")
public @interface OnLongClick {
    int[] parentId() default {0};

    int[] value();
}
