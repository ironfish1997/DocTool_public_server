package top.liuliyong.publicservice.repository.util;

import java.util.Calendar;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
public class TimeUtil {
    private TimeUtil() {
    }

    /**
     * get last day of current month as timestamp
     */
    public static long getLastDayOfCurrentMonth() {
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        long monthlastTimestamp = ca.getTime().getTime();
        return monthlastTimestamp;
    }
}
