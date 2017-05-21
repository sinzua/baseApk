package com.hw.http.responses;

import com.hw.entities.LoginUser;
import java.util.ArrayList;

public class FollowResponse extends BasicResponse {
    private boolean big_list;
    private String next_max_id;
    private int page_size;
    private ArrayList<LoginUser> users;

    public boolean isBig_list() {
        return this.big_list;
    }

    public void setBig_list(boolean big_list) {
        this.big_list = big_list;
    }

    public ArrayList<LoginUser> getUsers() {
        return this.users;
    }

    public void setUsers(ArrayList<LoginUser> users) {
        this.users = users;
    }

    public int getPage_size() {
        return this.page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public String getNext_max_id() {
        return this.next_max_id;
    }

    public void setNext_max_id(String next_max_id) {
        this.next_max_id = next_max_id;
    }
}
