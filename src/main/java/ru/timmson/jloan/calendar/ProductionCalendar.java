package ru.timmson.jloan.calendar;

import java.time.LocalDate;

/**
 * Interface of Calendar
 *
 * @author Artem Krotov
 */
public interface ProductionCalendar {

    /**
     * Checks if holiday in given day or not
     *
     * @param date - given date
     * @return true of false
     */
    boolean isHoliday(LocalDate date);

    /**
     * Returns next working date
     *
     * @param date - given date
     * @return
     */
    LocalDate getNextWorkDay(LocalDate date);
}
