package ru.timmson.jloan;

/**
 * Types of loans
 *
 * @author Artem Krotov
 */
public enum LoanType {

    /**
     * {@link AnnuityLoan}
     */
    ANNUITY("ANNUITY"),

    /**
     * {@link BubbleLoan}
     */
    BUBBLE("BUBBLE"),

    /**
     * {@link DiffirientedLoan}
     */
    DIFFERENTIATED("DIFFERENTIATED");


    private final String value;

    LoanType(String name) {
        this.value = name;
    }

    public String value() {
        return value;
    }
}
