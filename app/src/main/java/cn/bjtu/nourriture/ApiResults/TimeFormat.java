package cn.bjtu.nourriture.ApiResults;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ftb on 15-1-17.
 */
public class TimeFormat {

    public static String format(String dateString)
    {
        String response = dateString;

        String a = response.substring(0,10);
        String b = response.substring(11, 18);
        String c = b.substring(0,2);
        // Get the default MEDIUM/SHORT DateFormat
        SimpleDateFormat formatA = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatB = new SimpleDateFormat("hh:mm:ss");
        // Parse the date
        try {
            Date dateA = formatA.parse(a);
            SimpleDateFormat formatterA = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            Date dateB = formatB.parse(b);
            int h = Integer.parseInt(c) + 8;
            if (h >= 24)
                h -= 24;
            dateB.setHours(h);
            SimpleDateFormat formatterB = new SimpleDateFormat("mm");
            String x = Integer.toString(h);
            if (h < 10)
                x = "0" + x;
            response = formatterA.format(dateA) + " at " + x + ":" + formatterB.format(dateB);
        }
        catch(ParseException pe) {
            System.out.println("ERROR: could not parse date in string \"" +
                    dateString + "\"");
        }
        return response;
    }

    public static String formatSmall(String dateString)
    {
        String response = dateString;

        String a = response.substring(0,10);
        String b = response.substring(11, 18);
        String c = b.substring(0,2);
        // Get the default MEDIUM/SHORT DateFormat
        SimpleDateFormat formatA = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatB = new SimpleDateFormat("hh:mm:ss");
        // Parse the date
        try {
            Date dateA = formatA.parse(a);
            SimpleDateFormat formatterA = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date dateB = formatB.parse(b);
            int h = Integer.parseInt(c) + 8;
            if (h >= 24)
                h -= 24;
            dateB.setHours(h);
            String x = Integer.toString(h);
            if (h < 10)
                x = "0" + x;
            SimpleDateFormat formatterB = new SimpleDateFormat("mm");
            response = formatterA.format(dateA) + " at " + x + ":" + formatterB.format(dateB);
        }
        catch(ParseException pe) {
            System.out.println("ERROR: could not parse date in string \"" +
                    dateString + "\"");
        }
        return response;
    }
}
