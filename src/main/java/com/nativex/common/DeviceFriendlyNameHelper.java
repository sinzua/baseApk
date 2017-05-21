package com.nativex.common;

import android.os.Build;
import android.text.TextUtils;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class DeviceFriendlyNameHelper {
    private final String[] KNOWN_DEVICES = new String[]{"HTC One|htc_mecdwg|HTC M8Sd", "HTC One|htc_mectl|HTC M8St", "HTC One|htc_mectl|HTC One_E8", "HTC One|htc_mecul|HTC One_E8", "HTC One|htc_mecul|HTC_M8Sx", "HTC One|htc_mecwhl|0PAJ5", "HTC One|htc_mecdug|HTC One_E8 dual sim", "HTC One|htc_mecdwg|HTC 0PAJ4", "HTC One|htc_mecdwg|HTC One_E8 dual sim", "HTC One|htc_mectl|HTC M8St", "HTC One|htc_mectl|HTC M8Ss", "HTC One|m7|HTC 801e", "HTC One|m7|HTC One 801e", "HTC One|m7|HTC One", "HTC One|m7|HTC_PN071", "HTC One|m7cdtu|HTC 802t 16GB", "HTC One|m7cdtu|HTC 802t", "HTC One|m7cdug|HTC 802w", "HTC One|m7cdug|HTC One dual sim", "HTC One|m7cdwg|HTC 802d", "HTC One|m7cdwg|HTC One dual 802d", "HTC One|m7cdwg|HTC One dual sim", "HTC One|m7wls|HTC One", "HTC One|m7wls|HTCONE", "HTC One|m7wlv|HTC One", "HTC One|m7wlv|HTC6500LVW", "HTC One|htc_m8dug|HTC M8e", "HTC One|htc_m8|HTC M8w", "HTC One|htc_m8|HTC One_M8", "HTC One|htc_m8|HTC_0P6B", "HTC One|htc_m8|HTC_0P6B6", "HTC One|htc_m8|HTC_M8x", "HTC One|htc_m8dug|HTC One_M8 dual sim", "HTC One|htc_m8dwg|HTC M8d", "HTC One|htc_m8whl|831C", "HTC One|htc_m8wl|HTC One_M8", "HTC One|htc_m8wl|HTC6525LVW", "HTC One 801e|m7|HTC One 801e", "HTC One 801e|m7|HTC One 801s", "HTC One Dual 802d|m7cdwg|HTC One dual 802d", "HTC One Dual Sim|m7cdwg|HTC One dual sim", "HTC One E8 dual|htc_mecdwg|HTC_M8Sd", "HTC One Google Play edition|m7|HTC One", "HTC One M8 eye 4G LTE|htc_melstuhl|HTC M8Et", "HTC One M8 eye 4G LTE|htc_melsuhl|HTC M8Ew", "HTC One max|t6ul|HTC One max", "HTC One max|t6ul|HTC_One_max", "HTC One max|t6whl|HTC0P3P7", "HTC One mini|htc_m4|HTC One mini", "HTC One mini|m4|HTC One mini", "HTC One mini|m4|HTC_One_mini_601e", "HTC One mini|m4|HTC_PO582", "HTC One mini 2|htc_memul|HTC One mini 2", "HTC One mini 2|htc_memul|HTC_M8MINx", "HTC One mini 2|htc_memul|HTC_One_mini_2", "HTC One mini 601E|m4|HTC_One_mini_601e", "HTC One mini2|htc_memul|HTC One mini 2", "HTC One remix|htc_memwl|HTC6515LVW", "HTC One S|ville|HTC One S", "HTC One S|ville|HTC VLE_U", "HTC One S|villec2|HTC One S", "HTC One S Special Edition|villeplus|HTC One S Special Edition", "HTC One SC|cp2dcg|HTC One SC T528d", "HTC One SC|cp2dcg|HTC One SC", "HTC One SV|k2cl|C525c", "HTC One SV|k2plccl|HTC One SV", "HTC ONE SV|k2u|HTC K2_U", "HTC ONE SV|k2u|HTC One SV", "HTC One SV|k2ul|HTC K2_UL", "HTC One SV|k2ul|HTC One SV", "HTC ONE SV|k2ul|HTC One SV", "HTC One SV BLK|k2plccl|HTC One SV BLK", "HTC One SV BLK|k2plccl|HTC One SV", "HTC One V|primoc|HTC One V", "HTC One V|primou|HTC ONE V", "HTC One V|primou|HTC One V", "HTC One VX|totemc2|HTC One VX", "HTC One X|endeavoru|HTC One X", "HTC One X|evita|HTC One X", "HTC One X+|enrc2b|HTC One X+", "HTC One X+|evitareul|HTC One X+", "HTC One XL|evita|HTC One X", "HTC One XL|evita|HTC One XL", "HTC One XL|evita|HTC_One_XL", "HTC One XL|evitautl|HTC EVA_UTL", "HTC One_E8|htc_mecul_emea|HTC One_E8", "HTC Onex X|endeavoru|HTC One X"};

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : str.toCharArray()) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase = phrase + Character.toUpperCase(c);
                capitalizeNext = false;
            } else {
                if (Character.isWhitespace(c)) {
                    capitalizeNext = true;
                }
                phrase = phrase + c;
            }
        }
        return phrase;
    }

    public String getDeviceName() {
        String key = Build.DEVICE + "|" + Build.MODEL;
        for (String str : this.KNOWN_DEVICES) {
            if (str.endsWith(key)) {
                return str.split("\\|")[0];
            }
        }
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        if (manufacturer.equalsIgnoreCase("htc")) {
            return "HTC " + model;
        }
        return capitalize(manufacturer) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + model;
    }
}
