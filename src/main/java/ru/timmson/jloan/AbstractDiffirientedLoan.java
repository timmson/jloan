package ru.timmson.jloan;

import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

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
     * @return  list of {@link LoanPayment}
     */
    @Override
    protected final List<LoanPayment> getPayments() {
        final var payments = initPayments();
        final var fixedPrincipalPaymentPart = getFixedPrincipalPaymentPart();

        var i = 0;
        var date = issueDate;
        while (i++ < termInMonth && payments.get(payments.size() - 1).getFinalBalance().compareTo(ZERO) > 0) {
            date = getNextWorkingDate(date.plusMonths(1).withDayOfMonth(paymentOnDay));

            final var initialBalance = payments.get(payments.size() - 1).getFinalBalance();
            final var interestPayment = getLoanInterestRate().calculate(initialBalance, payments.get(payments.size() - 1).getDate(), date);
            final var principalPayment = (i == termInMonth ? initialBalance : fixedPrincipalPaymentPart);

            payments.add(LoanPayment
                    .builder()
                    .initialBalance(initialBalance)
                    .date(date)
                    .principalAmount(principalPayment)
                    .interestAmount(interestPayment)
                    .amount(principalPayment.add(interestPayment))
                    .interestRate(this.annualInterestRate)
                    .finalBalance(initialBalance.subtract(principalPayment))
                    .build()
            );

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
