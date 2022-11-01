package com.vld;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static javax.swing.JOptionPane.showMessageDialog;

public class StaticFunctions {

    public static Date getStartOfDayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();

    }

    public static Timestamp getStartOfDay(Date date) {

        Date startOfDay = getStartOfDayDate(date);
        Timestamp timestamp = new Timestamp(startOfDay.getTime());
        return timestamp;

    }

    public static Date getEndOfDayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);

        return calendar.getTime();

    }

    public static Timestamp getEndOfDay(Date date) {

        Date endOfDay = getEndOfDayDate(date);
        Timestamp timestamp = new Timestamp(endOfDay.getTime());

        getTimeZone();
        return timestamp;
    }

    public static Timestamp currentDate() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static Date getDate() {
        Date date = new java.util.Date();
        return date;
    }

    public static String getTimeZone() {

        return Calendar.getInstance().getTimeZone().getID();


    }

    public static void showMessage(String message) {
        showMessageDialog(null, message);
    }

    public static TableColumn getJTableColumn(JTable jTable, String columnName) {

        int columnIndex = jTable.getColumnModel().getColumnIndex(columnName);
        return jTable.getColumnModel().getColumn(columnIndex);

    }

    public static int getTableColumnIndex(JTable jTable, String columnName) {
        int columnIndex = jTable.getColumnModel().getColumnIndex(columnName);
        return columnIndex;
    }

    public static String castToTimeString(long downtimeCounter) {

        long days = downtimeCounter / (24 * 60 * 60);

        downtimeCounter %= (24 * 60 * 60);

        long hours = downtimeCounter / (60 * 60);
        downtimeCounter %= (60 * 60);

        long minutes = downtimeCounter / 60;
        long seconds = downtimeCounter % 60;

        return ""
                + (days > 0 ? "" + days + " days" : "")
                + (hours >= 10 ? "" + hours : "0" + hours)
                + ":"
                + (minutes >= 10 ? "" + minutes : "0" + minutes)
                + ":"
                + (seconds >= 10 ? "" + seconds : "0" + seconds)
                ;


    }

    public static void sleep(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
