package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.zzr;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@zzhb
public final class zzbt {
    public static final zzbp<String> zzvA = zzbp.zza(0, "gads:sdk_core_experiment_id");
    public static final zzbp<String> zzvB = zzbp.zza(0, "gads:sdk_core_location", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/sdk-core-v40.html");
    public static final zzbp<Boolean> zzvC = zzbp.zza(0, "gads:request_builder:singleton_webview", Boolean.valueOf(false));
    public static final zzbp<String> zzvD = zzbp.zza(0, "gads:request_builder:singleton_webview_experiment_id");
    public static final zzbp<Boolean> zzvE = zzbp.zza(0, "gads:sdk_use_dynamic_module", Boolean.valueOf(false));
    public static final zzbp<String> zzvF = zzbp.zza(0, "gads:sdk_use_dynamic_module_experiment_id");
    public static final zzbp<Boolean> zzvG = zzbp.zza(0, "gads:sdk_crash_report_enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzvH = zzbp.zza(0, "gads:sdk_crash_report_full_stacktrace", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzvI = zzbp.zza(0, "gads:block_autoclicks", Boolean.valueOf(false));
    public static final zzbp<String> zzvJ = zzbp.zza(0, "gads:block_autoclicks_experiment_id");
    public static final zzbp<String> zzvK = zzbp.zzb(0, "gads:prefetch:experiment_id");
    public static final zzbp<String> zzvL = zzbp.zza(0, "gads:spam_app_context:experiment_id");
    public static final zzbp<Boolean> zzvM = zzbp.zza(0, "gads:spam_app_context:enabled", Boolean.valueOf(false));
    public static final zzbp<String> zzvN = zzbp.zza(0, "gads:video_stream_cache:experiment_id");
    public static final zzbp<Integer> zzvO = zzbp.zza(0, "gads:video_stream_cache:limit_count", 5);
    public static final zzbp<Integer> zzvP = zzbp.zza(0, "gads:video_stream_cache:limit_space", 8388608);
    public static final zzbp<Integer> zzvQ = zzbp.zza(0, "gads:video_stream_exo_cache:buffer_size", 8388608);
    public static final zzbp<Long> zzvR = zzbp.zza(0, "gads:video_stream_cache:limit_time_sec", 300);
    public static final zzbp<Long> zzvS = zzbp.zza(0, "gads:video_stream_cache:notify_interval_millis", 1000);
    public static final zzbp<Integer> zzvT = zzbp.zza(0, "gads:video_stream_cache:connect_timeout_millis", (int) ControllerParameters.LOAD_RUNTIME);
    public static final zzbp<Boolean> zzvU = zzbp.zza(0, "gads:video:metric_reporting_enabled", Boolean.valueOf(false));
    public static final zzbp<String> zzvV = zzbp.zza(0, "gads:video:metric_frame_hash_times", "");
    public static final zzbp<Long> zzvW = zzbp.zza(0, "gads:video:metric_frame_hash_time_leniency", 500);
    public static final zzbp<String> zzvX = zzbp.zzb(0, "gads:spam_ad_id_decorator:experiment_id");
    public static final zzbp<Boolean> zzvY = zzbp.zza(0, "gads:spam_ad_id_decorator:enabled", Boolean.valueOf(false));
    public static final zzbp<String> zzvZ = zzbp.zzb(0, "gads:looper_for_gms_client:experiment_id");
    public static final zzbp<Integer> zzwA = zzbp.zza(0, "gads:webview_cache_version", 0);
    public static final zzbp<String> zzwB = zzbp.zzb(0, "gads:pan:experiment_id");
    public static final zzbp<String> zzwC = zzbp.zza(0, "gads:native:engine_url", "//googleads.g.doubleclick.net/mads/static/mad/sdk/native/native_ads.html");
    public static final zzbp<Boolean> zzwD = zzbp.zza(0, "gads:ad_manager_creator:enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwE = zzbp.zza(1, "gads:interstitial_ad_pool:enabled", Boolean.valueOf(false));
    public static final zzbp<String> zzwF = zzbp.zza(1, "gads:interstitial_ad_pool:schema", "customTargeting");
    public static final zzbp<Integer> zzwG = zzbp.zza(1, "gads:interstitial_ad_pool:max_pools", 3);
    public static final zzbp<Integer> zzwH = zzbp.zza(1, "gads:interstitial_ad_pool:max_pool_depth", 2);
    public static final zzbp<Integer> zzwI = zzbp.zza(1, "gads:interstitial_ad_pool:time_limit_sec", 1200);
    public static final zzbp<String> zzwJ = zzbp.zza(1, "gads:interstitial_ad_pool:experiment_id");
    public static final zzbp<Boolean> zzwK = zzbp.zza(0, "gads:log:verbose_enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwL = zzbp.zza(0, "gads:device_info_caching:enabled", Boolean.valueOf(true));
    public static final zzbp<Long> zzwM = zzbp.zza(0, "gads:device_info_caching_expiry_ms:expiry", 300000);
    public static final zzbp<Boolean> zzwN = zzbp.zza(0, "gads:gen204_signals:enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwO = zzbp.zza(0, "gads:webview:error_reporting_enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwP = zzbp.zza(0, "gads:adid_reporting:enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwQ = zzbp.zza(0, "gads:ad_settings_page_reporting:enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwR = zzbp.zza(0, "gads:adid_info_gmscore_upgrade_reporting:enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwS = zzbp.zza(0, "gads:request_pkg:enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwT = zzbp.zza(0, "gads:gmsg:disable_back_button:enabled", Boolean.valueOf(false));
    public static final zzbp<Long> zzwU = zzbp.zza(0, "gads:network:cache_prediction_duration_s", 300);
    public static final zzbp<Boolean> zzwV = zzbp.zza(0, "gads:mediation:dynamite_first:admobadapter", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwW = zzbp.zza(0, "gads:mediation:dynamite_first:adurladapter", Boolean.valueOf(true));
    public static final zzbp<Long> zzwX = zzbp.zza(0, "gads:ad_loader:timeout_ms", 60000);
    public static final zzbp<Long> zzwY = zzbp.zza(0, "gads:rendering:timeout_ms", 60000);
    public static final zzbp<Boolean> zzwZ = zzbp.zza(0, "gads:adshield:enable_adshield_instrumentation", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwa = zzbp.zza(0, "gads:looper_for_gms_client:enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwb = zzbp.zza(0, "gads:sw_ad_request_service:enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwc = zzbp.zza(0, "gads:sw_dynamite:enabled", Boolean.valueOf(true));
    public static final zzbp<String> zzwd = zzbp.zza(0, "gad:mraid:url_banner", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/mraid/v2/mraid_app_banner.js");
    public static final zzbp<String> zzwe = zzbp.zza(0, "gad:mraid:url_expanded_banner", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/mraid/v2/mraid_app_expanded_banner.js");
    public static final zzbp<String> zzwf = zzbp.zza(0, "gad:mraid:url_interstitial", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/mraid/v2/mraid_app_interstitial.js");
    public static final zzbp<Boolean> zzwg = zzbp.zza(0, "gads:enabled_sdk_csi", Boolean.valueOf(false));
    public static final zzbp<String> zzwh = zzbp.zza(0, "gads:sdk_csi_server", "https://csi.gstatic.com/csi");
    public static final zzbp<Boolean> zzwi = zzbp.zza(0, "gads:sdk_csi_write_to_file", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzwj = zzbp.zza(0, "gads:enable_content_fetching", Boolean.valueOf(true));
    public static final zzbp<Integer> zzwk = zzbp.zza(0, "gads:content_length_weight", 1);
    public static final zzbp<Integer> zzwl = zzbp.zza(0, "gads:content_age_weight", 1);
    public static final zzbp<Integer> zzwm = zzbp.zza(0, "gads:min_content_len", 11);
    public static final zzbp<Integer> zzwn = zzbp.zza(0, "gads:fingerprint_number", 10);
    public static final zzbp<Integer> zzwo = zzbp.zza(0, "gads:sleep_sec", 10);
    public static final zzbp<Boolean> zzwp = zzbp.zza(0, "gad:app_index_enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwq = zzbp.zza(0, "gads:app_index:without_content_info_present:enabled", Boolean.valueOf(true));
    public static final zzbp<Long> zzwr = zzbp.zza(0, "gads:app_index:timeout_ms", 1000);
    public static final zzbp<String> zzws = zzbp.zza(0, "gads:app_index:experiment_id");
    public static final zzbp<String> zzwt = zzbp.zza(0, "gads:kitkat_interstitial_workaround:experiment_id");
    public static final zzbp<Boolean> zzwu = zzbp.zza(0, "gads:kitkat_interstitial_workaround:enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwv = zzbp.zza(0, "gads:interstitial_follow_url", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzww = zzbp.zza(0, "gads:interstitial_follow_url:register_click", Boolean.valueOf(true));
    public static final zzbp<String> zzwx = zzbp.zza(0, "gads:interstitial_follow_url:experiment_id");
    public static final zzbp<Boolean> zzwy = zzbp.zza(0, "gads:analytics_enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzwz = zzbp.zza(0, "gads:ad_key_enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzxa = zzbp.zza(0, "gass:enabled", Boolean.valueOf(false));
    public static final zzbp<Boolean> zzxb = zzbp.zza(0, "gass:enable_int_signal", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzxc = zzbp.zza(0, "gads:adid_notification:first_party_check:enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzxd = zzbp.zza(0, "gads:edu_device_helper:enabled", Boolean.valueOf(true));
    public static final zzbp<Boolean> zzxe = zzbp.zza(0, "gads:support_screen_shot", Boolean.valueOf(true));
    public static final zzbp<Long> zzxf = zzbp.zza(0, "gads:js_flags:update_interval", TimeUnit.HOURS.toMillis(12));
    public static final zzbp<Boolean> zzxg = zzbp.zza(0, "gads:custom_render:ping_on_ad_rendered", Boolean.valueOf(false));

    public static void initialize(final Context context) {
        zzjb.zzb(new Callable<Void>() {
            public /* synthetic */ Object call() throws Exception {
                return zzdt();
            }

            public Void zzdt() {
                zzr.zzbL().initialize(context);
                return null;
            }
        });
    }

    public static List<String> zzdr() {
        return zzr.zzbK().zzdr();
    }

    public static List<String> zzds() {
        return zzr.zzbK().zzds();
    }
}
