package com.lutshe.doiter.views.util;

/**
 * Created by Arsen Adzhiametov on 2/15/14 in IntelliJ IDEA.
 */
public class StringUtils {
    private StringUtils() {}

    public static String getDayOrDaysString(int daysCount) {
        String number = String.valueOf(daysCount);
        if ((number.endsWith("2") || number.endsWith("3") || number.endsWith("4")) && !number.startsWith("1")){
            return  "дня  "; //whitespace is necessary for layout constant width
        } else if (!String.valueOf(daysCount).endsWith("1") || daysCount == 11) {
            return  "дней";
        } else {
            return  "день";
        }
    }
}
