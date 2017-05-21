package com.ty.followboom.helpers;

import android.content.Context;

public class AppContext {
    public static final String KEY_FREE_LIKES = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApSY/i/aH9q5plTpc5ew8aBZ4uRkkqrDHHlyKR7pXs1Pdg16ndZp2kjBwH8M6yPJB82QcuZ5VNxtRh1R4Vo/eio9LCAOM4hjVZ0Ywj69pBf+2AOetCCRh9OgtD/KBK77Zw5OnYnuZaiYKAynDFfW2vWCNyaWYblUhg2wSaVpl/vfgyFHyfFcuptgBlr8OHh5/TyVcWwWnMvbR5tYyqEbboRyPyyJv5ySoDch1o8clT5l2xjJ9I8l+z9SkaCDUYhuFl4R/h8xV1afgcXqee0bBGDqa7QjtLiidfE74yBqN9MtDqP8FvRVY3oJO5UR+uAPAEuDOEOGwcvWpmLmE5eITcwIDAQAB";
    public static final String KEY_FREE_VIEW = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlPygYqRkJWWvBmok1x6ibg7Eq/F4hbxajXZ9JYYi8uTu3RxplyXRhnqsRGTvQ2TRHAHldmzaiGtvhX0rfHrqvKkMlS8b6kQWHDTw8gtLTUBk0/pjbKvR5zpwz0cV3sQ8SMOc89g+1inq8KdythNSYqjAxVab/pvKDrUGW+HtWQ29oZi+hUTuB811LIbrVMaHku6B6NmywIz1CZr83pZ2sWgzHssNbL91bmB4kJnh18GdySsr1hCNPSRmkHcJ/MF5pezUTcPSHUlAF151z5C7MSpVFJwNFAPjFB6VcUPPcjIpV+dR/DjOjxoUlf1/QfPnOJss/FJ5YrrROvXIwAoL+QIDAQAB";
    public static final String KEY_LIKE4LIKE = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAirdierDw7q7MuSlnMDyewmwYAFzFnCdO4dap0ckCLdY0myG5IRJlnOxOl+doEA1HiclhmMAZmjnzHDIKqgrk0U+WbbmfEz4piFHPCnLn711t6UltU3k4p63GJATUh8tkCTBdAL2lMUrm1OTa0lF8xt3DvEvxnBjJVkzrjcReQQ7iEvv22m0tLsfn3fv1MX4Gottv1A2KBSJfXBtfYbCTITq5zTfoodVBsl1pRgr4cKRUELzRhW0RjvclzLAuWQl10+JHx291KgpI0oZZzXC55hyidGEmwAKEV8F+hz9wHKIaRmyjnwZXSnf4VDQyqaSf3sTi2JKPy/gQ0hjFVt0ndQIDAQAB";
    public static final String KEY_REALFOLLOWERS = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArsKCUJNEe+VXNyP2ab/IClVdHK/QZcemFuKD5B14PNsngOZ6Q8YvQ6LYZpKOsPazNeVwwnzXik64F4QUiMqBkge1qbOu175ibuCL4tBG/CBbogqp3mxzKAP0GLy+9gOlVZhZD1wvaKtR40pFsP1Lmgil3y5umPLLNxvS2zmFATPAhupT1eWOK1RCwogPjYk6qyo7poyIIPqp/Sl4wZ7K4QiwzSnPu1EzRa+QrCFZxwYfyyrX3Hldu2Svxk++5Sk5bFwDJlB/loO/vnS6NM/QC5aJsZC915gFMSOcrAFroFD5SX8WSMt/3jviFLuzlRgf0JGL7iecUtd8R+0/bpf+aQIDAQAB";
    public static final String KEY_TRACKER = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6KuQTl3yGF0NGfhZSJHp5OnYaz+SEuJmXBHVq/oiR8hQAK59ku4AUyjz7YyF/k9JucjU7yIlzsedbmlnQZeLbVnEon1giiKboh2QorPyz/dvmchv2Lg48LsXEGHjPYM27ME+B5X94nLINmyxqpbIY6VIgcmxeGniRdDHUinpWzWBA4xU026Gx662uE30j/xYwSgIBVk/RX00SWzShAZAp4PogjGxeWWd1Oc54aZnXFE1xEavYmYmiLzZH4bWcdx81+4oUNxGeIcP/nnMCHuKNKxHMCLhRnBYnshUtyTFExpFmc3CzrEFqyGMT5Pq49DZQTe6h7ncowtQ9KR8nY/7EQIDAQAB";
    public static final String KEY_TRACKER_NEW = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzO8KbarrAc7BVonYGQ3/Hsdt3LgTGLPKDZWGy0v0UZ3lIEad+ZuSv/NF/pfF98wJ2wwdDZFcL2WoKBY5GjHxssD+F2r1ggD+7IQOnm9lugu0LDg55cMtOVdbFQLwsSVBk3VKfsGIp2zR3d+O6JtQ4W9H21svJ43cY+08n6ss0fk5r8pNXmPRMeXW9pBWRtOogICew8Z/jUJko1EKpv8n+JgfqpJkQULosPR12pO7Ct9CJVkEOer3vCP33BAYY9b247qqs74CocgrJl5KEemd5sZG0RcBKmWPs+2sRgtQOsYBnxK6jrBL7gCltgX2DcjggpP87WEImoK5pl+fSxQF7QIDAQAB";
    public static final String KEY_VIEW = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmF5+Cb/+slZQmMcR3o8ktRsv5F0xY8zrZT0BPjWo8X7I+lq4ZrsfmmwhlBTdL+jW8l6hbI/iCvxk5C1ZAstBl1ytTjTS26lW4PnvIjMNFkBB4nDGpc+KVgUcIsee1extwjd3ezNhy1orqBEixcaFH92T6hXMM1D8zqB25J2jAKqaRJ+NlJLmphNErMoS6vCDUJkC4flt4YcHU/KISfg48PGicHeXvsN34QxCkzzdcVOUvjh/bsWrmLh+M7S/zU6Gsa2w0zIIr3tSck525m6JV0SwPrFy5gsHqs6RDbVjWowIEJYp3PjnewpUDpzB/8ec36eC4/bHMi1Goc5Ealjq7QIDAQAB";
    private static AppContext instance;
    public final String LICENSE_KEY = KEY_VIEW;
    private Context context;
    private String downLoadUrl;
    private boolean hasNewVer;
    private boolean newUser;
    private String version;

    public static AppContext getSingleton() {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }
        return instance;
    }

    public boolean isNewUser() {
        return this.newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public boolean isHasNewVer() {
        return this.hasNewVer;
    }

    public void setHasNewVer(boolean hasNewVer) {
        this.hasNewVer = hasNewVer;
    }

    public String getDownLoadUrl() {
        return this.downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
