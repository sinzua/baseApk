package com.hw.entities;

import com.hw.hwapi.HewService;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.UUID;

public class MediaConfigParams {
    private String _csrftoken;
    private long _uid;
    private String _uuid;
    private String camera_position;
    private String date_time_digitized = "";
    private String date_time_original;
    private Edits edits;
    private String geotag_enabled;
    private String phone_number;
    private String scene_capture_type;
    private long scene_type;
    private String software;
    private long source_type;
    private long upload_id;
    private String waterfall_id;

    private class Edits {
        private Double[] crop_center = new Double[]{Double.valueOf(0.0d), Double.valueOf(7.812500116415322E-4d)};
        private Integer[] crop_original_size = new Integer[]{Integer.valueOf(2448), Integer.valueOf(3264)};
        private float crop_zoom = 1.3333334f;
        private Integer filter_strength = Integer.valueOf(1);

        public Integer[] getCrop_original_size() {
            return this.crop_original_size;
        }

        public void setCrop_original_size(Integer[] crop_original_size) {
            this.crop_original_size = crop_original_size;
        }

        public float getCrop_zoom() {
            return this.crop_zoom;
        }

        public void setCrop_zoom(float crop_zoom) {
            this.crop_zoom = crop_zoom;
        }

        public Double[] getCrop_center() {
            return this.crop_center;
        }

        public void setCrop_center(Double[] crop_center) {
            this.crop_center = crop_center;
        }

        public Integer getFilter_strength() {
            return this.filter_strength;
        }

        public void setFilter_strength(Integer filter_strength) {
            this.filter_strength = filter_strength;
        }
    }

    public MediaConfigParams(long upload_id) {
        UserInfo userInfo = HewService.getInstance().getUserInfo();
        this._uuid = userInfo.getUuid();
        this._uid = userInfo.getUserid().longValue();
        this._csrftoken = getCsrftoken(userInfo.getCookie());
        this.upload_id = upload_id;
        this.geotag_enabled = "false";
        this.software = "7.1";
        this.phone_number = "";
        this.source_type = 0;
        this.date_time_original = "";
        this.scene_type = 1;
        this.camera_position = "unknown";
        this.scene_capture_type = "standard";
        this.waterfall_id = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        this.edits = new Edits();
    }

    public String getCsrftoken(String cookies) {
        for (String cookie : cookies.split(";")) {
            String[] content = cookie.split(RequestParameters.EQUAL);
            if (content[0].contains("csrftoken")) {
                return content[1];
            }
        }
        return "";
    }

    public String getDate_time_digitized() {
        return this.date_time_digitized;
    }

    public void setDate_time_digitized(String date_time_digitized) {
        this.date_time_digitized = date_time_digitized;
    }

    public String get_uuid() {
        return this._uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public long get_uid() {
        return this._uid;
    }

    public void set_uid(long _uid) {
        this._uid = _uid;
    }

    public String get_csrftoken() {
        return this._csrftoken;
    }

    public void set_csrftoken(String _csrftoken) {
        this._csrftoken = _csrftoken;
    }

    public long getUpload_id() {
        return this.upload_id;
    }

    public void setUpload_id(long upload_id) {
        this.upload_id = upload_id;
    }

    public String getGeotag_enabled() {
        return this.geotag_enabled;
    }

    public void setGeotag_enabled(String geotag_enabled) {
        this.geotag_enabled = geotag_enabled;
    }

    public String getSoftware() {
        return this.software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public long getSource_type() {
        return this.source_type;
    }

    public void setSource_type(long source_type) {
        this.source_type = source_type;
    }

    public String getDate_time_original() {
        return this.date_time_original;
    }

    public void setDate_time_original(String date_time_original) {
        this.date_time_original = date_time_original;
    }

    public long getScene_type() {
        return this.scene_type;
    }

    public void setScene_type(long scene_type) {
        this.scene_type = scene_type;
    }

    public String getCamera_position() {
        return this.camera_position;
    }

    public void setCamera_position(String camera_position) {
        this.camera_position = camera_position;
    }

    public String getScene_capture_type() {
        return this.scene_capture_type;
    }

    public void setScene_capture_type(String scene_capture_type) {
        this.scene_capture_type = scene_capture_type;
    }

    public String getWaterfall_id() {
        return this.waterfall_id;
    }

    public void setWaterfall_id(String waterfall_id) {
        this.waterfall_id = waterfall_id;
    }

    public Edits getEdits() {
        return this.edits;
    }

    public void setEdits(Edits edits) {
        this.edits = edits;
    }
}
