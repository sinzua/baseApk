package com.ty.followboom.helpers;

import android.content.Context;
import com.forwardwin.base.widgets.JsonSerializer;
import com.ty.followboom.entities.Goods;
import com.ty.followboom.entities.LoginData;
import java.util.ArrayList;
import java.util.Iterator;

public class GoodsHelper {
    public static ArrayList<Goods> getGoodsDataLikes(Context context) {
        return AppConfigHelper.getAppConfig(context).getGetLikeGoods();
    }

    public static void setGoodsDataLikes(Context context, ArrayList<Goods> mGoodsDataLikes) {
        LoginData appConfig = AppConfigHelper.getAppConfig(context);
        appConfig.setGetLikeGoods(mGoodsDataLikes);
        AppConfigHelper.saveAppConfig(context, JsonSerializer.getInstance().serialize(appConfig));
    }

    public static ArrayList<Goods> getGoodsDataFollowers(Context context) {
        return AppConfigHelper.getAppConfig(context).getGetFollowGoods();
    }

    public static void setGoodsDataFollowers(Context context, ArrayList<Goods> mGoodsDataFollowers) {
        LoginData appConfig = AppConfigHelper.getAppConfig(context);
        appConfig.setGetLikeGoods(mGoodsDataFollowers);
        AppConfigHelper.saveAppConfig(context, JsonSerializer.getInstance().serialize(appConfig));
    }

    public static ArrayList<Goods> getGoodsDataLoops(Context context) {
        return AppConfigHelper.getAppConfig(context).getGetLoopsGoods();
    }

    public static void setGoodsDataLoops(Context context, ArrayList<Goods> mGoodsDataLoops) {
        LoginData appConfig = AppConfigHelper.getAppConfig(context);
        appConfig.setGetLoopsGoods(mGoodsDataLoops);
        AppConfigHelper.saveAppConfig(context, JsonSerializer.getInstance().serialize(appConfig));
    }

    public static ArrayList<Goods> getGoodsDataCoins(Context context) {
        return AppConfigHelper.getAppConfig(context).getIapGoods();
    }

    public static void setGoodsDataCoins(Context context, ArrayList<Goods> mGoodsDataCoins) {
        LoginData appConfig = AppConfigHelper.getAppConfig(context);
        appConfig.setGetLikeGoods(mGoodsDataCoins);
        AppConfigHelper.saveAppConfig(context, JsonSerializer.getInstance().serialize(appConfig));
    }

    public static ArrayList<String> getProductIds(Context context) {
        ArrayList<Goods> goodsDataCoins = getGoodsDataCoins(context);
        if (goodsDataCoins == null) {
            return null;
        }
        ArrayList<String> results = new ArrayList();
        Iterator it = goodsDataCoins.iterator();
        while (it.hasNext()) {
            results.add(((Goods) it.next()).getGoodsId());
        }
        return results;
    }
}
