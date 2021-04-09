package com.bear.animal;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @author bear
 * @date 2021年04月09日 11:31
 */
public class TestTime {

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date now = calendar.getTime();
        System.out.println(new Timestamp(now.getTime()));
    }
}
