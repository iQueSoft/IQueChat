package net.iquesoft.android.seedprojectchat.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class StringToMapUtils {
    public static Map<String, String> splitToMap(String source, String entriesSeparator, String keyValueSeparator) {
        Map<String, String> map = new HashMap<>();
        String[] entries = source.replace("{", "").replace("}","").split(entriesSeparator);
        for (String entry : entries) {
            if (!TextUtils.isEmpty(entry) && entry.contains(keyValueSeparator)) {
                String[] keyValue = entry.split(keyValueSeparator);
                try {
                    map.put(keyValue[0], keyValue[1]);
                } catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
