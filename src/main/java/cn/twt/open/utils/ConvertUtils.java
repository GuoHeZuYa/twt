package cn.twt.open.utils;

import org.apache.commons.lang3.StringUtils;

public class ConvertUtils {
    private static final String YEAR_PREFIX = "20";

    public static String toGrade(String userNumber){
        if (StringUtils.isEmpty(userNumber)){
            return "";
        }
        String year = userNumber.substring(2,4);
        return YEAR_PREFIX.concat(year);
    }
}
