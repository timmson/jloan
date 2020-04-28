package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BubbleLoanShould {

    @Test
    void calculateSchedule() {
        final var loan = BubbleLoan
                .builder()
                .amount(valueOf(50000))
                .annualInterestRate(valueOf(11.5))
                .termInMonth(12)
                .paymentOnDay(25)
                .issueDate(of(2016, 10, 25))
                .build();

        final var schedule = loan.getSchedule();

        schedule.getPayments().forEach(System.out::println);

        assertEquals(13, schedule.getPayments().size());
        assertEquals(ZERO, schedule.getPayments().get(1).getPrincipalAmount());
        assertEquals(valueOf(50000), schedule.getPayments().get(12).getPrincipalAmount());
        assertEquals(valueOf(5747.13), schedule.getOverallInterest());
    }
}