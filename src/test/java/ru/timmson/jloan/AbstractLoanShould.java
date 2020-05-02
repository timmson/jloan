package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.timmson.jloan.LoanFactory.annuityLoanBuilder;

public class AbstractLoanShould {

    @Test
    void serveInterestRate() {
        final var loan = annuityLoanBuilder()
                .annualInterestRate(valueOf(15.5))
                .build();

        assertEquals(new LoanInterestRate(valueOf(15.5)), loan.getLoanInterestRate());
    }

    @Test
    void serveEarlyRepayment() {
        final var earlyRepaymentDate = of(2018, 5, 10);

        final var loan = annuityLoanBuilder()
                .addEarlyRepayment(earlyRepaymentDate, valueOf(100))
                .addEarlyRepayment(earlyRepaymentDate, valueOf(100))
                .addEarlyRepayment(earlyRepaymentDate.plusDays(1), valueOf(100))
                .build();

        assertEquals(valueOf(200), loan.getEarlyRepayments().get(earlyRepaymentDate));
    }
}
