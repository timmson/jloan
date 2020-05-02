package ru.timmson.jloan;

import ru.timmson.jloan.calendar.ProductionCalendar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.math.BigDecimal.ZERO;

/**
 * {@link AbstractLoan} class
 *
 * @author Artem Krotov
 */
abstract class AbstractLoan implements Loan {

    protected BigDecimal amount;
    protected BigDecimal annualInterestRate;
    protected long termInMonth;
    protected int paymentOnDay;
    protected LocalDate issueDate;
    protected ProductionCalendar productionCalendar;
    protected Map<LocalDate, BigDecimal> earlyRepayments;

    private LoanInterestRate interestRate;
    private LoanSchedule loanSchedule;

    protected AbstractLoan(AbstractLoanBuilder<?, ?> b) {
        this.amount = b.amount;
        this.annualInterestRate = b.annualInterestRate;
        this.termInMonth = b.termInMonth;
        this.paymentOnDay = b.paymentOnDay;
        this.issueDate = b.issueDate;
        this.productionCalendar = b.productionCalendar;
        this.interestRate = b.interestRate;
        this.earlyRepayments = b.earlyRepayments;
    }

    protected final List<LoanPayment> initPayments() {
        final var payments = new ArrayList<LoanPayment>();
        payments.add(LoanPayment
                .builder()
                .date(this.issueDate)
                .interestRate(this.annualInterestRate)
                .finalBalance(this.amount)
                .build());
        return payments;
    }

    protected LoanInterestRate getLoanInterestRate() {
        return interestRate;
    }

    protected Map<LocalDate, BigDecimal> getEarlyRepayments() {
        return Collections.unmodifiableMap(earlyRepayments);
    }

    /**
     * Generates schedule.
     * <p>
     * Caches it for next usage.
     *
     * @return - loan schedule {@link LoanSchedule}
     */
    @Override
    public final LoanSchedule getSchedule() {
        synchronized (this) {
            if (loanSchedule == null) {
                loanSchedule = LoanSchedule.build(getPayments());
            }
        }
        return loanSchedule;
    }

    /**
     * Generates payments
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

    public static abstract class AbstractLoanBuilder<C extends AbstractLoan, B extends AbstractLoan.AbstractLoanBuilder<C, B>> {
        private final Map<LocalDate, BigDecimal> earlyRepayments = new TreeMap<>();
        private BigDecimal amount;
        private BigDecimal annualInterestRate;
        private long termInMonth;
        private int paymentOnDay;
        private LocalDate issueDate;
        private ProductionCalendar productionCalendar;
        private LoanInterestRate interestRate;

        public B amount(BigDecimal amount) {
            this.amount = amount;
            return self();
        }

        public B annualInterestRate(BigDecimal annualInterestRate) {
            this.annualInterestRate = annualInterestRate;
            this.interestRate = new LoanInterestRate(annualInterestRate);
            return self();
        }

        public B termInMonth(long termInMonth) {
            this.termInMonth = termInMonth;
            return self();
        }

        public B paymentOnDay(int paymentOnDay) {
            this.paymentOnDay = paymentOnDay;
            return self();
        }

        public B issueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return self();
        }

        public B productionCalendar(ProductionCalendar productionCalendar) {
            this.productionCalendar = productionCalendar;
            return self();
        }

        public B addEarlyRepayment(LocalDate date, BigDecimal amount) {
            synchronized (earlyRepayments) {
                earlyRepayments.put(date, amount.add(earlyRepayments.getOrDefault(date, ZERO)));
            }
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "AbstractLoan.AbstractLoanBuilder(amount=" + this.amount + ", annualInterestRate=" + this.annualInterestRate + ", termInMonth=" + this.termInMonth + ", paymentOnDay=" + this.paymentOnDay + ", issueDate=" + this.issueDate + ", productionCalendar=" + this.productionCalendar + ")";
        }
    }
}
