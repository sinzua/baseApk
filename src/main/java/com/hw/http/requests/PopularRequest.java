package com.hw.http.requests;

import com.hw.http.RequestBuilder;

public class PopularRequest extends RequestBuilder {
    public String getPath() {
        return "https://i.instagram.com/api/v1/feed/popular/";
    }
}
