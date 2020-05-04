package ru.timmson.jloan;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Optional;
import java.util.TreeMap;

import static java.math.BigDecimal.*;
import static java.math.MathContext.DECIMAL32;
import static java.math.RoundingMode.HALF_UP;
import static java.time.temporal.ChronoUnit.MONTHS;

/**
 * Loan with equals ("annuity") payments
 * <p>
 * Monthly payment are equal and consists of principal amount and and interest amount of each month
 *
 * @author Artem Krotov
 */
public class AnnuityLoan extends AbstractLoan {

    protected BigDecimal paymentAmount;

    protected AnnuityLoan(AnnuityLoanBuilder<?, ?> b) {
        super(b);
        this.paymentAmount = b.paymentAmount;
    }

    public static AnnuityLoanBuilder<?, ?> builder() {
        return new AnnuityLoanBuilderImpl();
    }

    /**
     * Returns annuity payment amount
     *
     * @return {@link BigDecimal}
     */
    public BigDecimal getAnnuityPayment() {
        return Optional.ofNullable(paymentAmount).orElse(getAnnuityPayment(this.amount, this.termInMonth, this.annualInterestRate));
    }

    protected BigDecimal getAnnuityPayment(BigDecimal amount, long termInMonth, BigDecimal annulInterestRate) {
        final var monthlyInterestRate = annulInterestRate.divide(valueOf(100 * 12), DECIMAL32);
        final var ip1 = (monthlyInterestRate.add(ONE)).pow((int) termInMonth);
        return amount.multiply(monthlyInterestRate.multiply(ip1).divide(ip1.subtract(ONE), DECIMAL32)).setScale(2, HALF_UP);
    }


    /**
     * Generate payments
     *
     * @return list of {@link LoanPayment}
     */
    @Override
    protected LinkedList<LoanPayment> getPayments() {
        final var payments = initPayments();
        final var earlyRepayment = new TreeMap<>(getEarlyRepayments());
        var annuityPayment = getAnnuityPayment();

        var date = issueDate;
        final var endDate = issueDate.plusMonths(termInMonth).withDayOfMonth(paymentOnDay);

        var lastPayment = payments.getLast();
        while (date.isBefore(endDate) && lastPayment.getFinalBalance().compareTo(ZERO) > 0) {
            date = getNextWorkingDate(date.plusMonths(1).withDayOfMonth(paymentOnDay));

            if (!earlyRepayment.isEmpty()) {
                if (earlyRepayment.firstKey().compareTo(date) < 0) {
                    final var initialBalance = lastPayment.getFinalBalance();

                    final var earlyRepaymentEntry = earlyRepayment.pollFirstEntry();
                    final var interestAccrued = getLoanInterestRate()
                            .calculate(
                                    initialBalance,
                                    lastPayment.getDate(),
                                    earlyRepaymentEntry.getKey()
                            ).add(lastPayment.getInterestAccruedAmount());

                    final var payment = initialBalance.min(earlyRepaymentEntry.getValue());

                    lastPayment = LoanPayment
                            .builder()
                            .paymentType(LoanPaymentType.EARLY)
                            .initialBalance(initialBalance)
                            .date(earlyRepaymentEntry.getKey())
                            .amount(payment)
                            .principalAmount(payment)
                            .interestAmount(ZERO)
                            .interestAccruedAmount(interestAccrued)
                            .interestRate(this.annualInterestRate)
                            .finalBalance(initialBalance.subtract(payment))
                            .build();

                    payments.add(lastPayment);
                }
            }

            final var initialBalance = lastPayment.getFinalBalance();
            final var currentAnnuityPayment = date.isBefore(endDate) ?
                    getAnnuityPayment(
                            initialBalance,
                            MONTHS.between(date.withDayOfMonth(1), endDate.withDayOfMonth(1)),
                            annualInterestRate
                    ) : ZERO;

            annuityPayment = lastPayment.getPaymentType().equals(LoanPaymentType.EARLY) ?
                    currentAnnuityPayment : annuityPayment;

            var interestPayment = getLoanInterestRate()
                    .calculate(initialBalance, lastPayment.getDate(), date)
                    .add(lastPayment.getInterestAccruedAmount());

            var principalPayment = ZERO;

            if (date.isEqual(endDate)) {
                principalPayment = initialBalance;
            }

            if (interestPayment.compareTo(annuityPayment) < 0) {
                principalPayment = initialBalance.min(annuityPayment.subtract(interestPayment));
            }

            final var payment = principalPayment.add(interestPayment);

            lastPayment = LoanPayment
                    .builder()
                    .initialBalance(initialBalance)
                    .date(date)
                    .amount(payment)
                    .annuityAmount(currentAnnuityPayment)
                    .principalAmount(principalPayment)
                    .interestAmount(interestPayment)
                    .interestAccruedAmount(ZERO)
                    .interestRate(this.annualInterestRate)
                    .finalBalance(initialBalance.subtract(principalPayment))
                    .build();

            payments.add(lastPayment);

        }
        return payments;
    }

    public static abstract class AnnuityLoanBuilder<C extends AnnuityLoan, B extends AnnuityLoanBuilder<C, B>> extends AbstractLoanBuilder<C, B> {
        private BigDecimal paymentAmount;

        /**
         * Set fixed payment amount
         *
         * @param paymentAmount - fixed payment amount
         * @return {@link B}
         */
        public B paymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
            return self();
        }

        /**
         * Set fixed payment amount
         *
         * @param paymentAmount - fixed payment amount
         * @return {@link B}
         */
        public B paymentAmount(double paymentAmount) {
            return paymentAmount(valueOf(paymentAmount));
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "AnnuityLoan.AnnuityLoanBuilder(super=" + super.toString() + ", paymentAmount=" + this.paymentAmount + ")";
        }
    }

    private static final class AnnuityLoanBuilderImpl extends AnnuityLoanBuilder<AnnuityLoan, AnnuityLoanBuilderImpl> {
        private AnnuityLoanBuilderImpl() {
        }

        protected AnnuityLoan.AnnuityLoanBuilderImpl self() {
            return this;
        }

        public AnnuityLoan build() {
            return new AnnuityLoan(this);
        }
    }
}
