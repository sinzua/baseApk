package com.ty.followboom.models;

import com.squareup.okhttp.Response;
import com.ty.http.RequestCallback;
import com.ty.http.responses.TimelineResponse;
import com.ty.instagramapi.InstagramService;

public class InstagramApi {
    private static InstagramApi sInstagramApi;

    public static InstagramApi getSingleton() {
        if (sInstagramApi == null) {
            synchronized (InstagramApi.class) {
                if (sInstagramApi == null) {
                    sInstagramApi = new InstagramApi();
                }
            }
        }
        return sInstagramApi;
    }

    public void login(String username, String password, RequestCallback<Response> loginCallback) {
        InstagramService.getInstance().login(username, password, loginCallback);
    }

    public void userTimeline(RequestCallback<TimelineResponse> timelineCallback) {
        InstagramService.getInstance().userTimeline(timelineCallback);
    }

    public void popular(RequestCallback<TimelineResponse> popularCallback) {
        InstagramService.getInstance().popular(popularCallback);
    }
}
