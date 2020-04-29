package ru.timmson.jloan;

/**
 * LoanFactory returns builder for loans
 *
 * @author Artem Krotov
 */
public class LoanFactory {


    /**
     * Returns AnnuityLoanBuilder
     *
     * @return {@link AnnuityLoan.AnnuityLoanBuilder}
     */
    public static AnnuityLoan.AnnuityLoanBuilder<?, ?> annuityLoanBuilder() {
        return AnnuityLoan.builder();
    }

    /**
     * Returns BubbleLoanBuilder
     *
     * @return {@link BubbleLoan.BubbleLoanBuilder}
     */
    public static BubbleLoan.BubbleLoanBuilder<?, ?> bubbleLoanBuilder() {
        return BubbleLoan.builder();
    }

    /**
     * Returns DiffirientedLoanBuilder
     *
     * @return {@link DiffirientedLoan.DiffirientedLoanBuilder}
     */
    public static DiffirientedLoan.DiffirientedLoanBuilder<?, ?> differentiatedLoanBuilder() {
        return DiffirientedLoan.builder();
    }

}


