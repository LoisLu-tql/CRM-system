package com.loix.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    // format specified date and time
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    // format specified date
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    // format specified time
    public static String formateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

}
