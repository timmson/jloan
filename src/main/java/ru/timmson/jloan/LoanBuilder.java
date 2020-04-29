package ru.timmson.jloan;

import ru.timmson.jloan.calendar.ProductionCalendar;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface of Loan Builder
 *
 */
public interface LoanBuilder<L extends Loan> {

    LoanBuilder<L> amount(BigDecimal amount);

    LoanBuilder<L> annualInterestRate(BigDecimal annualInterestRate);

    LoanBuilder<L> termInMonth(long termInMonth);

    LoanBuilder<L> paymentOnDay(int paymentOnDay);

    LoanBuilder<L> issueDate(LocalDate issueDate);

    LoanBuilder<L> productionCalendar(ProductionCalendar productionCalendar);

    L build();

}
