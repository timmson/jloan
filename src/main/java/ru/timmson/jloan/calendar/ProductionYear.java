package ru.timmson.jloan.calendar;

import java.time.LocalDate;

/**
 * Interface of Production year
 *
 * @author Artem Krotov
 */
@FunctionalInterface
public interface ProductionYear {

    boolean isHoliday(LocalDate date);

}
