package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        final var loan = AnnuityLoan
                .builder()
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
}
