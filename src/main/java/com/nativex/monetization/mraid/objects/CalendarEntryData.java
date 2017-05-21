package com.nativex.monetization.mraid.objects;

import com.google.gson.annotations.SerializedName;

public class CalendarEntryData {
    @SerializedName("description")
    private String description;
    @SerializedName("end")
    private String end;
    @SerializedName("id")
    private String id;
    @SerializedName("location")
    private String location;
    @SerializedName("reminder")
    private String reminder;
    @SerializedName("start")
    private String start;
    @SerializedName("status")
    private String status;
    @SerializedName("summary")
    private String summary;
    @SerializedName("transparency")
    private String transparency;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransparency() {
        return this.transparency;
    }

    public void setTransparency(String transparency) {
        this.transparency = transparency;
    }

    public String getReminder() {
        return this.reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
}
