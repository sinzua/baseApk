package com.hw.hwapi;

import com.hw.entities.PostItem;
import com.hw.entities.UserInfo;
import com.hw.http.HttpSingleton;
import com.hw.http.JsonCallback;
import com.hw.http.RequestCallback;
import com.hw.http.requests.FollowRequest;
import com.hw.http.requests.FollowersRequest;
import com.hw.http.requests.FollowingRequest;
import com.hw.http.requests.GetPostRequest;
import com.hw.http.requests.LikeRequest;
import com.hw.http.requests.LoginRequest;
import com.hw.http.requests.MediaConfigRequest;
import com.hw.http.requests.PopularRequest;
import com.hw.http.requests.ProfileRequest;
import com.hw.http.requests.ShowManyRequest;
import com.hw.http.requests.SignupRequest;
import com.hw.http.requests.TagSearchRequest;
import com.hw.http.requests.TaglineRequest;
import com.hw.http.requests.TimelineRequest;
import com.hw.http.requests.UploadRequest;
import com.hw.http.requests.UserLikedRequest;
import com.hw.http.requests.UserSearchRequest;
import com.hw.http.requests.UserfeedRequest;
import com.hw.http.requests.UserinfoRequest;
import com.hw.http.responses.BasicResponse;
import com.hw.http.responses.FollowResponse;
import com.hw.http.responses.GetPostResponse;
import com.hw.http.responses.ShowManyResponse;
import com.hw.http.responses.SignupResponse;
import com.hw.http.responses.TagSearchResponse;
import com.hw.http.responses.TaglineResponse;
import com.hw.http.responses.TimelineResponse;
import com.hw.http.responses.UserSearchResponse;
import com.hw.http.responses.UserinfoResponse;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class HewService {
    private static HewService sInstance = null;
    private UserInfo mUserInfo = new UserInfo();

    private class GetPostCallback extends RequestCallback<GetPostResponse> {
        private RequestCallback<BasicResponse> mLikeCallback;
        private String mPostId;
        private String mTargetUserId;

        public GetPostCallback(String postId, String targetUserId, RequestCallback<BasicResponse> likeCallback) {
            this.mPostId = postId;
            this.mTargetUserId = targetUserId;
            this.mLikeCallback = likeCallback;
        }

        public void onResponse(GetPostResponse getPostResponse) {
            if (getPostResponse.isSuccessful() && !((PostItem) getPostResponse.getItems().get(0)).isHas_liked()) {
                Call likeCall = HttpSingleton.newCall(new LikeRequest(HewService.this.mUserInfo, this.mPostId, this.mTargetUserId));
                likeCall.enqueue(new JsonCallback<BasicResponse>(likeCall, BasicResponse.class) {
                    public void onResponse(BasicResponse likeResponse) {
                        GetPostCallback.this.mLikeCallback.onResponse(likeResponse);
                    }

                    public void onFailure(Exception e) {
                        GetPostCallback.this.mLikeCallback.onFailure(e);
                    }
                });
            }
        }

        public void onFailure(Exception e) {
            this.mLikeCallback.onFailure(e);
        }
    }

    public static HewService getInstance() {
        if (sInstance == null) {
            sInstance = new HewService();
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

    public void getPost(String postId, String targetUserId, final RequestCallback<GetPostResponse> getPostCallback) {
        Call getPostCall = HttpSingleton.newCall(new GetPostRequest(postId, targetUserId));
        getPostCall.enqueue(new JsonCallback<GetPostResponse>(getPostCall, GetPostResponse.class) {
            public void onResponse(GetPostResponse getPostResponse) {
                getPostCallback.onResponse(getPostResponse);
            }

            public void onFailure(Exception e) {
                getPostCallback.onFailure(e);
            }
        });
    }

    public void like(String postId, String targetUserId, RequestCallback<BasicResponse> likeCallback) {
        getPost(postId, targetUserId, new GetPostCallback(postId, targetUserId, likeCallback));
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

    public void signUp(String username, String password, String email, String uuid, final RequestCallback<SignupResponse> signUpCallback) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);
        userInfo.setPassword(password);
        userInfo.setEmail(email);
        userInfo.setUuid(uuid);
        Call signupCall = HttpSingleton.newCall(new SignupRequest(userInfo));
        signupCall.enqueue(new JsonCallback<SignupResponse>(signupCall, SignupResponse.class) {
            public void onResponse(SignupResponse signupResponse) {
                signUpCallback.onResponse(signupResponse);
            }

            public void onFailure(Exception e) {
                signUpCallback.onFailure(e);
            }
        });
    }

    public void setProfile(final RequestCallback<BasicResponse> profileCallback) {
        Call profileCall = HttpSingleton.newCall(new ProfileRequest());
        profileCall.enqueue(new JsonCallback<BasicResponse>(profileCall, BasicResponse.class) {
            public void onResponse(BasicResponse likeResponse) {
                profileCallback.onResponse(likeResponse);
            }

            public void onFailure(Exception e) {
                profileCallback.onFailure(e);
            }
        });
    }

    public void upload(long upload_id, final RequestCallback<BasicResponse> uploadCallback) {
        Call uploadCall = HttpSingleton.newCall(new UploadRequest(upload_id));
        uploadCall.enqueue(new JsonCallback<BasicResponse>(uploadCall, BasicResponse.class) {
            public void onResponse(BasicResponse uploadResponse) {
                uploadCallback.onResponse(uploadResponse);
            }

            public void onFailure(Exception e) {
                uploadCallback.onFailure(e);
            }
        });
    }

    public void media_config(long upload_id, final RequestCallback<BasicResponse> mediaConfigCallback) {
        Call mediaConfigCall = HttpSingleton.newCall(new MediaConfigRequest(upload_id));
        mediaConfigCall.enqueue(new JsonCallback<BasicResponse>(mediaConfigCall, BasicResponse.class) {
            public void onResponse(BasicResponse mediaConfigResponse) {
                mediaConfigCallback.onResponse(mediaConfigResponse);
            }

            public void onFailure(Exception e) {
                mediaConfigCallback.onFailure(e);
            }
        });
    }
}
