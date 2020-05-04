package ru.timmson.jloan;

import org.junit.jupiter.api.Test;
import ru.timmson.jloan.calendar.RussianProductionCalendar;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.timmson.jloan.LoanFactory.differentiatedLoanBuilder;

public class DiffirientedLoanShould {

    @Test
    void calculateSchedule() {
        final var loan = differentiatedLoanBuilder()
                .amount(valueOf(50000))
                .annualInterestRate(valueOf(11.5))
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(of(2020, 10, 25))
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(4206.01), schedule.getPayments().get(12).getAmount());
        assertEquals(valueOf(3111.18), schedule.getOverallInterest());
    }

    @Test
    void calculateScheduleWithRussianCalendar() {
        final var loan = differentiatedLoanBuilder()
                .amount(valueOf(50000))
                .annualInterestRate(valueOf(11.5))
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(of(2020, 10, 25))
                .productionCalendar(RussianProductionCalendar.getInstance())
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(4203.39), schedule.getPayments().get(12).getAmount());
        assertEquals(valueOf(3116.44), schedule.getOverallInterest());
    }

    @Test
    void calculateScheduleWithEarlyRepayment() {
        final var loan = differentiatedLoanBuilder()
                .amount(valueOf(50000))
                .annualInterestRate(valueOf(11.5))
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(of(2020, 10, 25))
                .addEarlyRepayment(of(2020, 12, 25), valueOf(15000))
                .productionCalendar(RussianProductionCalendar.getInstance())
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(11, schedule.getPayments().size());
        assertEquals(valueOf(1682.92), schedule.getPayments().get(10).getAmount());
        assertEquals(valueOf(1868.33), schedule.getOverallInterest());
    }
}
