package ru.timmson.jloan;

import java.util.Map;

import static ru.timmson.jloan.LoanType.*;

/**
 * LoanFactory
 *
 * @author Artem Krotov
 */
public class LoanFactory {

    private static final Map<LoanType, ? extends LoanBuilder<? extends Loan>> LOANS = Map.of(
            ANNUITY, AnnuityLoan.builder(),
            BUBBLE, BubbleLoan.builder(),
            DIFFERENTIATED, DiffirientedLoan.builder()
    );

    public static LoanBuilder<? extends Loan> annuityLoanBuilder() {
        return LOANS.get(ANNUITY);
    }

    public static LoanBuilder<? extends Loan> bubbleLoanBuilder() {
        return LOANS.get(BUBBLE);
    }

    public static LoanBuilder<? extends Loan> differentiatedLoanBuilder() {
        return LOANS.get(DIFFERENTIATED);
    }

    public static LoanBuilder<? extends Loan> builder(LoanType loanType) {
        return LOANS.get(loanType);
    }

}


