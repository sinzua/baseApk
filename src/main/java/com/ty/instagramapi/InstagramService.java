package com.ty.instagramapi;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ty.entities.UserInfo;
import com.ty.http.HttpSingleton;
import com.ty.http.JsonCallback;
import com.ty.http.RequestCallback;
import com.ty.http.requests.FollowRequest;
import com.ty.http.requests.FollowersRequest;
import com.ty.http.requests.FollowingRequest;
import com.ty.http.requests.LikeRequest;
import com.ty.http.requests.LoginRequest;
import com.ty.http.requests.PopularRequest;
import com.ty.http.requests.ShowManyRequest;
import com.ty.http.requests.TagSearchRequest;
import com.ty.http.requests.TaglineRequest;
import com.ty.http.requests.TimelineRequest;
import com.ty.http.requests.UserLikedRequest;
import com.ty.http.requests.UserSearchRequest;
import com.ty.http.requests.UserfeedRequest;
import com.ty.http.requests.UserinfoRequest;
import com.ty.http.responses.BasicResponse;
import com.ty.http.responses.FollowResponse;
import com.ty.http.responses.ShowManyResponse;
import com.ty.http.responses.TagSearchResponse;
import com.ty.http.responses.TaglineResponse;
import com.ty.http.responses.TimelineResponse;
import com.ty.http.responses.UserSearchResponse;
import com.ty.http.responses.UserinfoResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class InstagramService {
    private static InstagramService sInstance = null;
    private UserInfo mUserInfo = new UserInfo();

    public static InstagramService getInstance() {
        if (sInstance == null) {
            sInstance = new InstagramService();
        }
        return sInstance;
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public void login(String username, String password, final RequestCallback<Response> loginCallback) {
        this.mUserInfo = new UserInfo();
        this.mUserInfo.setUserName(username);
        this.mUserInfo.setPassword(password);
        this.mUserInfo.setUuid(UUID.randomUUID().toString());
        HttpSingleton.newCall(new LoginRequest(this.mUserInfo)).enqueue(new Callback() {
            public void onResponse(Response response) throws IOException {
                loginCallback.onResponse(response);
            }

            public void onFailure(Request request, IOException e) {
                loginCallback.onFailure(e);
            }
        });
    }

    public void userFeed(String userid, RequestCallback<TimelineResponse> userfeedCallback) {
        userFeed(userid, "", userfeedCallback);
    }

    public void userFeed(String userid, String maxId, final RequestCallback<TimelineResponse> userfeedCallback) {
        Call timelineCall = HttpSingleton.newCall(new UserfeedRequest(userid, maxId));
        timelineCall.enqueue(new JsonCallback<TimelineResponse>(timelineCall, TimelineResponse.class) {
            public void onResponse(TimelineResponse timelineResponse) {
                userfeedCallback.onResponse(timelineResponse);
            }

            public void onFailure(Exception e) {
                userfeedCallback.onFailure(e);
            }
        });
    }

    public void userTimeline(RequestCallback<TimelineResponse> timelineCallback) {
        userTimeline("", timelineCallback);
    }

    public void userTimeline(String maxId, final RequestCallback<TimelineResponse> timelineCallback) {
        Call timelineCall = HttpSingleton.newCall(new TimelineRequest(maxId));
        timelineCall.enqueue(new JsonCallback<TimelineResponse>(timelineCall, TimelineResponse.class) {
            public void onResponse(TimelineResponse timelineResponse) {
                timelineCallback.onResponse(timelineResponse);
            }

            public void onFailure(Exception e) {
                timelineCallback.onFailure(e);
            }
        });
    }

    public void popular(final RequestCallback<TimelineResponse> timelineCallback) {
        Call popularCall = HttpSingleton.newCall(new PopularRequest());
        popularCall.enqueue(new JsonCallback<TimelineResponse>(popularCall, TimelineResponse.class) {
            public void onResponse(TimelineResponse timelineResponse) {
                timelineCallback.onResponse(timelineResponse);
            }

            public void onFailure(Exception e) {
                timelineCallback.onFailure(e);
            }
        });
    }

    public void getUserDetail(String userId, final RequestCallback<UserinfoResponse> userInfoCallback) {
        Call userinfoCall = HttpSingleton.newCall(new UserinfoRequest(userId));
        userinfoCall.enqueue(new JsonCallback<UserinfoResponse>(userinfoCall, UserinfoResponse.class) {
            public void onResponse(UserinfoResponse userinfoResponse) {
                userInfoCallback.onResponse(userinfoResponse);
            }

            public void onFailure(Exception e) {
                userInfoCallback.onFailure(e);
            }
        });
    }

    public void followers(String userId, RequestCallback<FollowResponse> followersCallback) {
        followers(userId, "", followersCallback);
    }

    public void followers(String userId, String maxId, final RequestCallback<FollowResponse> followersCallback) {
        Call followersCall = HttpSingleton.newCall(new FollowersRequest(userId, maxId));
        followersCall.enqueue(new JsonCallback<FollowResponse>(followersCall, FollowResponse.class) {
            public void onResponse(FollowResponse followResponse) {
                followersCallback.onResponse(followResponse);
            }

            public void onFailure(Exception e) {
                followersCallback.onFailure(e);
            }
        });
    }

    public void following(String userId, RequestCallback<FollowResponse> followingCallback) {
        following(userId, "", followingCallback);
    }

    public void following(String userId, String maxId, final RequestCallback<FollowResponse> followingCallback) {
        Call followingCall = HttpSingleton.newCall(new FollowingRequest(userId, maxId));
        followingCall.enqueue(new JsonCallback<FollowResponse>(followingCall, FollowResponse.class) {
            public void onResponse(FollowResponse followResponse) {
                followingCallback.onResponse(followResponse);
            }

            public void onFailure(Exception e) {
                followingCallback.onFailure(e);
            }
        });
    }

    public void userSearch(String username, final RequestCallback<UserSearchResponse> userSearchCallback) {
        Call userSearchCall = HttpSingleton.newCall(new UserSearchRequest(username));
        userSearchCall.enqueue(new JsonCallback<UserSearchResponse>(userSearchCall, UserSearchResponse.class) {
            public void onResponse(UserSearchResponse userSearchResponse) {
                userSearchCallback.onResponse(userSearchResponse);
            }

            public void onFailure(Exception e) {
                userSearchCallback.onFailure(e);
            }
        });
    }

    public void tagSearch(String tagName, final RequestCallback<TagSearchResponse> tagSearchCallback) {
        Call tagSearchCall = HttpSingleton.newCall(new TagSearchRequest(tagName));
        tagSearchCall.enqueue(new JsonCallback<TagSearchResponse>(tagSearchCall, TagSearchResponse.class) {
            public void onResponse(TagSearchResponse tagSearchResponse) {
                tagSearchCallback.onResponse(tagSearchResponse);
            }

            public void onFailure(Exception e) {
                tagSearchCallback.onFailure(e);
            }
        });
    }

    public void tagLine(String tagName, RequestCallback<TaglineResponse> taglineCallback) {
        tagLine(tagName, "", taglineCallback);
    }

    public void tagLine(String tagName, String maxId, final RequestCallback<TaglineResponse> taglineCallback) {
        Call taglineCall = HttpSingleton.newCall(new TaglineRequest(tagName, maxId));
        taglineCall.enqueue(new JsonCallback<TaglineResponse>(taglineCall, TaglineResponse.class) {
            public void onResponse(TaglineResponse taglineResponse) {
                taglineCallback.onResponse(taglineResponse);
            }

            public void onFailure(Exception e) {
                taglineCallback.onFailure(e);
            }
        });
    }

    public void userLiked(RequestCallback<TimelineResponse> userLikedCallback) {
        userLiked("", userLikedCallback);
    }

    public void userLiked(String maxId, final RequestCallback<TimelineResponse> userLikedCallback) {
        Call userLikedCall = HttpSingleton.newCall(new UserLikedRequest(maxId));
        userLikedCall.enqueue(new JsonCallback<TimelineResponse>(userLikedCall, TimelineResponse.class) {
            public void onResponse(TimelineResponse timelineResponse) {
                userLikedCallback.onResponse(timelineResponse);
            }

            public void onFailure(Exception e) {
                userLikedCallback.onFailure(e);
            }
        });
    }

    public void like(String postId, String targetUserId, final RequestCallback<BasicResponse> likeCallback) {
        Call likeCall = HttpSingleton.newCall(new LikeRequest(this.mUserInfo, postId, targetUserId));
        likeCall.enqueue(new JsonCallback<BasicResponse>(likeCall, BasicResponse.class) {
            public void onResponse(BasicResponse likeResponse) {
                likeCallback.onResponse(likeResponse);
            }

            public void onFailure(Exception e) {
                likeCallback.onFailure(e);
            }
        });
    }

    public void follow(String targetUserId, final RequestCallback<BasicResponse> followCallback) {
        Call followCall = HttpSingleton.newCall(new FollowRequest(this.mUserInfo, targetUserId));
        followCall.enqueue(new JsonCallback<BasicResponse>(followCall, BasicResponse.class) {
            public void onResponse(BasicResponse likeResponse) {
                followCallback.onResponse(likeResponse);
            }

            public void onFailure(Exception e) {
                followCallback.onFailure(e);
            }
        });
    }

    public void showMany(ArrayList<Long> userIds, final RequestCallback<ShowManyResponse> showManyCallback) {
        Call showManyCall = HttpSingleton.newCall(new ShowManyRequest(userIds));
        showManyCall.enqueue(new JsonCallback<ShowManyResponse>(showManyCall, ShowManyResponse.class) {
            public void onResponse(ShowManyResponse showManyResponse) {
                showManyCallback.onResponse(showManyResponse);
            }

            public void onFailure(Exception e) {
                showManyCallback.onFailure(e);
            }
        });
    }
}
