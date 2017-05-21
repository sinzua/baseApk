package com.ty.http.responses;

import com.ty.entities.PostItem;
import java.util.ArrayList;

public class TaglineResponse extends BasicResponse {
    private ArrayList<PostItem> items;
    private boolean more_available;
    private String next_max_id;

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

    public String getNext_max_id() {
        return this.next_max_id;
    }

    public void setNext_max_id(String next_max_id) {
        this.next_max_id = next_max_id;
    }
}
