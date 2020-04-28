package ru.timmson.jloan;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

/**
 * Loan with diffiriented payments
 * <p>
 * Monthly payment consists of interest amount of each month only except the last one.
 * <p>
 * Payment at the last month consists of interest amount of that month and full loan amount.
 *
 * @author Artem Krotov
 */
public class BubbleLoan extends AbstractDiffirientedLoan {

    static BubbleAbstractLoanBuilder builder() {
        return new BubbleAbstractLoanBuilder(new BubbleLoan());
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
    public static class BubbleAbstractLoanBuilder extends AbstractLoanBuilder<BubbleLoan> {

        public BubbleAbstractLoanBuilder(BubbleLoan loan) {
            super(loan);
        }
    }
}
