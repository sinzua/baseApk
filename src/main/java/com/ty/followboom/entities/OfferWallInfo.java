package com.ty.followboom.entities;

import java.util.HashMap;

public class OfferWallInfo {
    private HashMap<String, QueryOfferWallResult> offerWallInfo = new HashMap();

    public HashMap<String, QueryOfferWallResult> getOfferWallInfo() {
        return this.offerWallInfo;
    }

    public void setOfferWallInfo(HashMap<String, QueryOfferWallResult> offerWallInfo) {
        this.offerWallInfo = offerWallInfo;
    }
}
