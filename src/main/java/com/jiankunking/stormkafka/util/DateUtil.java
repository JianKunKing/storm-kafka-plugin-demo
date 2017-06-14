package com.jiankunking.stormkafka.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiankunking on 2017/4/20 15:08.
 */
public class DateUtil {
    /**
     * 获取当前时间
     * 精确到分钟
     *
     * @return
     */
    public static Long getCurrentTime() {
        //毫秒时间转成分钟
        double doubleTime = (Math.floor(System.currentTimeMillis() / 1000L));
        //往下取整 1.9=> 1.0
        return new Double(doubleTime).longValue();
    }

    /**
     * 第一次获取当前时间，并判断是否是整点
     *
     * @return
     */
    public static List<Long> getCurrentTimeFirst() {
        List<Long> resultList = new ArrayList<Long>();
        Long origin = System.currentTimeMillis();
        //毫秒时间转成分钟
        //Long changed = Long.parseLong(origin.toString().substring(0, origin.toString().length() - 3)+"000");
        double doubleTime = (Math.floor(origin / 1000L));
        //往下取整 1.9=> 1.0
        Long result = new Double(doubleTime).longValue();
        resultList.add(0, result);
        if (origin == result) {
            resultList.add(1, new Long(1));
        } else {
            resultList.add(1, new Long(2));
        }
        return resultList;
    }

    public static Long getDateFloor(Long timeStamp) {
        //毫秒时间转成分钟
        double doubleTime = (Math.floor(timeStamp / 1000L));
        //往下取整 1.9=> 1.0
        return new Double(doubleTime).longValue();
    }
}
