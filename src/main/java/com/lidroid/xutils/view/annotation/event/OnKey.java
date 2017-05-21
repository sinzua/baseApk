package com.lidroid.xutils.view.annotation.event;

import android.view.View.OnKeyListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnKeyListener", listenerType = OnKeyListener.class, methodName = "onKey")
public @interface OnKey {
    int[] parentId() default {0};

    int[] value();
}
