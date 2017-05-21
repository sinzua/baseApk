package com.nativex.common;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class UDIDs extends ArrayList<UDID> {
    private static final long serialVersionUID = 1;

    public class UDID {
        @SerializedName("Type")
        private String type;
        @SerializedName("Value")
        private String value;

        public boolean equals(Object object) {
            if (!(object instanceof UDID)) {
                return false;
            }
            UDID udid = (UDID) object;
            if (udid.type.equals(this.type) && udid.value.equals(this.value)) {
                return true;
            }
            return false;
        }
    }

    public void addUDID(String type, String value) {
        if (value != null) {
            UDID udid = new UDID();
            udid.type = type;
            udid.value = value;
            add(udid);
        }
    }

    public boolean equals(Object object) {
        if (!(object instanceof UDIDs)) {
            return false;
        }
        UDIDs udid = (UDIDs) object;
        if (udid.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!((UDID) udid.get(i)).equals(get(i))) {
                return false;
            }
        }
        return true;
    }
}
