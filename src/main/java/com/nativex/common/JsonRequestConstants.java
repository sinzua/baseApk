package com.nativex.common;

public class JsonRequestConstants {

    public interface ActionTaken {
        public static final String ACTION_ID = "ActionId";
        public static final String IS_HACKED = "IsHacked";
        public static final String UDIDS = "UDIDs";
    }

    public interface AndroidMarketInputs {
        public static final String CAMPAIGN = "utm_campaign";
        public static final String CONTENT = "utm_content";
        public static final String MEDIUM = "utm_medium";
        public static final String SOURCE = "utm_source";
        public static final String TERM = "utm_term";
    }

    public interface Balance {
        public static final String AMOUNT = "Amount";
        public static final String DISPLAY_NAME = "DisplayName";
        public static final String EXTERNAL_CURRENCY_ID = "ExternalCurrencyId";
        public static final String PAYOUT_ID = "PayoutId";
    }

    public interface CreateSession {
        public static final String APP_ID = "AppId";
        public static final String APP_LANGUAGE_CODE = "AppLanguageCode";
        public static final String BUILD_TYPE = "BuildType";
        public static final String CACHING_FREQUENCY = "CachingFrequency";
        public static final String DEVICE_GENERATION_INFO = "DeviceGenerationInfo";
        public static final String DEVICE_ID = "ApiDeviceId";
        public static final String DEVICE_LANGUAGE_CODE = "DeviceLanguageCode";
        public static final String FREE_SPACE_MIN_MEGABYTES = "FreeSpaceMin";
        public static final String IS_ADVERTISER_TRACKING_ENABLED = "IsAdvertiserTrackingEnabled";
        public static final String IS_BACKUP_ADS_ENABLED = "IsBackupAdsEnabled";
        public static final String IS_CURRENCY_ENABLED = "IsCurrencyEnabled";
        public static final String IS_FIRST_RUN = "IsFirstRun";
        public static final String IS_HACKED = "IsHacked";
        public static final String IS_OFFERWALL_ENABLED = "IsAfppOfferwallEnabled";
        public static final String IS_OFFER_CACHE_AVAILABLE = "IsOfferCacheAvailable";
        public static final String IS_ON_WIFI = "IsOnWifi";
        public static final String IS_USING_SDK = "IsUsingSdk";
        public static final String LOG = "Log";
        public static final String MESSAGES = "Messages";
        public static final String OS_VERSION = "OSVersion";
        public static final String PREVIOUS_SESSION_ID = "PreviousSessionId";
        public static final String PUBLISHER_USER_ID = "PublisherUserId";
        public static final String REPLACE_WEBVIEW_USERAGENT = "ReplaceWebViewUserAgent";
        public static final String SDK_VERSION = "PublisherSDKVersion";
        public static final String SESSION = "Session";
        public static final String UDIDS = "UDIDs";
        public static final String VIOLATIONS = "Violations";
        public static final String WEB_VIEW_USER_AGENT = "WebViewUserAgent";
    }

    public interface GetDeviceBalance {
        public static final String BALANCES = "Balances";
        public static final String LOG = "Log";
        public static final String MESSAGES = "Messages";
        public static final String SESSION = "Session";
        public static final String VIOLATIONS = "Violations";
    }

    public interface GetOfferCache {
        public static final String CACHE_SIZE_MAX = "CacheSizeMax";
        public static final String CDN = "CDN";
        public static final String CODE = "Code";
        public static final String EXPIRATION_DATE = "ExpirationDateUTC";
        public static final String EXT = "Ext";
        public static final String FILES = "Files";
        public static final String FILE_SIZE = "FileSize";
        public static final String FREE_SPACE_MIN = "FreeSpaceMin";
        public static final String GLOBAL_FILES = "GlobalFiles";
        public static final String MD5 = "MD5";
        public static final String OFFERS = "Offers";
        public static final String OFFER_ID = "OfferId";
        public static final String RELATIVE_URL = "RelativeUrl";
        public static final String URL = "Url";
    }

    public interface InAppBilling {
        public static final String ADVERTISER_SESSION = "AdvertiserSessionId";
        public static final String COST_PER_ITEM = "CostPerItem";
        public static final String CURRENCY_LOCALE = "CurrencyLocale";
        public static final String IS_SUCCESSFUL = "IsSuccessful";
        public static final String LOG = "Log";
        public static final String MESSAGES = "Messages";
        public static final String PRODUCT_TITLE = "ProductTitle";
        public static final String PUBLISHER_SESSION = "PublisherSessionId";
        public static final String QUANTITY = "Quantity";
        public static final String STORE_PRODUCT_ID = "StoreProductId";
        public static final String STORE_TRANSACTION_ID = "StoreTransactionId";
        public static final String STORE_TRANSACTION_TIME = "StoreTransactionTimeUtc";
        public static final String VIOLATIONS = "Violations";
    }

    public interface Log {
        public static final String DISPLAY_NAME = "DisplayName";
        public static final String DISPLAY_TEXT = "DisplayText";
        public static final String REFERENCE_NAME = "ReferenceName";
    }

    public interface Message {
        public static final String DISPLAY_NAME = "DisplayName";
        public static final String DISPLAY_TEXT = "DisplayText";
        public static final String REFERENCE_NAME = "ReferenceName";
    }

    public interface Receipt {
        public static final String AMOUNT = "Amount";
        public static final String DISPLAY_NAME = "DisplayName";
        public static final String EXTERNAL_CURRENCY_ID = "ExternalCurrencyId";
        public static final String ID = "Id";
        public static final String PAYOUT_ID = "PayoutId";
        public static final String RECEIPT_ID = "ReceiptId";
    }

    public interface RedeemBalance {
        public static final String LOG = "Log";
        public static final String MESSAGES = "Messages";
        public static final String PAYOUT_IDS = "PayoutIds";
        public static final String RECEIPTS = "Receipts";
        public static final String SESSION = "Session";
        public static final String VIOLATIONS = "Violations";
    }

    public interface RichMedia {
        public static final String AD_BEHAVIOR = "AdBehaviors";
        public static final String AD_INFO = "AdInfo";
        public static final String CDN = "CDNs";
        public static final String CONTENT = "Content";
        public static final String OFFER_IDS = "OfferIds";
        public static final String VIEW_TIMEOUT = "ViewTimeout";
        public static final String WILL_PLAY_AUDIO = "WillPlayAudio";
    }

    public interface Session {
        public static final String SESSION_ID = "SessionId";
    }

    public interface UDIDs {
        public static final String ANDROID_ADVERTISER_ID = "12";
        public static final String ANDROID_DEVICE_ID = "3";
        public static final String ANDROID_ID = "4";
        public static final String TYPE = "Type";
        public static final String VALUE = "Value";
    }

    public interface URLS {
        public static final String ACTION_TAKEN = "PublicServices/MobileTrackingApiRestV1.svc/ActionTaken/Put";
        public static final String CREATE_SESSION = "PublicServices/MobileTrackingApiRestV1.svc/CreateSessionV2";
        public static final String GET_DEVICE_BALANCE = "PublicServices/AfppApiRestV1.svc/GetAvailableDeviceBalanceV2";
        public static final String GET_OFFER_CACHE_V2 = "PublicServices/ServiceApiRestV1.svc/Offer/CacheV2";
        public static final String IN_APP_PURCHASE = "PublicServices/MobileTrackingApiRestV1.svc/InAppPurchase/Put";
        public static final String MRAID_ADS = "Richmedia/";
        public static final String REDEEM_BALANCE = "PublicServices/AfppApiRestV1.svc/RedeemDeviceBalanceV2";
    }

    public interface UnityRewardData {
        public static final String BALANCES = "balances";
        public static final String MESSAGES = "messages";
        public static final String RECEIPTS = "receipts";
    }

    public interface UniversalQueryParameters {
        public static final String APP_ID = "appId";
    }

    public interface VideoPlayerOptions {
        public static final String ALLOW_MUTE = "allowMute";
        public static final String ALLOW_SKIP_AFTER = "allowSkipAfterMilliseconds";
        public static final String ALLOW_SKIP_AFTER_VIDEO_STRUCK = "allowSkipAfterVideoStuckForMilliseconds";
        public static final String AUTO_PLAY = "autoPlay";
        public static final String CONTROLS_ALPHA = "controlsAlpha";
        public static final String CONTROL_DISTANCE_FROM_EDGES_IN_DIP = "controlsDistanceFromScreenEdgeInDensityIndependentPixels";
        public static final String COUNT_DOWN_AFTER = "countdownAfterMilliseconds";
        public static final String COUNT_DOWN_MESSAGE_FORMAT = "countdownMessageFormat";
        public static final String COUNT_DOWN_TEXT_COLOR = "countdownMessageTextColor";
        public static final String ERROR_MESSAGE_TOAST = "errorMessageToast";
        public static final String ICON_MAX_DIMENSION_IN_DIP = "controlIconMaxDimensionInDensityIndependentPixels";
        public static final String SPECIAL_SKIP_COUNTDOWN_MESSAGE_FORMAT = "specialSkipCountdownMessageFormat";
        public static final String START_MUTED = "startMuted";
    }

    public interface Violation {
        public static final String ENTITY = "Entity";
        public static final String MESSAGE = "Message";
        public static final String TYPE = "Type";
    }

    private JsonRequestConstants() {
    }
}
