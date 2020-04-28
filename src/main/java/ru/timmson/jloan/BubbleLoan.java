package ru.timmson.jloan;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

/**
 * Loan with diffiriented payments
 *
 * Monthly payment consists of interest amount of each month only except the last one.
 *
 * Payment at the last month consists of interest amount of that month and full loan amount
 */
public class BubbleLoan extends AbstractDiffirientedLoan {

    public static BubbleLoanBuilder builder() {
        return new BubbleLoanBuilder(new BubbleLoan());
    }

    /**
     * Returns amount of principal monthly payment
     *
     * For {@link BubbleLoan} it equals O.
     *
     * @return {@link BigDecimal}
     */
    @Override
    protected BigDecimal getFixedPrincipalPaymentPart() {
        return ZERO;
    }

    /**
     * Build {@code BubbleLoan}
     */
    public static class BubbleLoanBuilder extends LoanBuilder<BubbleLoan> {

        public BubbleLoanBuilder(BubbleLoan loan) {
            super(loan);
        }
    }
}
