package com.ty.helloworld.okhttp.responses;

import com.ty.helloworld.entities.FriendShip;
import java.util.Map;

public class GetFriendShipResponse extends BasicResponse {
    private Map<Long, FriendShip> data;

    public Map<Long, FriendShip> getData() {
        return this.data;
    }

    public void setData(Map<Long, FriendShip> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return this.data == null || this.data.size() <= 0;
    }
}
