package com.wz.logapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zheng on 2017/5/18.
 * 小工具
 */

public class Util {
    //获取当前时间
    public static String getCurrentDate(){
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }
}
