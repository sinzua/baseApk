package com.lidroid.xutils.view.annotation.event;

import android.widget.SeekBar.OnSeekBarChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnSeekBarChangeListener", listenerType = OnSeekBarChangeListener.class, methodName = "onProgressChanged")
public @interface OnProgressChanged {
    int[] parentId() default {0};

    int[] value();
}
