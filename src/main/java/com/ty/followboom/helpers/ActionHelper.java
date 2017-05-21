package com.ty.followboom.helpers;

import android.content.Context;
import com.forwardwin.base.widgets.JsonSerializer;
import com.forwardwin.base.widgets.PreferenceHelper;
import com.ty.followboom.entities.TaskRewards;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.GetAccountInfoResponse;
import java.util.ArrayList;

public class ActionHelper {
    private static final String TASK_REWARDS_DEFAULT = "[{'rewardTypeId': 0,'rewardName': 'rateUs', 'rewardCoin': 20},{'rewardTypeId': 1,'rewardName': 'dailyLogin','rewardCoin': 5},{ 'rewardTypeId': 2, 'rewardName': 'share','rewardCoin': 10}]";
    public static final int TYPE_DAILY_LOGIN = 1;
    public static final int TYPE_RATEUS = 0;
    public static final int TYPE_SHARE = 2;

    public static ArrayList<TaskRewards> getTaskRewards(Context context) {
        ArrayList<TaskRewards> taskRewards = (ArrayList) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, "appinfo", "taskrewards"), ArrayList.class, TaskRewards.class);
        return taskRewards != null ? taskRewards : (ArrayList) JsonSerializer.getInstance().deserialize(TASK_REWARDS_DEFAULT, ArrayList.class, TaskRewards.class);
    }

    public static void setTaskRewards(Context context, ArrayList<TaskRewards> taskRewards) {
        PreferenceHelper.saveContent(context, "appinfo", "taskrewards", JsonSerializer.getInstance().serialize(taskRewards));
    }

    public static int getRateUsRewards(Context context) {
        ArrayList<TaskRewards> taskRewards = getTaskRewards(context);
        if (taskRewards != null) {
            for (int i = 0; i < taskRewards.size(); i++) {
                if (((TaskRewards) taskRewards.get(i)).getRewardTypeId() == 0) {
                    return ((TaskRewards) taskRewards.get(i)).getRewardCoin();
                }
            }
        }
        return 20;
    }

    public static int getDailyLoginRewards(Context context) {
        ArrayList<TaskRewards> taskRewards = getTaskRewards(context);
        for (int i = 0; i < taskRewards.size(); i++) {
            if (1 == ((TaskRewards) taskRewards.get(i)).getRewardTypeId()) {
                return ((TaskRewards) taskRewards.get(i)).getRewardCoin();
            }
        }
        return 5;
    }

    public static void rateUs(Context context, RequestCallback<GetAccountInfoResponse> getRewardsRequestCallBack) {
        if (couldRateUs(context)) {
            PreferenceHelper.saveInteger(context, "appinfo", "last_rate_version", Integer.parseInt(VLTools.getAppInfo(context)));
            LikeServerInstagram.getSingleton().rateUs(context, 0, getRewardsRequestCallBack);
        }
    }

    public static boolean couldRateUs(Context context) {
        return !VLTools.getAppInfo(context).equals(PreferenceHelper.getInteger(context, "appinfo", "last_rate_version").toString());
    }

    public static int getShareRewards(Context context) {
        ArrayList<TaskRewards> taskRewards = getTaskRewards(context);
        for (int i = 0; i < taskRewards.size(); i++) {
            if (2 == ((TaskRewards) taskRewards.get(i)).getRewardTypeId()) {
                return ((TaskRewards) taskRewards.get(i)).getRewardCoin();
            }
        }
        return 10;
    }
}
