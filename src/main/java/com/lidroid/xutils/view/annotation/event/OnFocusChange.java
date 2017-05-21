package com.lidroid.xutils.view.annotation.event;

import android.view.View.OnFocusChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnFocusChangeListener", listenerType = OnFocusChangeListener.class, methodName = "onFocusChange")
public @interface OnFocusChange {
    int[] parentId() default {0};

    int[] value();
}
