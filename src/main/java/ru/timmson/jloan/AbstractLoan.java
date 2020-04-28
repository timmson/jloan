package ru.timmson.jloan;

import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
abstract class AbstractLoan implements Loan {
    protected BigDecimal amount;
    protected LoanInterestRate interestRate;
    protected int termInMonth;
    protected int paymentOnDay;
    protected LocalDate issueDate;

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
     * @return  list of {@link LoanPayment}
     */
    protected abstract List<LoanPayment> getPayments();

    protected static class LoanBuilder<L extends AbstractLoan> {

        protected final L loan;

        protected LoanBuilder(L loan) {
            this.loan = loan;
        }

        public LoanBuilder<L> amount(BigDecimal amount) {
            this.loan.amount = amount;
            return this;
        }

        public LoanBuilder<L> annualInterestRate(BigDecimal annualInterestRate) {
            this.loan.interestRate = new LoanInterestRate(annualInterestRate);
            return this;
        }

        public LoanBuilder<L> termInMonth(int termInMonth) {
            this.loan.termInMonth = termInMonth;
            return this;
        }

        public LoanBuilder<L> paymentOnDay(int paymentOnDay) {
            this.loan.paymentOnDay = paymentOnDay;
            return this;
        }

        public LoanBuilder<L> issueDate(LocalDate issueDate) {
            this.loan.issueDate = issueDate;
            return this;
        }

        public L build() {
            return this.loan;
        }

    }
}
