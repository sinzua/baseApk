package com.supersonic.mediationsdk.model;

import java.util.ArrayList;
import java.util.Iterator;

public class PlacementsHolder {
    private static final int DEFAULT_PLACEMENT_ID = 0;
    private Placement mDefaultRewardedVideo;
    private ArrayList<Placement> mRewardedVideoPlacements = new ArrayList();

    public void addRewardedVideoPlacement(Placement placementToAdd) {
        if (placementToAdd != null) {
            this.mRewardedVideoPlacements.add(placementToAdd);
            if (placementToAdd.getId() == 0) {
                this.mDefaultRewardedVideo = placementToAdd;
            }
        }
    }

    public Placement getRewardedVideoPlacement(String placementName) {
        Iterator i$ = this.mRewardedVideoPlacements.iterator();
        while (i$.hasNext()) {
            Placement placement = (Placement) i$.next();
            if (placement.getPlacementName().equals(placementName)) {
                return placement;
            }
        }
        return null;
    }

    public Placement getDefaultRewardedVideoPlacement() {
        return this.mDefaultRewardedVideo;
    }
}
