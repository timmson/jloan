package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiffirientedLoanShould {

    @Test
    void calculateSchedule () {
        final var loan = DiffirientedLoan
                .builder()
                .amount(valueOf(5000))
                .annualInterestRate(valueOf(11.5))
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(of(2016, 10, 25))
                .build();

        BigDecimal result = loan.getSchedule().getOverallInterest();

        assertEquals(valueOf(3111.18), result);
    }
}
