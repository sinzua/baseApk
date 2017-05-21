package com.lidroid.xutils.view.annotation.event;

import android.widget.TabHost.OnTabChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnTabChangeListener", listenerType = OnTabChangeListener.class, methodName = "onTabChange")
public @interface OnTabChange {
    int[] parentId() default {0};

    int[] value();
}
