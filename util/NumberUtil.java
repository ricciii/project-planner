package util;

import java.util.ArrayList;
import java.util.List;

public class NumberUtil {
    public static List<Long> stringToLongList(String string) {
        List<Long> longList = new ArrayList<Long>();
        String[] stringArray = string.split("\\s+");
        for (String str : stringArray) {
            longList.add(Long.parseLong(str));
        }
        return longList;
    }

    public static boolean isStringInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
