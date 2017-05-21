package com.ty.http.responses;

import com.ty.entities.FriendshipStatus;
import java.util.HashMap;

public class ShowManyResponse extends BasicResponse {
    private HashMap<String, FriendshipStatus> friendship_statuses;

    public HashMap<String, FriendshipStatus> getFriendship_statuses() {
        return this.friendship_statuses;
    }

    public void setFriendship_statuses(HashMap<String, FriendshipStatus> friendship_statuses) {
        this.friendship_statuses = friendship_statuses;
    }
}
