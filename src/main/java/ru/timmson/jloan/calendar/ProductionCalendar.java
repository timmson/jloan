package ru.timmson.jloan.calendar;

import java.time.LocalDate;

/**
 * Interface of Calendar
 *
 * @author Artem Krotov
 */
public interface ProductionCalendar {

    /**
     * Check if holiday in given day or not
     *
     * @param date - given date
     * @return true of false
     */
    boolean isHoliday(LocalDate date);

}
