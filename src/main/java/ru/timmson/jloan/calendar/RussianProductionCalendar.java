package ru.timmson.jloan.calendar;

import java.time.LocalDate;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
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

    /**
     * Returns next working date
     *
     * @param date - given date
     * @return the closest working date
     */
    @Override
    public LocalDate getNextWorkDay(LocalDate date) {
        var isForward = true;
        while (isHoliday(date)) {
            if (date.isEqual(date.with(lastDayOfMonth()))) {
                isForward = false;
            }
            date = isForward ? date.plusDays(1) : date.minusDays(1);
        }
        return date;
    }
}
