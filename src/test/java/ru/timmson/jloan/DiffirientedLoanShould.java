package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

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
                .issueDate(of(2016, 10, 25))
                .build();

        final var schedule = loan.getSchedule();

        assertEquals(13, schedule.getPayments().size());
        assertEquals(valueOf(4206.01), schedule.getPayments().get(12).getAmount());
        assertEquals(valueOf(3111.18), schedule.getOverallInterest());
    }
}
