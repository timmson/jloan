package ru.timmson.jloan;

import lombok.experimental.SuperBuilder;

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
@SuperBuilder
public class BubbleLoan extends AbstractDiffirientedLoan {

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

}
