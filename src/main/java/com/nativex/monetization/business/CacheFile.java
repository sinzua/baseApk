package com.nativex.monetization.business;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.nativex.monetization.enums.FileStatus;
import java.util.Arrays;

public class CacheFile {
    @SerializedName("CDN")
    private String CDN;
    @SerializedName("Ext")
    private String EXT;
    @SerializedName("MD5")
    private String MD5;
    @SerializedName("ExpirationDateUTC")
    private long expirationDate;
    @SerializedName("FileSize")
    private long fileSize;
    private FileStatus fileStatus;
    private long offerId;
    @SerializedName("RelativeUrl")
    private String relativeUrl;

    public long getOfferId() {
        return this.offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public String getRelativeUrl() {
        return this.relativeUrl;
    }

    public void setRelativeUrl(String url) {
        this.relativeUrl = url;
    }

    public FileStatus getFileStatus() {
        return this.fileStatus;
    }

    public void setFileStatus(FileStatus downloadStatus) {
        this.fileStatus = downloadStatus;
    }

    public String getCDN() {
        return this.CDN;
    }

    public void setCDN(String cdn) {
        this.CDN = cdn;
        if (!this.CDN.endsWith("/")) {
            this.CDN += "/";
        }
    }

    public String getMD5() {
        return this.MD5;
    }

    public void setMD5(String md5) {
        this.MD5 = md5;
    }

    public String getExt() {
        return this.EXT;
    }

    public void setExt(String ext) {
        this.EXT = ext;
    }

    public long getExpiryTime() {
        return this.expirationDate;
    }

    public void setExpiryTime(long expiryTime) {
        this.expirationDate = expiryTime;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return this.MD5 + "." + this.EXT;
    }

    public String getDownloadUrl() {
        return this.CDN + this.relativeUrl;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CacheFile)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return getMD5().equals(((CacheFile) obj).getMD5());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.offerId), this.CDN, this.MD5, Long.valueOf(this.fileSize), this.fileStatus, this.relativeUrl});
    }

    public String toJson() {
        return new Gson().toJson((Object) this);
    }
}
