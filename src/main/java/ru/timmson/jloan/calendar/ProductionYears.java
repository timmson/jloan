package ru.timmson.jloan.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.LocalDate.of;

public class ProductionYears {

    private static final List<DayOfWeek> SAT_OR_SUM = List.of(SATURDAY, SUNDAY);

    public static ProductionYear get2020() {
        final var holidays = Set.of(
                of(2020, 1, 1),
                of(2020, 1, 2),
                of(2020, 1, 3),
                of(2020, 1, 6),
                of(2020, 1, 7),
                of(2020, 1, 8),
                of(2020, 2, 24),
                of(2020, 3, 8),
                of(2020, 5, 1),
                of(2020, 5, 4),
                of(2020, 5, 5),
                of(2020, 5, 9),
                of(2020, 6, 12),
                of(2020, 11, 4)
        );
        return (date) -> holidays.contains(date) || isSatOrSun(date);
    }

    public static boolean isSatOrSun(LocalDate date) {
        return SAT_OR_SUM.contains(date.getDayOfWeek());
    }

}
