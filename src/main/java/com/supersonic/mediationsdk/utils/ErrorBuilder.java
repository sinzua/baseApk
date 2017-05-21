package com.supersonic.mediationsdk.utils;

import com.supersonic.mediationsdk.logger.SupersonicError;

public class ErrorBuilder {
    public static SupersonicError buildNoConfigurationAvailableError() {
        return new SupersonicError(501, "Mediation - Unable to retrieve configurations from Supersonic server");
    }

    public static SupersonicError buildUsingCachedConfigurationError(String appKey, String userId) {
        return new SupersonicError(SupersonicError.ERROR_CODE_USING_CACHED_CONFIGURATION, "Mediation - Unable to retrieve configurations from Supersonic server, using cached configurations with appKey:" + appKey + " and userId:" + userId);
    }

    public static SupersonicError buildAppKeyNotSetError() {
        return new SupersonicError(SupersonicError.ERROR_CODE_APP_KEY_NOT_SET, "Mediation - App key wasn't set");
    }

    public static SupersonicError buildAppKeyIncorrectError() {
        return new SupersonicError(SupersonicError.ERROR_CODE_APP_KEY_INCORRECT, "Mediation - App key incorrect");
    }

    public static SupersonicError buildKeyNotSetForProviderError(String key, String provider) {
        if (SupersonicUtils.isEmpty(key) || SupersonicUtils.isEmpty(provider)) {
            return getGenericErrorForMissingParams();
        }
        return new SupersonicError(SupersonicError.ERROR_CODE_KEY_NOT_SET_FOR_PROVIDER, "Mediation - " + key + " is not set for " + provider);
    }

    public static SupersonicError buildInvalidKeyValueError(String key, String provider, String optionalReason) {
        if (SupersonicUtils.isEmpty(key) || SupersonicUtils.isEmpty(provider)) {
            return getGenericErrorForMissingParams();
        }
        return new SupersonicError(SupersonicError.ERROR_CODE_INVALID_KEY_VALUE, "Mediation - " + key + " value is not valid for " + provider + (!SupersonicUtils.isEmpty(optionalReason) ? " - " + optionalReason : ""));
    }

    public static SupersonicError buildUnsupportedSdkVersion(String version, String provider) {
        if (SupersonicUtils.isEmpty(provider)) {
            return getGenericErrorForMissingParams();
        }
        if (version == null) {
            version = "";
        }
        return new SupersonicError(SupersonicError.ERROR_CODE_UNSUPPORTED_SDK_VERSION, "Mediation - Unsupported SDK version " + version + " for " + provider);
    }

    public static SupersonicError buildAdapterInitFailedError(String adapterDescription) {
        if (SupersonicUtils.isEmpty(adapterDescription)) {
            return getGenericErrorForMissingParams();
        }
        return new SupersonicError(SupersonicError.ERROR_CODE_ADAPTER_INIT_FAILED, "Init failed - " + adapterDescription);
    }

    public static SupersonicError buildShowVideoFailedError(String adapterDescription) {
        if (SupersonicUtils.isEmpty(adapterDescription)) {
            return getGenericErrorForMissingParams();
        }
        return new SupersonicError(SupersonicError.ERROR_CODE_SHOW_VIDEO_FAILED, "Show video failed - " + adapterDescription);
    }

    public static SupersonicError buildGenericError(String errorMsg) {
        if (SupersonicUtils.isEmpty(errorMsg)) {
            errorMsg = "An error occurred";
        }
        return new SupersonicError(SupersonicError.ERROR_CODE_GENERIC, errorMsg);
    }

    private static SupersonicError getGenericErrorForMissingParams() {
        return buildGenericError("Mediation - wrong configuration");
    }
}
