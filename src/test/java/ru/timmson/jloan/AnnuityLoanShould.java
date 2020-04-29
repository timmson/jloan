package ru.timmson.jloan;

import org.junit.jupiter.api.Test;
import ru.timmson.jloan.calendar.RussianProductionCalendar;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.timmson.jloan.LoanFactory.annuityLoanBuilder;

public class AnnuityLoanShould {

    @Test
    void calculateAnnuityAmount() {
        final var loan = AnnuityLoan
                .builder()
                .amount(valueOf(110000))
                .annualInterestRate(valueOf(12.9))
                .termInMonth(60)
                .build();

        final var annuityPayment = loan.getAnnuityPayment();

        assertEquals(valueOf(2497.21), annuityPayment);
    }

    @Test
    void calculateSchedule() {
        final var loan = annuityLoanBuilder()
                .amount(valueOf(500000))
                .annualInterestRate(valueOf(11.5))
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(of(2018, 10, 25))
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(44301.39), schedule.getPayments().get(12).getAmount());
        assertEquals(valueOf(31684.22), schedule.getOverallInterest());
    }

    @Test
    void calculateScheduleWithRussianCalendar() {
        final var loan = annuityLoanBuilder()
                .amount(valueOf(500000))
                .annualInterestRate(valueOf(11.5))
                .termInMonth(12)
                .paymentOnDay(5)
                .issueDate(of(2018, 10, 15))
                .productionCalendar(RussianProductionCalendar.getInstance())
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(42623.17), schedule.getPayments().get(12).getAmount());
        assertEquals(valueOf(30006.00).setScale(2, HALF_UP), schedule.getOverallInterest());
    }
}
