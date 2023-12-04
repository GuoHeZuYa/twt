package cn.twt.open.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Lino
 */
public class DateUtils {
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * 获取今日0点的时间戳，为了严谨用23:59:59代替
     * @return
     */
    public static long getTodayZeroTimestamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date zero = calendar.getTime();
        return zero.getTime();
    }

    /**
     * 获取昨日0点的时间戳，为了严谨用今日00:00:00代替
     * @return
     */
    public static long getYesterdayZeroTimestamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, NumberUtils.INTEGER_ZERO);
        calendar.set(Calendar.MINUTE, NumberUtils.INTEGER_ZERO);
        calendar.set(Calendar.SECOND, NumberUtils.INTEGER_ZERO);
        Date zero = calendar.getTime();
        return zero.getTime();
    }
}
