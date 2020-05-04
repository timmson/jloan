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
     * Checks if holiday in given day or not
     *
     * @param year,       e.g 2020
     * @param month,      e.g 10 (October)
     * @param dayOfMonth, eg 15
     * @return true of false
     */
    default boolean isHoliday(int year, int month, int dayOfMonth) {
        return isHoliday(LocalDate.of(year, month, dayOfMonth));
    }

    /**
     * Returns next working date
     *
     * @param date - given date
     * @return the closest working date
     */
    LocalDate getNextWorkDay(LocalDate date);

    /**
     * Returns next working date
     *
     * @param year,       e.g 2020
     * @param month,      e.g 10 (October)
     * @param dayOfMonth, eg 15
     * @return the closest working date
     */
    default LocalDate getNextWorkDay(int year, int month, int dayOfMonth) {
        return getNextWorkDay(LocalDate.of(year, month, dayOfMonth));
    }
}
