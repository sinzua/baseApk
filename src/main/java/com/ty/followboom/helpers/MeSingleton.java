package com.ty.followboom.helpers;

import com.ty.entities.PostItem;
import com.ty.http.responses.TimelineResponse;
import java.util.List;

public class MeSingleton {
    private static MeSingleton sInstance;
    private TimelineResponse mTimelineResponse;
    private List<PostItem> myPost;

    public static MeSingleton getSingleton() {
        if (sInstance == null) {
            synchronized (MeSingleton.class) {
                if (sInstance == null) {
                    sInstance = new MeSingleton();
                }
            }
        }
        return sInstance;
    }

    public List<PostItem> getMyPost() {
        return this.myPost;
    }

    public void setMyPost(List<PostItem> myPost) {
        this.myPost = myPost;
    }

    public TimelineResponse getTimelineResponse() {
        return this.mTimelineResponse;
    }

    public void setTimelineResponse(TimelineResponse mTimelineResponse) {
        this.mTimelineResponse = mTimelineResponse;
    }
}
