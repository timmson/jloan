package ru.timmson.jloan;

import java.math.BigDecimal;
import java.util.List;

import static java.math.MathContext.DECIMAL32;
import static java.math.RoundingMode.HALF_UP;

/**
 * Loan with equals ("annuity") payments
 * <p>
 * Monthly payment are equal and consists of principal amount and and interest amount of each month
 *
 * @author Artem Krotov
 */
public class AnnuityLoan extends AbstractLoan {

    static AnnuityAbstractLoanBuilder builder() {
        return new AnnuityAbstractLoanBuilder(new AnnuityLoan());
    }

    /**
     * Returns annuity payment amount
     *
     * @return {@link BigDecimal}
     */
    public BigDecimal getAnnuityPayment() {
        final var monthlyInterestRate = this.interestRate.getAnnualInterestRate().divide(BigDecimal.valueOf(100 * 12), DECIMAL32);
        final var ip1 = (monthlyInterestRate.add(BigDecimal.ONE)).pow(this.termInMonth);
        return this.amount.multiply(monthlyInterestRate.multiply(ip1).divide(ip1.subtract(BigDecimal.ONE), DECIMAL32)).setScale(2, HALF_UP);
    }


    /**
     * Generate payments
     *
     * @return  list of {@link LoanPayment}
     */
    @Override
    protected List<LoanPayment> getPayments() {
        final var payments = initPayments();
        final var annuityPayment = getAnnuityPayment();

        var i = 0;
        var date = issueDate;
        while (i++ < termInMonth) {
            date = getNextWorkingDate(date.plusMonths(1).withDayOfMonth(paymentOnDay));

            final var initialBalance = payments.get(payments.size() - 1).getFinalBalance();
            final var interestPayment = interestRate.calculate(initialBalance, payments.get(payments.size() - 1).getDate(), date);

            final var payment = ((i == termInMonth) ? initialBalance.add(interestPayment) : annuityPayment);
            final var principalPayment = payment.subtract(interestPayment);

            payments.add(LoanPayment
                    .builder()
                    .initialBalance(initialBalance)
                    .date(date)
                    .amount(payment)
                    .principalAmount(principalPayment)
                    .interestAmount(interestPayment)
                    .annuityAmount(annuityPayment)
                    .interestRate(this.interestRate.getAnnualInterestRate())
                    .finalBalance(initialBalance.subtract(principalPayment))
                    .build()
            );

        }
        return payments;
    }

    public static class AnnuityAbstractLoanBuilder extends AbstractLoanBuilder<AnnuityLoan> {

        protected AnnuityAbstractLoanBuilder(AnnuityLoan loan) {
            super(loan);
        }

    }
}
