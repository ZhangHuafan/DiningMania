package rvrc.geq1917.g34.android.diningmania;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    protected static String formatDate(Date dateObj) {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd_MM_yyyy");
        return dfDate.format(dateObj);
    }

    protected static String formatTime(Date dateObj) {
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(dateObj);
    }
}
