package com.lidroid.xutils.view.annotation.event;

import android.preference.Preference.OnPreferenceChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnPreferenceChangeListener", listenerType = OnPreferenceChangeListener.class, methodName = "onPreferenceChange")
public @interface OnPreferenceChange {
    String[] value();
}
