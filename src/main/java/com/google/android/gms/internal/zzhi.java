package com.google.android.gms.internal;

import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@zzhb
class zzhi {
    private final String zzEY;
    private String zzF;
    private int zzGu;
    private final List<String> zzJI;
    private final List<String> zzJJ;
    private final String zzJK;
    private final String zzJL;
    private final String zzJM;
    private final String zzJN;
    private final boolean zzJO;
    private final boolean zzJP;

    public zzhi(int i, Map<String, String> map) {
        this.zzF = (String) map.get(ParametersKeys.URL);
        this.zzJL = (String) map.get("base_uri");
        this.zzJM = (String) map.get("post_parameters");
        this.zzJO = parseBoolean((String) map.get("drt_include"));
        this.zzJP = parseBoolean((String) map.get("pan_include"));
        this.zzJK = (String) map.get("activation_overlay_url");
        this.zzJJ = zzav((String) map.get("check_packages"));
        this.zzEY = (String) map.get("request_id");
        this.zzJN = (String) map.get("type");
        this.zzJI = zzav((String) map.get("errors"));
        this.zzGu = i;
    }

    private static boolean parseBoolean(String bool) {
        return bool != null && (bool.equals("1") || bool.equals("true"));
    }

    private List<String> zzav(String str) {
        return str == null ? null : Arrays.asList(str.split(","));
    }

    public int getErrorCode() {
        return this.zzGu;
    }

    public String getRequestId() {
        return this.zzEY;
    }

    public String getType() {
        return this.zzJN;
    }

    public String getUrl() {
        return this.zzF;
    }

    public void setUrl(String url) {
        this.zzF = url;
    }

    public List<String> zzgE() {
        return this.zzJI;
    }

    public String zzgF() {
        return this.zzJM;
    }

    public boolean zzgG() {
        return this.zzJO;
    }

    public boolean zzgH() {
        return this.zzJP;
    }
}
