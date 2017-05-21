package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.OrderStatusData;

public class OrderStatusResponse extends BasicResponse {
    private OrderStatusData data;

    public OrderStatusData getData() {
        return this.data;
    }

    public void setData(OrderStatusData data) {
        this.data = data;
    }
}
