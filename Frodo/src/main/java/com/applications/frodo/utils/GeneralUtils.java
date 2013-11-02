package com.applications.frodo.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by siddharth on 11/10/13.
 */
public class GeneralUtils {

    public static String getYesterdayDate(){

        Calendar cal=new GregorianCalendar();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE,-1);
        return sdf.format(cal.getTime());
    }

    public static List<String> wrapText(String string, int length, int maxlines){
        List<String> list=new ArrayList<String>();
        StringBuilder sb=new StringBuilder();
        String[] words=string.split("[ ]");
        for(String word:words){
            if(sb.length()+word.length()>length){
                list.add(sb.toString());
                if(list.size()>=maxlines)
                    break;
                sb=new StringBuilder();
            }
            sb.append(word);
            sb.append(" ");
        }

        if(list.size()<maxlines){
            list.add(sb.toString());
        }

        return list;
    }
}
