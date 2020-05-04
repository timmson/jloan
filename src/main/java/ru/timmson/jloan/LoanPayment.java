package ru.timmson.jloan;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.BigDecimal.ZERO;

/**
 * Loan payment DTO
 *
 * @author Artem Krotov
 */
@Getter
@Builder
public class LoanPayment {

    @Builder.Default
    private final LocalDate date = LocalDate.MIN;
    @Builder.Default
    private final LoanPaymentType paymentType = LoanPaymentType.REGULAR;
    @Builder.Default
    private final BigDecimal initialBalance = ZERO;
    @Builder.Default
    private final BigDecimal amount = ZERO;
    @Builder.Default
    private final BigDecimal annuityAmount = ZERO;
    @Builder.Default
    private final BigDecimal principalAmount = ZERO;
    @Builder.Default
    private final BigDecimal interestAmount = ZERO;
    @Builder.Default
    private final BigDecimal interestAccruedAmount = ZERO;
    @Builder.Default
    private final BigDecimal interestRate = ZERO;
    @Builder.Default
    private final BigDecimal finalBalance = ZERO;

    @Override
    public String toString() {
        return String.format(
                "| %-12s | %-12s | %-12s | %-12s | %-12s | %-12s |%n",
                this.date, this.initialBalance, this.amount,
                this.principalAmount, this.interestAmount, this.finalBalance);
    }

}
