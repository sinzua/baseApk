package com.parse;

import java.io.IOException;

interface ParseNetworkInterceptor {

    public interface Chain {
        ParseHttpRequest getRequest();

        ParseHttpResponse proceed(ParseHttpRequest parseHttpRequest) throws IOException;
    }

    ParseHttpResponse intercept(Chain chain) throws IOException;
}
