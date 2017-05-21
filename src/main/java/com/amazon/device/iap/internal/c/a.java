package com.amazon.device.iap.internal.c;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import com.amazon.device.iap.internal.util.d;
import com.amazon.device.iap.internal.util.e;
import com.amazon.device.iap.model.Receipt;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PendingReceiptsManager */
public class a {
    private static final String a = a.class.getSimpleName();
    private static final String b = (a.class.getName() + "_PREFS");
    private static final String c = (a.class.getName() + "_CLEANER_PREFS");
    private static int d = 604800000;
    private static final a e = new a();

    public void a(String str, String str2, String str3, String str4) {
        e.a(a, "enter saveReceipt for receipt [" + str4 + RequestParameters.RIGHT_BRACKETS);
        try {
            d.a(str2, "userId");
            d.a(str3, "receiptId");
            d.a(str4, "receiptString");
            Object b = com.amazon.device.iap.internal.d.d().b();
            d.a(b, "context");
            d dVar = new d(str2, str4, str, System.currentTimeMillis());
            Editor edit = b.getSharedPreferences(b, 0).edit();
            edit.putString(str3, dVar.d());
            edit.commit();
        } catch (Throwable th) {
            e.a(a, "error in saving pending receipt:" + str + "/" + str4 + ":" + th.getMessage());
        }
        e.a(a, "leaving saveReceipt for receipt id [" + str3 + RequestParameters.RIGHT_BRACKETS);
    }

    private void e() {
        e.a(a, "enter old receipts cleanup! ");
        final Object b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        a(System.currentTimeMillis());
        new Handler().post(new Runnable(this) {
            final /* synthetic */ a b;

            public void run() {
                try {
                    e.a(a.a, "perform house keeping! ");
                    SharedPreferences sharedPreferences = b.getSharedPreferences(a.b, 0);
                    for (String str : sharedPreferences.getAll().keySet()) {
                        if (System.currentTimeMillis() - d.a(sharedPreferences.getString(str, null)).c() > ((long) a.d)) {
                            e.a(a.a, "house keeping - try remove Receipt:" + str + " since it's too old");
                            this.b.a(str);
                        }
                    }
                } catch (e e) {
                    e.a(a.a, "house keeping - try remove Receipt:" + str + " since it's invalid ");
                    this.b.a(str);
                } catch (Throwable th) {
                    e.a(a.a, "Error in running cleaning job:" + th);
                }
            }
        });
    }

    public void a(String str) {
        e.a(a, "enter removeReceipt for receipt[" + str + RequestParameters.RIGHT_BRACKETS);
        Object b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        Editor edit = b.getSharedPreferences(b, 0).edit();
        edit.remove(str);
        edit.commit();
        e.a(a, "leave removeReceipt for receipt[" + str + RequestParameters.RIGHT_BRACKETS);
    }

    private long f() {
        Object b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        long currentTimeMillis = System.currentTimeMillis();
        long j = b.getSharedPreferences(c, 0).getLong("LAST_CLEANING_TIME", 0);
        if (j != 0) {
            return j;
        }
        a(currentTimeMillis);
        return currentTimeMillis;
    }

    private void a(long j) {
        Object b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        Editor edit = b.getSharedPreferences(c, 0).edit();
        edit.putLong("LAST_CLEANING_TIME", j);
        edit.commit();
    }

    public Set<Receipt> b(String str) {
        Object b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        e.a(a, "enter getLocalReceipts for user[" + str + RequestParameters.RIGHT_BRACKETS);
        Set<Receipt> hashSet = new HashSet();
        if (d.a(str)) {
            e.b(a, "empty UserId: " + str);
            throw new RuntimeException("Invalid UserId:" + str);
        }
        Map all = b.getSharedPreferences(b, 0).getAll();
        for (String str2 : all.keySet()) {
            String str3 = (String) all.get(str2);
            try {
                d a = d.a(str3);
                hashSet.add(com.amazon.device.iap.internal.util.a.a(new JSONObject(a.b()), str, a.a()));
            } catch (com.amazon.device.iap.internal.b.d e) {
                a(str2);
                e.b(a, "failed to verify signature:[" + str3 + RequestParameters.RIGHT_BRACKETS);
            } catch (JSONException e2) {
                a(str2);
                e.b(a, "failed to convert string to JSON object:[" + str3 + RequestParameters.RIGHT_BRACKETS);
            } catch (Throwable th) {
                e.b(a, "failed to load the receipt from SharedPreference:[" + str3 + RequestParameters.RIGHT_BRACKETS);
            }
        }
        e.a(a, "leaving getLocalReceipts for user[" + str + "], " + hashSet.size() + " local receipts found.");
        if (System.currentTimeMillis() - f() > ((long) d)) {
            e();
        }
        return hashSet;
    }

    public static a a() {
        return e;
    }

    public String c(String str) {
        String str2 = null;
        Object b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        if (d.a(str)) {
            e.b(a, "empty receiptId: " + str);
            throw new RuntimeException("Invalid ReceiptId:" + str);
        }
        String string = b.getSharedPreferences(b, 0).getString(str, str2);
        if (string != null) {
            try {
                str2 = d.a(string).a();
            } catch (e e) {
            }
        }
        return str2;
    }
}
