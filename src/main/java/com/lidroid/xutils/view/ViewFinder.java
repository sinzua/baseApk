package com.lidroid.xutils.view;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.view.View;

public class ViewFinder {
    private Activity activity;
    private PreferenceActivity preferenceActivity;
    private PreferenceGroup preferenceGroup;
    private View view;

    public ViewFinder(View view) {
        this.view = view;
    }

    public ViewFinder(Activity activity) {
        this.activity = activity;
    }

    public ViewFinder(PreferenceGroup preferenceGroup) {
        this.preferenceGroup = preferenceGroup;
    }

    public ViewFinder(PreferenceActivity preferenceActivity) {
        this.preferenceActivity = preferenceActivity;
        this.activity = preferenceActivity;
    }

    public View findViewById(int id) {
        return this.activity == null ? this.view.findViewById(id) : this.activity.findViewById(id);
    }

    public View findViewByInfo(ViewInjectInfo info) {
        return findViewById(((Integer) info.value).intValue(), info.parentId);
    }

    public View findViewById(int id, int pid) {
        View pView = null;
        if (pid > 0) {
            pView = findViewById(pid);
        }
        if (pView != null) {
            return pView.findViewById(id);
        }
        return findViewById(id);
    }

    public Preference findPreference(CharSequence key) {
        return this.preferenceGroup == null ? this.preferenceActivity.findPreference(key) : this.preferenceGroup.findPreference(key);
    }

    public Context getContext() {
        if (this.view != null) {
            return this.view.getContext();
        }
        if (this.activity != null) {
            return this.activity;
        }
        if (this.preferenceActivity != null) {
            return this.preferenceActivity;
        }
        return null;
    }
}
