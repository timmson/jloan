package ru.timmson.jloan;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
    private LocalDate date = LocalDate.MIN;
    @Builder.Default
    private LoanPaymentType paymentType = LoanPaymentType.REGULAR;
    @Builder.Default
    private BigDecimal initialBalance = ZERO;
    @Builder.Default
    private BigDecimal amount = ZERO;
    @Builder.Default
    private BigDecimal annuityAmount = ZERO;
    @Builder.Default
    private BigDecimal interestAmount = ZERO;
    @Builder.Default
    private BigDecimal principalAmount = ZERO;
    @Builder.Default
    private BigDecimal interestRate = ZERO;
    @Builder.Default
    private BigDecimal finalBalance = ZERO;

    protected enum LoanPaymentType {
        REGULAR, EARLY
    }

    @Override
    public String toString() {
        return String.format("| %-12s | %-12s | %-12s | %-12s | %-12s | %-12s |%n", this.date, this.initialBalance, this.amount, this.principalAmount, this.interestAmount, this.finalBalance);
    }
}
