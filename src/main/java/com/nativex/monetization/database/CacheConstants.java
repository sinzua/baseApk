package com.nativex.monetization.database;

public class CacheConstants {
    public static final String CACHE_ERROR_TABLE = "cache_error";
    public static final String CACHE_TABLE = "cache_files";
    public static final String CACHE_UTIL_TABLE = "cache_utils";
    public static final String DOWNLOADED_FILES_TABLE = "downloaded_files";
    public static final String DOWNLOAD_MAP_TABLE = "download_map";
    public static final String FILE_STATUS_TABLE = "file_status";

    public static class CACHE {
        public static final String CACHE_ID = "cache_id";
        public static final String CDN = "CDN";
        public static final String EXPIRATION_TIME = "ExpirationDateUTC";
        public static final String EXT = "Ext";
        public static final String FILE_SIZE = "FileSize";
        public static final String FILE_STATUS = "FileStatus";
        public static final String MD5 = "MD5";
        public static final String OFFER_ID = "OfferId";
        public static final String RELATIVE_URL = "RelativeUrl";
    }

    public static class CACHE_UTILS {
        public static final String CACHE_UTIL = "cache_util";
        public static final String CACHE_UTILS_ID = "cache_utils_id";
        public static final String CACHE_UTIL_VALUE_INTEGER = "cache_util_value_int";
        public static final String FREE_SPACE_MIN = "free_space_min";
    }

    public static class DOWNLOAD_IDS {
        public static final String CACHE_FILE = "cachefile";
        public static final String DOWNLOAD_ID = "download_id";
    }
}
