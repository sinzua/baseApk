package com.google.android.gms.ads.internal.request;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface zzj extends IInterface {

    public static abstract class zza extends Binder implements zzj {

        private static class zza implements zzj {
            private IBinder zzoz;

            zza(IBinder iBinder) {
                this.zzoz = iBinder;
            }

            public IBinder asBinder() {
                return this.zzoz;
            }

            public void zza(AdRequestInfoParcel adRequestInfoParcel, zzk com_google_android_gms_ads_internal_request_zzk) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.request.IAdRequestService");
                    if (adRequestInfoParcel != null) {
                        obtain.writeInt(1);
                        adRequestInfoParcel.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_ads_internal_request_zzk != null ? com_google_android_gms_ads_internal_request_zzk.asBinder() : null);
                    this.zzoz.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public AdResponseParcel zzd(AdRequestInfoParcel adRequestInfoParcel) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.request.IAdRequestService");
                    if (adRequestInfoParcel != null) {
                        obtain.writeInt(1);
                        adRequestInfoParcel.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    AdResponseParcel zzj = obtain2.readInt() != 0 ? AdResponseParcel.CREATOR.zzj(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return zzj;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.ads.internal.request.IAdRequestService");
        }

        public static zzj zzX(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.request.IAdRequestService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzj)) ? new zza(iBinder) : (zzj) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            AdRequestInfoParcel adRequestInfoParcel = null;
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.ads.internal.request.IAdRequestService");
                    if (data.readInt() != 0) {
                        adRequestInfoParcel = AdRequestInfoParcel.CREATOR.zzi(data);
                    }
                    AdResponseParcel zzd = zzd(adRequestInfoParcel);
                    reply.writeNoException();
                    if (zzd != null) {
                        reply.writeInt(1);
                        zzd.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.ads.internal.request.IAdRequestService");
                    if (data.readInt() != 0) {
                        adRequestInfoParcel = AdRequestInfoParcel.CREATOR.zzi(data);
                    }
                    zza(adRequestInfoParcel, com.google.android.gms.ads.internal.request.zzk.zza.zzY(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.request.IAdRequestService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zza(AdRequestInfoParcel adRequestInfoParcel, zzk com_google_android_gms_ads_internal_request_zzk) throws RemoteException;

    AdResponseParcel zzd(AdRequestInfoParcel adRequestInfoParcel) throws RemoteException;
}
