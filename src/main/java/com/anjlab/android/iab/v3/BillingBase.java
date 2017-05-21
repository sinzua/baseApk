package com.anjlab.android.iab.v3;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.lang.ref.WeakReference;

class BillingBase {
    private WeakReference<Context> contextReference;

    public BillingBase(Context context) {
        this.contextReference = new WeakReference(context);
    }

    public Context getContext() {
        return (Context) this.contextReference.get();
    }

    protected String getPreferencesBaseKey() {
        return ((Context) this.contextReference.get()).getPackageName() + "_preferences";
    }

    private SharedPreferences getPreferences() {
        if (this.contextReference.get() != null) {
            return PreferenceManager.getDefaultSharedPreferences((Context) this.contextReference.get());
        }
        return null;
    }

    public void release() {
        if (this.contextReference != null) {
            this.contextReference.clear();
        }
    }

    protected boolean saveString(String key, String value) {
        SharedPreferences sp = getPreferences();
        if (sp == null) {
            return false;
        }
        Editor spe = sp.edit();
        spe.putString(key, value);
        spe.commit();
        return true;
    }

    protected String loadString(String key, String defValue) {
        SharedPreferences sp = getPreferences();
        if (sp != null) {
            return sp.getString(key, defValue);
        }
        return defValue;
    }

    protected boolean saveBoolean(String key, Boolean value) {
        SharedPreferences sp = getPreferences();
        if (sp == null) {
            return false;
        }
        Editor spe = sp.edit();
        spe.putBoolean(key, value.booleanValue());
        spe.commit();
        return true;
    }

    protected boolean loadBoolean(String key, boolean defValue) {
        SharedPreferences sp = getPreferences();
        if (sp != null) {
            return sp.getBoolean(key, defValue);
        }
        return defValue;
    }
}
