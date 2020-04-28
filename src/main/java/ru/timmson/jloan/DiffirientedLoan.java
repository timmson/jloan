package ru.timmson.jloan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;

/**
 * Loan with diffiriented payments
 * <p>
 * Monthly payment consists of equal portion of loan amount and interest amount of each month
 *
 * @author Artem Krotov
 */
public class DiffirientedLoan extends AbstractDiffirientedLoan {

    static DiffirientedAbstractLoanBuilder builder() {
        return new DiffirientedAbstractLoanBuilder(new DiffirientedLoan());
    }

    /**
     * Returns amount of principal monthly payment
     *
     * For {@link DiffirientedLoan} it equals portion of loan amount
     *
     * @return {@link BigDecimal}
     */
    protected BigDecimal getFixedPrincipalPaymentPart() {
        return amount.divide(valueOf(termInMonth), MathContext.DECIMAL32).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Build {@code DiffirientedLoan}
     */
    public static class DiffirientedAbstractLoanBuilder extends AbstractLoanBuilder<DiffirientedLoan> {

        public DiffirientedAbstractLoanBuilder(DiffirientedLoan loan) {
            super(loan);
        }
    }

}

