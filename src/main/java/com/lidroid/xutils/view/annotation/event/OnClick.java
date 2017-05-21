package com.lidroid.xutils.view.annotation.event;

import android.view.View.OnClickListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnClickListener", listenerType = OnClickListener.class, methodName = "onClick")
public @interface OnClick {
    int[] parentId() default {0};

    int[] value();
}
