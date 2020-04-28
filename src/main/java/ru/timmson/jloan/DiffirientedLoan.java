package ru.timmson.jloan;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class DiffirientedLoan {

    private BigDecimal amount;

    private BigDecimal annualInterestRate;

    private int termInMonth;

    private int paymentOnDay;

    private LocalDate issueDate;

    public LoanSchedule getSchedule() {
        return new LoanSchedule();
    }

}

