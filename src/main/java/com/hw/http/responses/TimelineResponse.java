package com.hw.http.responses;

import com.hw.entities.PostItem;
import java.util.ArrayList;

public class TimelineResponse extends BasicResponse {
    private boolean auto_load_more_enabled;
    private ArrayList<PostItem> items;
    private boolean more_available;
    private String next_max_id;
    private int num_results;

    public boolean isAuto_load_more_enabled() {
        return this.auto_load_more_enabled;
    }

    public void setAuto_load_more_enabled(boolean auto_load_more_enabled) {
        this.auto_load_more_enabled = auto_load_more_enabled;
    }

    public ArrayList<PostItem> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<PostItem> items) {
        this.items = items;
    }

    public int getNum_results() {
        return this.num_results;
    }

    public void setNum_results(int num_results) {
        this.num_results = num_results;
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
