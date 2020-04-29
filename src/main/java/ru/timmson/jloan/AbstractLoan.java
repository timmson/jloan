package ru.timmson.jloan;

import ru.timmson.jloan.calendar.ProductionCalendar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractLoan} class
 *
 * @author Artem Krotov
 */
abstract class AbstractLoan implements Loan {
    protected BigDecimal amount;
    protected LoanInterestRate interestRate;
    protected int termInMonth;
    protected int paymentOnDay;
    protected LocalDate issueDate;
    protected ProductionCalendar productionCalendar;

    protected AbstractLoan() {
    }

    protected final List<LoanPayment> initPayments() {
        final var payments = new ArrayList<LoanPayment>();
        payments.add(LoanPayment
                .builder()
                .date(this.issueDate)
                .interestRate(this.interestRate.getAnnualInterestRate())
                .finalBalance(this.amount)
                .build());
        return payments;
    }

    /**
     * Generate schedule
     *
     * @return - loan schedule {@link LoanSchedule}
     */
    @Override
    public final LoanSchedule getSchedule() {
        return LoanSchedule.build(getPayments());
    }

    /**
     * Generate payments
     *
     * @return list of {@link LoanPayment}
     */
    protected abstract List<LoanPayment> getPayments();

    /**
     * Returns next working date if ProductionCalendar is not null
     *
     * @param date - given date
     * @return the closest working date
     */
    protected LocalDate getNextWorkingDate(LocalDate date) {
        if (productionCalendar != null) {
            date = productionCalendar.getNextWorkDay(date);
        }
        return date;
    }

    protected static class AbstractLoanBuilder<L extends AbstractLoan> implements LoanBuilder<L> {

        protected final L loan;

        protected AbstractLoanBuilder(L loan) {
            this.loan = loan;
        }

        @Override
        public AbstractLoanBuilder<L> amount(BigDecimal amount) {
            this.loan.amount = amount;
            return this;
        }

        @Override
        public AbstractLoanBuilder<L> annualInterestRate(BigDecimal annualInterestRate) {
            this.loan.interestRate = new LoanInterestRate(annualInterestRate);
            return this;
        }

        @Override
        public AbstractLoanBuilder<L> termInMonth(int termInMonth) {
            this.loan.termInMonth = termInMonth;
            return this;
        }

        @Override
        public AbstractLoanBuilder<L> paymentOnDay(int paymentOnDay) {
            this.loan.paymentOnDay = paymentOnDay;
            return this;
        }

        @Override
        public AbstractLoanBuilder<L> issueDate(LocalDate issueDate) {
            this.loan.issueDate = issueDate;
            return this;
        }

        @Override
        public AbstractLoanBuilder<L> productionCalendar(ProductionCalendar productionCalendar) {
            this.loan.productionCalendar = productionCalendar;
            return this;
        }

        public L build() {
            return this.loan;
        }

    }
}
