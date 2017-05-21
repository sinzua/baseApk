package com.lidroid.xutils.view.annotation.event;

import android.widget.RadioGroup.OnCheckedChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnCheckedChangeListener", listenerType = OnCheckedChangeListener.class, methodName = "onCheckedChanged")
public @interface OnRadioGroupCheckedChange {
    int[] parentId() default {0};

    int[] value();
}
