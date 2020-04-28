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
@ToString
public class LoanPayment {

    @Builder.Default
    private BigDecimal initialBalance = ZERO;
    @Builder.Default
    private LocalDate date = LocalDate.MIN;
    @Builder.Default
    private BigDecimal amount = ZERO;
    @Builder.Default
    private BigDecimal interestAmount = ZERO;
    @Builder.Default
    private BigDecimal principalAmount = ZERO;
    @Builder.Default
    private BigDecimal interestRate = ZERO;
    @Builder.Default
    private BigDecimal finalBalance = ZERO;

}
