package ru.timmson.jloan.calendar;

import java.time.LocalDate;
import java.util.Map;

import static ru.timmson.jloan.calendar.ProductionYears.get2020;

/**
 * Russian production calendar
 *
 * @author Artem Krotov
 */
public class RussianProductionCalendar implements ProductionCalendar {

    private static ProductionCalendar INSTANCE;

    private final Map<Integer, ProductionYear> holidays;

    private RussianProductionCalendar() {
        this.holidays = Map.of(2020, get2020());
    }

    /**
     * Creates new instance of calendar
     *
     * @return {@link ProductionCalendar}
     */
    public static ProductionCalendar getInstance() {
        if (INSTANCE == null) {
            synchronized (RussianProductionCalendar.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RussianProductionCalendar();
                }
            }
        }
        return INSTANCE;

    }

    /**
     * Check if holiday in given day or not
     *
     * @param date - given date
     * @return true of false
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        return this.holidays.getOrDefault(date.getYear(), ProductionYears::isSatOrSun).isHoliday(date);
    }

    @Override
    public LocalDate getNextWorkDay(LocalDate date) {
        throw new UnsupportedOperationException();
    }
}
