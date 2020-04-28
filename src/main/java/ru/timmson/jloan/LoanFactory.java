package ru.timmson.jloan;

import java.util.Map;

/**
 * LoanFactory
 *
 * @author Artem Krotov
 */
public class LoanFactory {

    private static final Map<LoanType, ? extends LoanBuilder<? extends Loan>> LOANS = Map.of(
            LoanType.ANNUITY, AnnuityLoan.builder(),
            LoanType.BUBBLE, BubbleLoan.builder(),
            LoanType.DIFFERENTIATED, DiffirientedLoan.builder()
    );

    public static LoanBuilder<? extends Loan> build(LoanType loanType) {
        return LOANS.get(loanType);
    }

}


