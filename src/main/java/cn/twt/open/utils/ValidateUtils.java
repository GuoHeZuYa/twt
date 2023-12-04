package cn.twt.open.utils;

public class ValidateUtils {
    public static boolean isEmail(String str) {
        if ("".equals(str) || str==null){
            return false;
        }
        String reg = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        return str.matches(reg);
    }

    // 现在手机号种类太多了，只做简单判断
    public static boolean isPhone(String str){
        if ("".equals(str) || str==null){
            return false;
        }
        return str.length()==11 && str.charAt(0)=='1';
    }
}
