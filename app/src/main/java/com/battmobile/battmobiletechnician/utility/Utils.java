package com.battmobile.battmobiletechnician.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.appcompat.app.ActionBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utils {
    public static Date date;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void setActionBarScreen(String name, ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(name);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public static long toStamp(String dateString) {

        if (dateString.trim().length() == 0)
            return 0;
        else {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long unixTime = date.getTime();
            return (unixTime / 1000);
        }
    }

    public static long toUnixStampFrom(String dateString) {

        if (dateString.trim().length() == 0)
            return 0;
        else {
            //2019-02-02 00:00:00
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long unixTime = date.getTime();
            return (unixTime / 1000);
        }
    }

    public static class TimeStamp {

        public static Date convertUnixToDate(String unixDateTime) {
            long datetime = Long.parseLong(unixDateTime);
            Date date = new Date(datetime * 1000L);
            return date;
        }
        public static String getISODateFromEpoch(long unixTime) {
            unixTime = unixTime * 1000;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(unixTime);
            return formatter.format(calendar.getTimeInMillis());
        }
        public static String getFormattedDateTimeFromEpoch(long unixTime) {
            unixTime = unixTime * 1000;
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(unixTime);
            return formatter.format(calendar.getTimeInMillis());
        }

        public static String getFormattedDateFromEpoch(long unixTime) {
            unixTime = unixTime * 1000;
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(unixTime);
            return formatter.format(calendar.getTimeInMillis());
        }

        public static String getFormattedTimeFromEpoch(long unixTime) {
            unixTime = unixTime * 1000;
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(unixTime);
            return formatter.format(calendar.getTimeInMillis());
        }
    }

    public static class TimeConverter {

        public static String getHMSfromSeconds(int seconds) {

            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            seconds = seconds % 60;
            if (hours == 0 && minutes == 0)
                return seconds + "s";
            else if (hours == 0)
                return minutes + "m " + seconds + "s";
            else
                return hours + "h " + minutes + "m " + seconds + "s";
        }

        public static String getHMfromMinutes(int minutes) {

            int hours = minutes / 60;
            int remaining = minutes % 60;
            if (hours == 0 && remaining == 0)
                return "N/A";
            else if (hours == 0)
                return remaining + " min";
            else if (remaining == 0)
                return hours + " hour";

            else
                return hours + " hour " + remaining + " min";
        }

        public static String converttoDateTime(String date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date testDate = null;
            try {
                testDate = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
            return formatter.format(testDate);
        }

        public static String converttoDate(String date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date testDate = null;
            try {
                testDate = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");
            return formatter.format(testDate);
        }
    }


}
