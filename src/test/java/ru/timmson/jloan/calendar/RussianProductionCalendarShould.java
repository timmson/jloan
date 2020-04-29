package ru.timmson.jloan.calendar;

import org.junit.jupiter.api.Test;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.timmson.jloan.calendar.RussianProductionCalendar.getInstance;

public class RussianProductionCalendarShould {

    @Test
    void shouldReturnHolidayFrom1to8ofJanuary2020() {
        final var ruCalendar = getInstance();

        assertTrue(ruCalendar.isHoliday(of(2020, 1, 1)));
        assertTrue(ruCalendar.isHoliday(of(2020, 1, 4)));
        assertTrue(ruCalendar.isHoliday(of(2020, 1, 8)));
    }
}
