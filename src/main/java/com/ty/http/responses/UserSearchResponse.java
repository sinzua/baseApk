package com.ty.http.responses;

import com.ty.entities.LoginUser;
import java.util.ArrayList;

public class UserSearchResponse extends BasicResponse {
    private boolean has_more;
    private int num_results;
    private ArrayList<LoginUser> users;

    public boolean isHas_more() {
        return this.has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public ArrayList<LoginUser> getUsers() {
        return this.users;
    }

    public void setUsers(ArrayList<LoginUser> users) {
        this.users = users;
    }

    public int getNum_results() {
        return this.num_results;
    }

    public void setNum_results(int num_results) {
        this.num_results = num_results;
    }
}
