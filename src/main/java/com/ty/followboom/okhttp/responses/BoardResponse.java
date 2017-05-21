package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.BoardList;

public class BoardResponse extends BasicResponse {
    private BoardList data;

    public BoardList getData() {
        return this.data;
    }

    public void setData(BoardList data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return this.data == null || this.data.getBoardList() == null || this.data.getBoardList().size() <= 0;
    }
}
