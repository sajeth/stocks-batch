package com.saji.stocks.batch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BatchUtil {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static Calendar getDate(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return calendar;
    }
}
