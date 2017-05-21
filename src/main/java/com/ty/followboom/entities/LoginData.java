package com.ty.followboom.entities;

import java.util.ArrayList;

public class LoginData {
    private String accountLevel;
    private String admobEnabled;
    private String coins;
    private String defaultFreeCoin;
    private String faqUrl;
    private ArrayList<Integer> flimit;
    private ArrayList<ActionLog> followingHistory;
    private ArrayList<Goods> getFollowGoods;
    private ArrayList<Goods> getLikeGoods;
    private String getLoopsEnabled;
    private ArrayList<Goods> getLoopsGoods;
    private String getRevineEnabled;
    private ArrayList<Goods> getStoreGoods;
    private ArrayList<Goods> iapGoods;
    private String inrVersion;
    private String inviteString;
    private ArrayList<ActionLog> likedHistory;
    private ArrayList<Integer> llimit;
    private String nativexEnabled;
    private String popMessage;
    private String promoteAppDescription;
    private String rateString;
    private String ssEnabled;
    private String tapjoyEnabled;
    private ArrayList<TaskRewards> taskRewards;
    private String videoCoins;

    public String getAccountLevel() {
        return this.accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getVideoCoins() {
        return this.videoCoins;
    }

    public void setVideoCoins(String videoCoins) {
        this.videoCoins = videoCoins;
    }

    public String getCoins() {
        return this.coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public ArrayList<TaskRewards> getTaskRewards() {
        return this.taskRewards;
    }

    public void setTaskRewards(ArrayList<TaskRewards> taskRewards) {
        this.taskRewards = taskRewards;
    }

    public ArrayList<Integer> getLlimit() {
        return this.llimit;
    }

    public void setLlimit(ArrayList<Integer> llimit) {
        this.llimit = llimit;
    }

    public ArrayList<Integer> getFlimit() {
        return this.flimit;
    }

    public void setFlimit(ArrayList<Integer> flimit) {
        this.flimit = flimit;
    }

    public String getRateString() {
        return this.rateString;
    }

    public void setRateString(String rateString) {
        this.rateString = rateString;
    }

    public String getInrVersion() {
        return this.inrVersion;
    }

    public void setInrVersion(String inrVersion) {
        this.inrVersion = inrVersion;
    }

    public String getDefaultFreeCoin() {
        return this.defaultFreeCoin;
    }

    public void setDefaultFreeCoin(String defaultFreeCoin) {
        this.defaultFreeCoin = defaultFreeCoin;
    }

    public String getSsEnabled() {
        return this.ssEnabled;
    }

    public void setSsEnabled(String ssEnabled) {
        this.ssEnabled = ssEnabled;
    }

    public String getNativexEnabled() {
        return this.nativexEnabled;
    }

    public void setNativexEnabled(String nativexEnabled) {
        this.nativexEnabled = nativexEnabled;
    }

    public String getTapjoyEnabled() {
        return this.tapjoyEnabled;
    }

    public void setTapjoyEnabled(String tapjoyEnabled) {
        this.tapjoyEnabled = tapjoyEnabled;
    }

    public String getAdmobEnabled() {
        return this.admobEnabled;
    }

    public void setAdmobEnabled(String admobEnabled) {
        this.admobEnabled = admobEnabled;
    }

    public String getInviteString() {
        return this.inviteString;
    }

    public void setInviteString(String inviteString) {
        this.inviteString = inviteString;
    }

    public String getGetLoopsEnabled() {
        return this.getLoopsEnabled;
    }

    public void setGetLoopsEnabled(String getLoopsEnabled) {
        this.getLoopsEnabled = getLoopsEnabled;
    }

    public String getGetRevineEnabled() {
        return this.getRevineEnabled;
    }

    public void setGetRevineEnabled(String getRevineEnabled) {
        this.getRevineEnabled = getRevineEnabled;
    }

    public String getPromoteAppDescription() {
        return this.promoteAppDescription;
    }

    public void setPromoteAppDescription(String promoteAppDescription) {
        this.promoteAppDescription = promoteAppDescription;
    }

    public ArrayList<Goods> getIapGoods() {
        return this.iapGoods;
    }

    public void setIapGoods(ArrayList<Goods> iapGoods) {
        this.iapGoods = iapGoods;
    }

    public ArrayList<Goods> getGetLikeGoods() {
        return this.getLikeGoods;
    }

    public void setGetLikeGoods(ArrayList<Goods> getLikeGoods) {
        this.getLikeGoods = getLikeGoods;
    }

    public ArrayList<Goods> getGetFollowGoods() {
        return this.getFollowGoods;
    }

    public void setGetFollowGoods(ArrayList<Goods> getFollowGoods) {
        this.getFollowGoods = getFollowGoods;
    }

    public ArrayList<Goods> getGetLoopsGoods() {
        return this.getLoopsGoods;
    }

    public void setGetLoopsGoods(ArrayList<Goods> getLoopsGoods) {
        this.getLoopsGoods = getLoopsGoods;
    }

    public ArrayList<Goods> getGetStoreGoods() {
        return this.getStoreGoods;
    }

    public void setGetStoreGoods(ArrayList<Goods> getStoreGoods) {
        this.getStoreGoods = getStoreGoods;
    }

    public String getPopMessage() {
        return this.popMessage;
    }

    public void setPopMessage(String popMessage) {
        this.popMessage = popMessage;
    }

    public String getFaqUrl() {
        return this.faqUrl;
    }

    public void setFaqUrl(String faqUrl) {
        this.faqUrl = faqUrl;
    }

    public ArrayList<ActionLog> getFollowingHistory() {
        return this.followingHistory;
    }

    public void setFollowingHistory(ArrayList<ActionLog> followingHistory) {
        this.followingHistory = followingHistory;
    }

    public ArrayList<ActionLog> getLikedHistory() {
        return this.likedHistory;
    }

    public void setLikedHistory(ArrayList<ActionLog> likedHistory) {
        this.likedHistory = likedHistory;
    }
}
