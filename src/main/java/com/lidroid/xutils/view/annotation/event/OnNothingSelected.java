package com.lidroid.xutils.view.annotation.event;

import android.widget.AdapterView.OnItemSelectedListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemSelectedListener", listenerType = OnItemSelectedListener.class, methodName = "onNothingSelected")
public @interface OnNothingSelected {
    int[] parentId() default {0};

    int[] value();
}
