package com.example.notepad.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDateTime {

    public static String dateFromLong(long time)
    {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MM YYYY 'at' hh:mm:aaa", Locale.UK);
        return dateFormat.format(new Date(time));


    }

}
