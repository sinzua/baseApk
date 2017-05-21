package com.google.android.gms.internal;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class zzsp<M extends zzso<M>, T> {
    public final int tag;
    protected final int type;
    protected final Class<T> zzbuk;
    protected final boolean zzbul;

    private zzsp(int i, Class<T> cls, int i2, boolean z) {
        this.type = i;
        this.zzbuk = cls;
        this.tag = i2;
        this.zzbul = z;
    }

    private T zzK(List<zzsw> list) {
        int i;
        int i2 = 0;
        List arrayList = new ArrayList();
        for (i = 0; i < list.size(); i++) {
            zzsw com_google_android_gms_internal_zzsw = (zzsw) list.get(i);
            if (com_google_android_gms_internal_zzsw.zzbuv.length != 0) {
                zza(com_google_android_gms_internal_zzsw, arrayList);
            }
        }
        i = arrayList.size();
        if (i == 0) {
            return null;
        }
        T cast = this.zzbuk.cast(Array.newInstance(this.zzbuk.getComponentType(), i));
        while (i2 < i) {
            Array.set(cast, i2, arrayList.get(i2));
            i2++;
        }
        return cast;
    }

    private T zzL(List<zzsw> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.zzbuk.cast(zzP(zzsm.zzD(((zzsw) list.get(list.size() - 1)).zzbuv)));
    }

    public static <M extends zzso<M>, T extends zzsu> zzsp<M, T> zza(int i, Class<T> cls, long j) {
        return new zzsp(i, cls, (int) j, false);
    }

    final T zzJ(List<zzsw> list) {
        return list == null ? null : this.zzbul ? zzK(list) : zzL(list);
    }

    protected Object zzP(zzsm com_google_android_gms_internal_zzsm) {
        Class componentType = this.zzbul ? this.zzbuk.getComponentType() : this.zzbuk;
        try {
            zzsu com_google_android_gms_internal_zzsu;
            switch (this.type) {
                case 10:
                    com_google_android_gms_internal_zzsu = (zzsu) componentType.newInstance();
                    com_google_android_gms_internal_zzsm.zza(com_google_android_gms_internal_zzsu, zzsx.zzmJ(this.tag));
                    return com_google_android_gms_internal_zzsu;
                case 11:
                    com_google_android_gms_internal_zzsu = (zzsu) componentType.newInstance();
                    com_google_android_gms_internal_zzsm.zza(com_google_android_gms_internal_zzsu);
                    return com_google_android_gms_internal_zzsu;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e);
        } catch (Throwable e2) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e2);
        } catch (Throwable e22) {
            throw new IllegalArgumentException("Error reading extension field", e22);
        }
    }

    int zzY(Object obj) {
        return this.zzbul ? zzZ(obj) : zzaa(obj);
    }

    protected int zzZ(Object obj) {
        int i = 0;
        int length = Array.getLength(obj);
        for (int i2 = 0; i2 < length; i2++) {
            if (Array.get(obj, i2) != null) {
                i += zzaa(Array.get(obj, i2));
            }
        }
        return i;
    }

    protected void zza(zzsw com_google_android_gms_internal_zzsw, List<Object> list) {
        list.add(zzP(zzsm.zzD(com_google_android_gms_internal_zzsw.zzbuv)));
    }

    void zza(Object obj, zzsn com_google_android_gms_internal_zzsn) throws IOException {
        if (this.zzbul) {
            zzc(obj, com_google_android_gms_internal_zzsn);
        } else {
            zzb(obj, com_google_android_gms_internal_zzsn);
        }
    }

    protected int zzaa(Object obj) {
        int zzmJ = zzsx.zzmJ(this.tag);
        switch (this.type) {
            case 10:
                return zzsn.zzb(zzmJ, (zzsu) obj);
            case 11:
                return zzsn.zzc(zzmJ, (zzsu) obj);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }

    protected void zzb(Object obj, zzsn com_google_android_gms_internal_zzsn) {
        try {
            com_google_android_gms_internal_zzsn.zzmB(this.tag);
            switch (this.type) {
                case 10:
                    zzsu com_google_android_gms_internal_zzsu = (zzsu) obj;
                    int zzmJ = zzsx.zzmJ(this.tag);
                    com_google_android_gms_internal_zzsn.zzb(com_google_android_gms_internal_zzsu);
                    com_google_android_gms_internal_zzsn.zzE(zzmJ, 4);
                    return;
                case 11:
                    com_google_android_gms_internal_zzsn.zzc((zzsu) obj);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException(e);
    }

    protected void zzc(Object obj, zzsn com_google_android_gms_internal_zzsn) {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                zzb(obj2, com_google_android_gms_internal_zzsn);
            }
        }
    }
}
