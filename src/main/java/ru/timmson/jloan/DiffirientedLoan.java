package ru.timmson.jloan;

import lombok.ToString;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;

/**
 * Loan with diffiriented payments
 *
 * Monthly payment consists of equal portion of loan amount and interest amount of each month
 *
 */
public class DiffirientedLoan extends AbstractDiffirientedLoan {

    public static DiffirientedLoanBuilder builder() {
        return new DiffirientedLoanBuilder(new DiffirientedLoan());
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
    public static class DiffirientedLoanBuilder extends LoanBuilder<DiffirientedLoan> {

        public DiffirientedLoanBuilder(DiffirientedLoan loan) {
            super(loan);
        }
    }

}

