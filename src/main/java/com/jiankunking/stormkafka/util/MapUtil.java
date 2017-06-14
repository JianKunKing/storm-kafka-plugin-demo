package com.jiankunking.stormkafka.util;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by jiankunking on 2017/4/23 9:46.
 */
public class MapUtil {
    /**
     * 方法名称:transMapToString
     * 传入参数:map
     * 返回值:String 形如 username'chenziwen^password'1234
     */
    public static String transMapToString(Map map) {
        if (map == null) {
            return "";
        }
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("'").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }
}
