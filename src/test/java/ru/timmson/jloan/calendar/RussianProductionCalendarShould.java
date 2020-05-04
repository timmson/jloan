package ru.timmson.jloan.calendar;

import org.junit.jupiter.api.Test;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.timmson.jloan.calendar.RussianProductionCalendar.getInstance;

public class RussianProductionCalendarShould {

    final ProductionCalendar ruCalendar = getInstance();

    @Test
    void returnHolidayFrom1to8ofJanuary2020() {
        assertTrue(ruCalendar.isHoliday(2020, 1, 1));
        assertTrue(ruCalendar.isHoliday(2020, 1, 4));
        assertTrue(ruCalendar.isHoliday(2020, 1, 8));
    }

    @Test
    void returnNextWorkingDayInCurrentMonth() {
        assertEquals(of(2020, 5, 6), ruCalendar.getNextWorkDay(2020, 5, 4));
        assertEquals(of(2020, 5, 29), ruCalendar.getNextWorkDay(2020, 5, 31));
    }
}
