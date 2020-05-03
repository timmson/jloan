package ru.timmson.jloan;

import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static java.math.BigDecimal.*;
import static java.math.MathContext.DECIMAL32;
import static java.math.RoundingMode.HALF_UP;

/**
 * Loan with equals ("annuity") payments
 * <p>
 * Monthly payment are equal and consists of principal amount and and interest amount of each month
 *
 * @author Artem Krotov
 */
@SuperBuilder
public class AnnuityLoan extends AbstractLoan {

    protected BigDecimal paymentAmount;

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
    protected List<LoanPayment> getPayments() {
        final var payments = initPayments();
        final var tempEarlyRepayment = new TreeMap<>(getEarlyRepayments());
        var annuityPayment = getAnnuityPayment();

        var i = 0;
        var date = issueDate;
        while (i++ < termInMonth && payments.get(payments.size() - 1).getFinalBalance().compareTo(ZERO) > 0) {
            date = getNextWorkingDate(date.plusMonths(1).withDayOfMonth(paymentOnDay));

            if (!tempEarlyRepayment.isEmpty()) {
                if (tempEarlyRepayment.firstKey().compareTo(date) < 0) {
                    final var initialBalance = payments.get(payments.size() - 1).getFinalBalance();

                    final var earlyRepaymentEntry = tempEarlyRepayment.pollFirstEntry();
                    final var interestAccrued = getLoanInterestRate()
                            .calculate(
                                    initialBalance,
                                    payments.get(payments.size() - 1).getDate(),
                                    earlyRepaymentEntry.getKey()
                            ).add(payments.get(payments.size() - 1).getInterestAccruedAmount());

                    final var payment = initialBalance.min(earlyRepaymentEntry.getValue());
                    payments.add(LoanPayment
                            .builder()
                            .paymentType(LoanPayment.LoanPaymentType.EARLY)
                            .initialBalance(initialBalance)
                            .date(earlyRepaymentEntry.getKey())
                            .amount(payment)
                            .principalAmount(payment)
                            .interestAmount(ZERO)
                            .interestAccruedAmount(interestAccrued)
                            .interestRate(this.annualInterestRate)
                            .finalBalance(initialBalance.subtract(payment))
                            .build()
                    );
                }
            }

            final var initialBalance = payments.get(payments.size() - 1).getFinalBalance();
            final var currentAnnuityPayment = termInMonth - i > 0 ? getAnnuityPayment(initialBalance, termInMonth - i, annualInterestRate) : ZERO;

            annuityPayment = payments.get(payments.size() - 1).getPaymentType().equals(LoanPayment.LoanPaymentType.EARLY) ? currentAnnuityPayment : annuityPayment;

            var interestPayment = getLoanInterestRate()
                    .calculate(initialBalance, payments.get(payments.size() - 1).getDate(), date)
                    .add(payments.get(payments.size() - 1).getInterestAccruedAmount());

            final var interestAccrued = interestPayment.compareTo(annuityPayment) > 0 ? interestPayment.subtract(annuityPayment) : ZERO;
            interestPayment = interestPayment.compareTo(annuityPayment) > 0 ? annuityPayment : interestPayment;

            final var principalPayment = (i == termInMonth ? initialBalance : initialBalance.min(annuityPayment.subtract(interestPayment)));
            final var payment = principalPayment.add(interestPayment);

            payments.add(LoanPayment
                    .builder()
                    .initialBalance(initialBalance)
                    .date(date)
                    .amount(payment)
                    .annuityAmount(currentAnnuityPayment)
                    .principalAmount(principalPayment)
                    .interestAmount(interestPayment)
                    .interestAccruedAmount(interestAccrued)
                    .interestRate(this.annualInterestRate)
                    .finalBalance(initialBalance.subtract(principalPayment))
                    .build()
            );

        }
        return payments;
    }

}
