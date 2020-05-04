package ru.timmson.jloan;

import org.junit.jupiter.api.Test;
import ru.timmson.jloan.calendar.RussianProductionCalendar;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.timmson.jloan.LoanFactory.annuityLoanBuilder;

public class AnnuityLoanShould {

    @Test
    void calculateAnnuityAmount() {
        final var loan = annuityLoanBuilder()
                .amount(110000)
                .annualInterestRate(12.9)
                .termInMonth(60)
                .build();

        final var annuityPayment = loan.getAnnuityPayment();

        assertEquals(valueOf(2497.21), annuityPayment);
    }

    @Test
    void calculateSchedule() {
        final var loan = annuityLoanBuilder()
                .amount(500000)
                .annualInterestRate(11.5)
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(2018, 10, 25)
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(44301.39), schedule.getPayments().get(12).getAmount());
        assertEquals(valueOf(31684.22), schedule.getOverallInterest());
    }

    @Test
    void calculateScheduleWithEarlyRepayment1() {
        final var loan = annuityLoanBuilder()
                .amount(valueOf(500000))
                .annualInterestRate(11.5)
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(2016, 10, 25)
                .addEarlyRepayment(2016, 12, 24, 440000)
                .productionCalendar(RussianProductionCalendar.getInstance())
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(14, schedule.getPayments().size());
        assertEquals(0, valueOf(2166.20).compareTo(schedule.getPayments().get(12).getAmount()));
        assertEquals(valueOf(10173.53), schedule.getOverallInterest());
    }

    @Test
    void calculateScheduleWithEarlyRepayment2() {
        final var loan = annuityLoanBuilder()
                .amount(500000)
                .annualInterestRate(11.5)
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(2016, 10, 25)
                .addEarlyRepayment(2016, 12, 26, 44000)
                .productionCalendar(RussianProductionCalendar.getInstance())
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(43795.12), schedule.getPayments().get(12).getAmount());
        assertEquals(valueOf(27541.78), schedule.getOverallInterest());
    }


    @Test
    void calculateScheduleWithFixedPayment() {
        final var loan = annuityLoanBuilder()
                .amount(500000)
                .annualInterestRate(11.5)
                .termInMonth(12)
                .paymentAmount(57000)
                .paymentOnDay(25)
                .issueDate(2018, 10, 25)
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(schedule.getTermInMonth(), schedule.getPayments().size() - 1);
        assertEquals(valueOf(11804.06), schedule.getPayments().get((int) schedule.getTermInMonth()).getAmount());
        assertEquals(valueOf(24804.06), schedule.getOverallInterest());
    }

    @Test
    void calculateScheduleWithRussianCalendar() {
        final var loan = annuityLoanBuilder()
                .amount(500000)
                .annualInterestRate(11.5)
                .termInMonth(12)
                .paymentOnDay(5)
                .issueDate(2018, 10, 15)
                .productionCalendar(RussianProductionCalendar.getInstance())
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(42623.17), schedule.getPayments().get(12).getAmount());
        assertEquals(0, valueOf(30006.00).compareTo(schedule.getOverallInterest()));
    }
}
