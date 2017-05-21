package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.dynamic.zzc.zza;

public final class zzh extends zza {
    private Fragment zzalg;

    private zzh(Fragment fragment) {
        this.zzalg = fragment;
    }

    public static zzh zza(Fragment fragment) {
        return fragment != null ? new zzh(fragment) : null;
    }

    public Bundle getArguments() {
        return this.zzalg.getArguments();
    }

    public int getId() {
        return this.zzalg.getId();
    }

    public boolean getRetainInstance() {
        return this.zzalg.getRetainInstance();
    }

    public String getTag() {
        return this.zzalg.getTag();
    }

    public int getTargetRequestCode() {
        return this.zzalg.getTargetRequestCode();
    }

    public boolean getUserVisibleHint() {
        return this.zzalg.getUserVisibleHint();
    }

    public zzd getView() {
        return zze.zzC(this.zzalg.getView());
    }

    public boolean isAdded() {
        return this.zzalg.isAdded();
    }

    public boolean isDetached() {
        return this.zzalg.isDetached();
    }

    public boolean isHidden() {
        return this.zzalg.isHidden();
    }

    public boolean isInLayout() {
        return this.zzalg.isInLayout();
    }

    public boolean isRemoving() {
        return this.zzalg.isRemoving();
    }

    public boolean isResumed() {
        return this.zzalg.isResumed();
    }

    public boolean isVisible() {
        return this.zzalg.isVisible();
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        this.zzalg.setHasOptionsMenu(hasMenu);
    }

    public void setMenuVisibility(boolean menuVisible) {
        this.zzalg.setMenuVisibility(menuVisible);
    }

    public void setRetainInstance(boolean retain) {
        this.zzalg.setRetainInstance(retain);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.zzalg.setUserVisibleHint(isVisibleToUser);
    }

    public void startActivity(Intent intent) {
        this.zzalg.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.zzalg.startActivityForResult(intent, requestCode);
    }

    public void zzn(zzd com_google_android_gms_dynamic_zzd) {
        this.zzalg.registerForContextMenu((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public void zzo(zzd com_google_android_gms_dynamic_zzd) {
        this.zzalg.unregisterForContextMenu((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public zzd zztV() {
        return zze.zzC(this.zzalg.getActivity());
    }

    public zzc zztW() {
        return zza(this.zzalg.getParentFragment());
    }

    public zzd zztX() {
        return zze.zzC(this.zzalg.getResources());
    }

    public zzc zztY() {
        return zza(this.zzalg.getTargetFragment());
    }
}
