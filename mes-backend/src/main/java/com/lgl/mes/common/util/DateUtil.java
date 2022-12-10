package com.lgl.mes.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.sql.Timestamp;

public class DateUtil {
    public static final Calendar calendar = Calendar.getInstance();

    public static Date parse(String dates) {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        try {
            date = sdf.parse(dates);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    public static LocalDateTime GetLocalDateTimeFromString(String dates) {
        LocalDateTime dateTime=LocalDateTime.parse(dates, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return  dateTime;
    }


    public static Timestamp parseT(String dates) {
        Date date = new Date();

        String strDateFormat = "yyyy-MM-ddTHH:mm:ss";  //2022-07-10T15:30
        String res[] = dates.split("T");

        if(res[1].length()==5)   res[1] = res[1]+":00";
        String result = res[0]+" "+res[1];//+":00";

        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        try {
            date = sdf.parse(result);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return new Timestamp(date.getTime());
    }


    public static String GetShortStampString(String Tdates) {
        //Date date = new Date();
        //2022-07-11T21:08-->2022-07-11
        if(Tdates.isEmpty())   return  "" ;
        //2022-07-10T15:30
        String res[] = Tdates.split("T");

        return res[0];
    }


    public static String GetStampString(String Tdates) {
        //Date date = new Date();

        if(Tdates.isEmpty())   return  null ;
          //2022-07-10T15:30
        String res[] = Tdates.split("T");
        if(res[1].length()==5)   res[1] = res[1]+":00";
        String result = res[0]+" "+res[1];//+":00";


        return result;
    }



    public static String GetTString(String Tdates) {
        Date date = new Date();

        //2022-07-10T15:30:00  <---- 2022-07-10T15:30:00
        String res[] = Tdates.split(" ");
        String result = res[0]+"T"+res[1];

        return result;
    }


    public static Date add(Date dateIn, Integer hour) {

        calendar.setTime(dateIn);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();

    }


    public static Boolean IsInScope(LocalDateTime t,LocalDateTime start,LocalDateTime end)
    {
        if((t.isAfter(start)|| t.isEqual(start)) &&   (t.isBefore(end)|| t.isEqual(end))  )
            return true;
        return false;
    }


    public static void main  (String[] args) {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-ddTHH:mm:ss";  //2022-07-10T15:30
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String dates="2022-07-10T15:30";
        try {
            date = sdf.parse(dates);
            System.out.println("date="+date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}