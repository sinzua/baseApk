package com.lidroid.xutils.view.annotation.event;

import android.preference.Preference.OnPreferenceClickListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnPreferenceClickListener", listenerType = OnPreferenceClickListener.class, methodName = "onPreferenceClick")
public @interface OnPreferenceClick {
    String[] value();
}
