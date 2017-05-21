package com.lidroid.xutils.view;

public class ViewInjectInfo {
    public int parentId;
    public Object value;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewInjectInfo)) {
            return false;
        }
        ViewInjectInfo that = (ViewInjectInfo) o;
        if (this.parentId != that.parentId) {
            return false;
        }
        if (this.value != null) {
            return this.value.equals(that.value);
        }
        if (that.value != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.value.hashCode() * 31) + this.parentId;
    }
}
