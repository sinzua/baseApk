package com.ty.followboom.models;

import com.ty.followboom.entities.Order;
import com.ty.followboom.okhttp.responses.BoardResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class GetCoinsManager {
    public static final int GET_FOLLOWER = 1;
    public static final int GET_LIKE = 0;
    public static final int MIX = 2;
    private static GetCoinsManager instance;
    private HashMap<Integer, BoardResponse> mBoardDataMap = new HashMap();
    private HashMap<Integer, Integer> mCurIndexMap = new HashMap();

    public static GetCoinsManager getSingleton() {
        if (instance == null) {
            synchronized (GetCoinsManager.class) {
                if (instance == null) {
                    instance = new GetCoinsManager();
                }
            }
        }
        return instance;
    }

    public GetCoinsManager() {
        for (int i = 0; i < 3; i++) {
            this.mCurIndexMap.put(Integer.valueOf(i), Integer.valueOf(0));
        }
    }

    public ArrayList<Order> getCurrentBoardList(int curType) {
        if (this.mBoardDataMap.get(Integer.valueOf(curType)) == null || ((BoardResponse) this.mBoardDataMap.get(Integer.valueOf(curType))).getData() == null) {
            return null;
        }
        return ((BoardResponse) this.mBoardDataMap.get(Integer.valueOf(curType))).getData().getBoardList();
    }

    public int getCurrentPage(int curType) {
        if (this.mBoardDataMap.get(Integer.valueOf(curType)) == null || ((BoardResponse) this.mBoardDataMap.get(Integer.valueOf(curType))).getData() == null) {
            return 0;
        }
        return ((BoardResponse) this.mBoardDataMap.get(Integer.valueOf(curType))).getData().getPage();
    }

    public boolean verifyBoardList(int curType) {
        return getCurrentBoardList(curType) != null && getCurrentBoardList(curType).size() > 0;
    }

    public Order getCurrentOrder(int curType) {
        return (Order) getCurrentBoardList(curType).get(((Integer) this.mCurIndexMap.get(Integer.valueOf(curType))).intValue());
    }

    public int getCurrentIndex(int curType) {
        return ((Integer) this.mCurIndexMap.get(Integer.valueOf(curType))).intValue();
    }

    public void setCurIndexMap(int curType, int index) {
        this.mCurIndexMap.put(Integer.valueOf(curType), Integer.valueOf(index));
    }

    public void setCurBoardDataMap(int curType, BoardResponse boardData) {
        this.mBoardDataMap.put(Integer.valueOf(curType), boardData);
    }

    public boolean isCurBoardDataEmpty(int curType) {
        return this.mBoardDataMap.get(Integer.valueOf(curType)) == null;
    }
}
