package com.ty.followboom.entities;

public class OrderStatus {
    private String costs;
    private String createTime;
    private String finishedCount;
    private String orderKind;
    private String state;
    private String thumbnailUrl;
    private String totalCount;

    public String getOrderKind() {
        return this.orderKind;
    }

    public void setOrderKind(String orderKind) {
        this.orderKind = orderKind;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFinishedCount() {
        return this.finishedCount;
    }

    public void setFinishedCount(String finishedCount) {
        this.finishedCount = finishedCount;
    }

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getCosts() {
        return this.costs;
    }

    public void setCosts(String costs) {
        this.costs = costs;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
