package ru.timmson.jloan;

import ru.timmson.jloan.calendar.ProductionCalendar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;

/**
 * {@link AbstractLoan} class
 *
 * @author Artem Krotov
 */
abstract class AbstractLoan implements Loan {

    private final LoanInterestRate interestRate;
    protected BigDecimal amount;
    protected BigDecimal annualInterestRate;
    protected long termInMonth;
    protected int paymentOnDay;
    protected LocalDate issueDate;
    protected ProductionCalendar productionCalendar;
    protected Map<LocalDate, BigDecimal> earlyRepayments;
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

    protected final LinkedList<LoanPayment> initPayments() {
        final var payments = new LinkedList<LoanPayment>();
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
    protected abstract LinkedList<LoanPayment> getPayments();

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
        private final Map<LocalDate, BigDecimal> earlyRepayments = new HashMap<>();
        private BigDecimal amount;
        private BigDecimal annualInterestRate;
        private long termInMonth;
        private int paymentOnDay;
        private LocalDate issueDate;
        private ProductionCalendar productionCalendar;
        private LoanInterestRate interestRate;

        /**
         * Sets loan amount
         *
         * @param amount, eg 15000
         * @return {@link B}
         */
        public B amount(BigDecimal amount) {
            this.amount = amount;
            return self();
        }

        /**
         * Sets loan amount
         *
         * @param amount, eg 15000
         * @return {@link B}
         */
        public B amount(double amount) {
            return amount(valueOf(amount));
        }

        public B annualInterestRate(BigDecimal annualInterestRate) {
            this.annualInterestRate = annualInterestRate;
            this.interestRate = new LoanInterestRate(annualInterestRate);
            return self();
        }

        public B annualInterestRate(double annualInterestRate) {
            return annualInterestRate(valueOf(annualInterestRate));
        }

        public B termInMonth(long termInMonth) {
            this.termInMonth = termInMonth;
            return self();
        }

        public B paymentOnDay(int paymentOnDay) {
            this.paymentOnDay = paymentOnDay;
            return self();
        }

        /**
         * Sets issue date
         *
         * @param issueDate {@link LocalDate} , e.g 2020-10-15
         * @return {@link B}
         */
        public B issueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return self();
        }

        /**
         * Sets issue date
         *
         * @param year,       e.g 2020
         * @param month,      e.g 10 (October)
         * @param dayOfMonth, eg 15
         * @return {@link B}
         */
        public B issueDate(int year, int month, int dayOfMonth) {
            return issueDate(of(year, month, dayOfMonth));
        }

        public B productionCalendar(ProductionCalendar productionCalendar) {
            this.productionCalendar = productionCalendar;
            return self();
        }

        /**
         * Adds early repayment
         *
         * @param date    {@link LocalDate} , e.g 2020-10-15
         * @param amount, eg 15000
         * @return {@link B}
         */
        public B addEarlyRepayment(LocalDate date, BigDecimal amount) {
            synchronized (earlyRepayments) {
                earlyRepayments.put(date, amount.add(earlyRepayments.getOrDefault(date, ZERO)));
            }
            return self();
        }

        /**
         * Adds early repayment
         *
         * @param year,       e.g 2020
         * @param month,      e.g 10 (October)
         * @param dayOfMonth, eg 15
         * @param amount,     eg 15000
         * @return {@link B}
         */
        public B addEarlyRepayment(int year, int month, int dayOfMonth, double amount) {
            return addEarlyRepayment(of(year, month, dayOfMonth), valueOf(amount));
        }

        protected abstract B self();

        public abstract C build();

    }
}
