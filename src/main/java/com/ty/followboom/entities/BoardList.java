package com.ty.followboom.entities;

import java.util.ArrayList;

public class BoardList {
    private ArrayList<Order> boardList;
    private int page;

    public ArrayList<Order> getBoardList() {
        return this.boardList;
    }

    public void setBoardList(ArrayList<Order> boardList) {
        this.boardList = boardList;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
