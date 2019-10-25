package com.study.springboot01.com.lq.util.Stringutil;

import org.springframework.stereotype.Component;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-06-17 17:24
 * Editored:
 */

public class StringUtil {

    public static boolean isNotEmpty(Object o){
        if(o==null||"".equals(o)){
            return false;
        }
        return true;
    }
    /**
     * 将obj 转换为String
     *
     * @param obj
     * @return
     */
    public static String getObjStr(Object obj) {
        return obj == null ? "" : obj.toString().trim();
    }

}
