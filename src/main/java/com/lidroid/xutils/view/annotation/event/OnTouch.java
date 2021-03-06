package com.lidroid.xutils.view.annotation.event;

import android.view.View.OnTouchListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnTouchListener", listenerType = OnTouchListener.class, methodName = "onTouch")
public @interface OnTouch {
    int[] parentId() default {0};

    int[] value();
}
