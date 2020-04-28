package ru.timmson.jloan;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface of Loan Builder
 *
 * @param <L> - extends {@link Loan}
 */
public interface LoanBuilder<L extends Loan> {

    LoanBuilder<L> amount(BigDecimal amount);

    LoanBuilder<L> annualInterestRate(BigDecimal annualInterestRate);

    LoanBuilder<L> termInMonth(int termInMonth);

    LoanBuilder<L> paymentOnDay(int paymentOnDay);

    LoanBuilder<L> issueDate(LocalDate issueDate);

    L build();

}
