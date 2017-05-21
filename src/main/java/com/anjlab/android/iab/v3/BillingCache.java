package com.anjlab.android.iab.v3;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

class BillingCache extends BillingBase {
    private static final String ENTRY_DELIMITER = "#####";
    private static final String LINE_DELIMITER = ">>>>>";
    private static final String VERSION_KEY = ".version";
    private String cacheKey;
    private HashMap<String, PurchaseInfo> data = new HashMap();
    private String version;

    public BillingCache(Context context, String key) {
        super(context);
        this.cacheKey = key;
        load();
    }

    private String getPreferencesCacheKey() {
        return getPreferencesBaseKey() + this.cacheKey;
    }

    private String getPreferencesVersionKey() {
        return getPreferencesCacheKey() + VERSION_KEY;
    }

    private void load() {
        for (String entry : loadString(getPreferencesCacheKey(), "").split(Pattern.quote(ENTRY_DELIMITER))) {
            if (!TextUtils.isEmpty(entry)) {
                String[] parts = entry.split(Pattern.quote(LINE_DELIMITER));
                if (parts.length > 2) {
                    this.data.put(parts[0], new PurchaseInfo(parts[1], parts[2]));
                } else if (parts.length > 1) {
                    this.data.put(parts[0], new PurchaseInfo(parts[1], null));
                }
            }
        }
        this.version = getCurrentVersion();
    }

    private void flush() {
        ArrayList<String> output = new ArrayList();
        for (String productId : this.data.keySet()) {
            PurchaseInfo info = (PurchaseInfo) this.data.get(productId);
            output.add(productId + LINE_DELIMITER + info.responseData + LINE_DELIMITER + info.signature);
        }
        saveString(getPreferencesCacheKey(), TextUtils.join(ENTRY_DELIMITER, output));
        this.version = Long.toString(new Date().getTime());
        saveString(getPreferencesVersionKey(), this.version);
    }

    public boolean includesProduct(String productId) {
        reloadDataIfNeeded();
        return this.data.containsKey(productId);
    }

    public PurchaseInfo getDetails(String productId) {
        reloadDataIfNeeded();
        return this.data.containsKey(productId) ? (PurchaseInfo) this.data.get(productId) : null;
    }

    public void put(String productId, String details, String signature) {
        reloadDataIfNeeded();
        if (!this.data.containsKey(productId)) {
            this.data.put(productId, new PurchaseInfo(details, signature));
            flush();
        }
    }

    public void remove(String productId) {
        reloadDataIfNeeded();
        if (this.data.containsKey(productId)) {
            this.data.remove(productId);
            flush();
        }
    }

    public void clear() {
        reloadDataIfNeeded();
        this.data.clear();
        flush();
    }

    private String getCurrentVersion() {
        return loadString(getPreferencesVersionKey(), "0");
    }

    private void reloadDataIfNeeded() {
        if (!this.version.equalsIgnoreCase(getCurrentVersion())) {
            this.data.clear();
            load();
        }
    }

    public List<String> getContents() {
        return new ArrayList(this.data.keySet());
    }

    public String toString() {
        return TextUtils.join(", ", this.data.keySet());
    }
}
