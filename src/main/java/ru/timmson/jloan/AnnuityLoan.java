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
        final var annuityPayment = getAnnuityPayment();
        final var tempEarlyRepayment = new TreeMap<>(getEarlyRepayments());

        var i = 0;
        var date = issueDate;
        while (i++ < termInMonth && payments.get(payments.size() - 1).getFinalBalance().compareTo(ZERO) > 0) {
            date = getNextWorkingDate(date.plusMonths(1).withDayOfMonth(paymentOnDay));

            if (!tempEarlyRepayment.isEmpty() && tempEarlyRepayment.firstKey().isBefore(date)) {
                final var earlyRepaymentEntry = tempEarlyRepayment.pollFirstEntry();
            }

            final var initialBalance = payments.get(payments.size() - 1).getFinalBalance();
            final var interestPayment = getLoanInterestRate().calculate(initialBalance, payments.get(payments.size() - 1).getDate(), date);

            final var currentAnnuityPayment = termInMonth - i > 0 ? getAnnuityPayment(initialBalance, termInMonth - i, annualInterestRate) : ZERO;
            final var payment = ((i == termInMonth || initialBalance.add(interestPayment).compareTo(annuityPayment) <= 0) ? initialBalance.add(interestPayment) : annuityPayment);
            final var principalPayment = payment.subtract(interestPayment);

            payments.add(LoanPayment
                    .builder()
                    .initialBalance(initialBalance)
                    .date(date)
                    .amount(payment)
                    .principalAmount(principalPayment)
                    .interestAmount(interestPayment)
                    .annuityAmount(currentAnnuityPayment)
                    .interestRate(this.annualInterestRate)
                    .finalBalance(initialBalance.subtract(principalPayment))
                    .build()
            );

        }
        return payments;
    }

}
