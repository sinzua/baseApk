package com.ty.followboom.entities;

public class ActionLimitParams {
    private int countInDay;
    private int countInHour;
    private int countInMinute;
    private long startTimeInDay;
    private long startTimeInHour;
    private long startTimeInMinute;

    public long getStartTimeInDay() {
        return this.startTimeInDay;
    }

    public void setStartTimeInDay(long startTimeInDay) {
        this.startTimeInDay = startTimeInDay;
    }

    public long getStartTimeInHour() {
        return this.startTimeInHour;
    }

    public void setStartTimeInHour(long startTimeInHour) {
        this.startTimeInHour = startTimeInHour;
    }

    public long getStartTimeInMinute() {
        return this.startTimeInMinute;
    }

    public void setStartTimeInMinute(long startTimeInMinute) {
        this.startTimeInMinute = startTimeInMinute;
    }

    public int getCountInDay() {
        return this.countInDay;
    }

    public void setCountInDay(int countInDay) {
        this.countInDay = countInDay;
    }

    public int getCountInHour() {
        return this.countInHour;
    }

    public void setCountInHour(int countInHour) {
        this.countInHour = countInHour;
    }

    public int getCountInMinute() {
        return this.countInMinute;
    }

    public void setCountInMinute(int countInMinute) {
        this.countInMinute = countInMinute;
    }
}
