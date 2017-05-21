package com.ty.followboom.models;

import android.content.Context;
import android.util.Log;
import com.ty.entities.FriendshipStatus;
import com.ty.followboom.entities.ActionLog;
import com.ty.followboom.entities.Order;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.okhttp.responses.BasicResponse;
import com.ty.followboom.okhttp.responses.BoardResponse;
import com.ty.http.RequestCallback;
import com.ty.http.responses.ShowManyResponse;
import com.ty.instagramapi.InstagramService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TrackActionManager {
    private static final String TAG = "TrackActionManager";
    private static TrackActionManager sInstance;
    private ArrayList<ActionLog> mFollowingHistory = new ArrayList();
    private ArrayList<ActionLog> mLikedHistory = new ArrayList();
    private RequestCallback<ShowManyResponse> showManyCallback = new RequestCallback<ShowManyResponse>() {
        public void onResponse(ShowManyResponse showManyResponse) {
            ArrayList<Long> userIds = new ArrayList();
            if (showManyResponse.isSuccessful()) {
                for (String key : showManyResponse.getFriendship_statuses().keySet()) {
                    if (!(((FriendshipStatus) showManyResponse.getFriendship_statuses().get(key)).isFollowing() || ((FriendshipStatus) showManyResponse.getFriendship_statuses().get(key)).isOutgoing_request())) {
                        userIds.add(Long.valueOf(Long.parseLong(key)));
                    }
                }
            }
            LikeServerInstagram.getSingleton().unfollowReport(userIds, TrackActionManager.this.trackActionCallback);
        }

        public void onFailure(Exception e) {
        }
    };
    private com.ty.followboom.okhttp.RequestCallback<BasicResponse> trackActionCallback = new com.ty.followboom.okhttp.RequestCallback<BasicResponse>() {
        public void onResponse(BasicResponse basicResponse) {
        }

        public void onFailure(Exception e) {
        }
    };

    public static TrackActionManager getSingleton() {
        if (sInstance == null) {
            synchronized (TrackActionManager.class) {
                if (sInstance == null) {
                    sInstance = new TrackActionManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        if (AppConfigHelper.getAppConfig(context) != null) {
            this.mFollowingHistory = AppConfigHelper.getAppConfig(context).getFollowingHistory();
            this.mLikedHistory = AppConfigHelper.getAppConfig(context).getLikedHistory();
        }
        if (this.mFollowingHistory != null && this.mLikedHistory != null) {
            print();
            analysis();
        }
    }

    public ArrayList<ActionLog> getFollowingHistory() {
        return this.mFollowingHistory;
    }

    public void setFollowingHistory(ArrayList<ActionLog> mFollowingHistory) {
        this.mFollowingHistory = mFollowingHistory;
    }

    public ArrayList<ActionLog> getLikedHistory() {
        return this.mLikedHistory;
    }

    public void setLikedHistory(ArrayList<ActionLog> mLikedHistory) {
        this.mLikedHistory = mLikedHistory;
    }

    public BoardResponse filterOrder(int curType, BoardResponse boardData) {
        Log.d(TAG, String.format("before filterOrder : %s", new Object[]{Integer.valueOf(boardData.getData().getBoardList().size())}));
        ArrayList<Order> ordersResult = new ArrayList();
        Set<Long> postIds;
        Iterator it;
        Order order;
        ArrayList<Order> ordersInside;
        Order orderInside;
        if (curType == 0) {
            postIds = new HashSet();
            it = this.mLikedHistory.iterator();
            while (it.hasNext()) {
                postIds.add(Long.valueOf(((ActionLog) it.next()).getTargetId()));
            }
            it = boardData.getData().getBoardList().iterator();
            while (it.hasNext()) {
                order = (Order) it.next();
                if (postIds.add(Long.valueOf(Long.parseLong(order.getPostId())))) {
                    ordersInside = new ArrayList();
                    ordersInside.add(order);
                    orderInside = new Order();
                    orderInside.setCollage(ordersInside);
                    ordersResult.add(orderInside);
                }
            }
        } else if (1 == curType) {
            targetUserIds = new HashSet();
            it = this.mFollowingHistory.iterator();
            while (it.hasNext()) {
                targetUserIds.add(Long.valueOf(((ActionLog) it.next()).getTargetId()));
            }
            it = boardData.getData().getBoardList().iterator();
            while (it.hasNext()) {
                order = (Order) it.next();
                if (targetUserIds.add(Long.valueOf(Long.parseLong(order.getViUserId())))) {
                    ordersInside = new ArrayList();
                    ordersInside.add(order);
                    orderInside = new Order();
                    orderInside.setCollage(ordersInside);
                    ordersResult.add(orderInside);
                }
            }
        } else if (2 == curType) {
            postIds = new HashSet();
            it = this.mLikedHistory.iterator();
            while (it.hasNext()) {
                postIds.add(Long.valueOf(((ActionLog) it.next()).getTargetId()));
            }
            targetUserIds = new HashSet();
            it = this.mFollowingHistory.iterator();
            while (it.hasNext()) {
                targetUserIds.add(Long.valueOf(((ActionLog) it.next()).getTargetId()));
            }
            it = boardData.getData().getBoardList().iterator();
            while (it.hasNext()) {
                order = (Order) it.next();
                if (order.isLikeOrder()) {
                    if (postIds.add(Long.valueOf(Long.parseLong(order.getPostId())))) {
                        ordersInside = new ArrayList();
                        ordersInside.add(order);
                        orderInside = new Order();
                        orderInside.setCollage(ordersInside);
                        ordersResult.add(orderInside);
                    }
                } else if (order.isFollowOrder() && targetUserIds.add(Long.valueOf(Long.parseLong(order.getViUserId())))) {
                    ordersInside = new ArrayList();
                    ordersInside.add(order);
                    orderInside = new Order();
                    orderInside.setCollage(ordersInside);
                    ordersResult.add(orderInside);
                }
            }
        }
        Log.d(TAG, String.format("after filterOrder : %s", new Object[]{Integer.valueOf(ordersResult.size())}));
        boardData.getData().setBoardList(ordersResult);
        return boardData;
    }

    public void addToLog(int orderKind, int type, long targetId) {
        ActionLog actionLog;
        if (orderKind == 0) {
            if (this.mLikedHistory != null) {
                actionLog = new ActionLog();
                actionLog.setAction(type);
                actionLog.setTargetId(targetId);
                this.mLikedHistory.add(actionLog);
            }
        } else if (orderKind == 1 && this.mFollowingHistory != null) {
            actionLog = new ActionLog();
            actionLog.setAction(type);
            actionLog.setTargetId(targetId);
            this.mFollowingHistory.add(actionLog);
        }
        print();
    }

    public void print() {
        if (this.mFollowingHistory != null && this.mLikedHistory != null) {
            Log.d(TAG, String.format("mFollowingHistory size: %s, mLikedHistory size: %s", new Object[]{Integer.valueOf(this.mFollowingHistory.size()), Integer.valueOf(this.mLikedHistory.size())}));
        }
    }

    private void analysis() {
        ArrayList<Long> targetUserIds = new ArrayList();
        Iterator it = this.mFollowingHistory.iterator();
        while (it.hasNext()) {
            targetUserIds.add(Long.valueOf(((ActionLog) it.next()).getTargetId()));
        }
        InstagramService.getInstance().showMany(targetUserIds, this.showManyCallback);
    }
}
