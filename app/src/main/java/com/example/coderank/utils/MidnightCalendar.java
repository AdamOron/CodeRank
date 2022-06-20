package com.example.coderank.utils;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * Simple wrapper class for Calendar.
 * Represents a Calendar which ends at midnight.
 *
 * For example, if we pass a Calendar that's set to 18.4.2022 at 7:00, MidnightCalendar will save the 18.4.2022 at 23:59.
 */
public class MidnightCalendar
{
    /* The Calendar instance we are wrapping */
    private final Calendar calendar;

    /**
     * Initialize MidnightCalendar using Calendar.
     *
     * @param calendar the Calendar we are using.
     */
    public MidnightCalendar(Calendar calendar)
    {
        this.calendar = calendar;
        setMidnight();
    }

    /**
     * Initialize MidnightCalendar using LocalDate.
     *
     * @param date the LocalDate we are using.
     */
    public MidnightCalendar(LocalDate date)
    {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        calendar.set(Calendar.MONTH, date.getMonthValue() - 1);
        calendar.set(Calendar.YEAR, date.getYear());
        setMidnight();
    }

    public Calendar getCalendar()
    {
        return calendar;
    }

    /**
     * Sets the MidnightCalendar's Calendar to midnight.
     */
    private void setMidnight()
    {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}
