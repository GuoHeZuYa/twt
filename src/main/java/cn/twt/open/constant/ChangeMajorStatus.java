package cn.twt.open.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lino
 */
public class ChangeMajorStatus {
    private static Map<Integer, String> statusMap = new HashMap<>();

    static {
        statusMap.put(0,"申请中");
        statusMap.put(1,"申请通过");
        statusMap.put(2,"申请未通过");
    }

    public static String getStatus(int index){
        return statusMap.get(index);
    }
}
