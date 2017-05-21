package com.supersonic.mediationsdk.integration;

import android.util.Pair;
import java.util.ArrayList;
import java.util.List;

class AdapterObject {
    private static final String mAdapter = "Adapter";
    private static final String mAdapterbase = "com.supersonic.adapters.";
    private List<String> mActivities;
    private String mAdapterName;
    private List<String> mBroadcastReceivers;
    private ArrayList<Pair<String, String>> mExternalLibraries;
    private boolean mIsAdapter;
    private String mName;
    private List<String> mPermissions;
    private String mSdkName;

    AdapterObject(String name, List<String> activities, boolean isAdapter) {
        this.mName = name;
        this.mActivities = activities;
        this.mIsAdapter = isAdapter;
        if (isAdapter) {
            this.mAdapterName = mAdapterbase + name.toLowerCase() + "." + name + mAdapter;
        }
    }

    String getName() {
        return this.mName;
    }

    void setName(String name) {
        this.mName = name;
    }

    List<String> getPermissions() {
        return this.mPermissions;
    }

    void setPermissions(List<String> permissions) {
        this.mPermissions = permissions;
    }

    List<String> getActivities() {
        return this.mActivities;
    }

    void setActivities(List<String> activities) {
        this.mActivities = activities;
    }

    ArrayList<Pair<String, String>> getExternalLibraries() {
        return this.mExternalLibraries;
    }

    void setExternalLibraries(ArrayList<Pair<String, String>> externalLibraries) {
        this.mExternalLibraries = externalLibraries;
    }

    String getSdkName() {
        return this.mSdkName;
    }

    void setSdkName(String sdkName) {
        this.mSdkName = sdkName;
    }

    String getAdapterName() {
        return this.mAdapterName;
    }

    boolean isAdapter() {
        return this.mIsAdapter;
    }

    List<String> getBroadcastReceivers() {
        return this.mBroadcastReceivers;
    }

    void setBroadcastReceivers(List<String> broadcastReceivers) {
        this.mBroadcastReceivers = broadcastReceivers;
    }
}
