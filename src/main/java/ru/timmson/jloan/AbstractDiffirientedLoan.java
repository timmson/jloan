package ru.timmson.jloan;

import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.TreeMap;

import static java.math.BigDecimal.ZERO;

/**
 * Abstract Loan with diffiriented payments
 *
 * @author Artem Krotov
 */
@SuperBuilder
public abstract class AbstractDiffirientedLoan extends AbstractLoan {

    /**
     * Generate payments
     *
     * @return list of {@link LoanPayment}
     */
    @Override
    protected final LinkedList<LoanPayment> getPayments() {
        final var payments = initPayments();
        final var fixedPrincipalPaymentPart = getFixedPrincipalPaymentPart();
        final var earlyRepayment = new TreeMap<>(getEarlyRepayments());

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

            final var interestPayment = getLoanInterestRate()
                    .calculate(initialBalance, lastPayment.getDate(), date)
                    .add(lastPayment.getInterestAccruedAmount());

            final var principalPayment = (date.isEqual(endDate) ? initialBalance : initialBalance.min(fixedPrincipalPaymentPart));

            lastPayment = LoanPayment
                    .builder()
                    .initialBalance(initialBalance)
                    .date(date)
                    .principalAmount(principalPayment)
                    .interestAmount(interestPayment)
                    .interestAccruedAmount(ZERO)
                    .amount(principalPayment.add(interestPayment))
                    .interestRate(this.annualInterestRate)
                    .finalBalance(initialBalance.subtract(principalPayment))
                    .build();

            payments.add(lastPayment);

        }
        return payments;
    }

    /**
     * Returns amount of principal monthly payment
     *
     * For {@link DiffirientedLoan} it equals portion of loan amount
     * For {@link BubbleLoan} it equals O.
     *
     * @return {@link BigDecimal}
     */
    protected abstract BigDecimal getFixedPrincipalPaymentPart();
}
