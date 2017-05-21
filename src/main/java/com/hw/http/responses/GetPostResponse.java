package com.hw.http.responses;

import com.hw.entities.PostItem;
import java.util.ArrayList;

public class GetPostResponse extends BasicResponse {
    private ArrayList<PostItem> items;
    private boolean more_available;

    public ArrayList<PostItem> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<PostItem> items) {
        this.items = items;
    }

    public boolean isMore_available() {
        return this.more_available;
    }

    public void setMore_available(boolean more_available) {
        this.more_available = more_available;
    }
}
