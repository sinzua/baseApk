package com.lidroid.xutils.view.annotation.event;

import android.widget.AbsListView.OnScrollListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnScrollListener", listenerType = OnScrollListener.class, methodName = "onScrollStateChanged")
public @interface OnScrollStateChanged {
    int[] parentId() default {0};

    int[] value();
}
