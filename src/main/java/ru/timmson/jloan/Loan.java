package ru.timmson.jloan;

/**
 * Loan interface
 *
 * @author Artem Krotov
 */
public interface Loan {

    /**
     * Generate schedule
     *
     * @return - loan schedule {@link LoanSchedule}
     */
    LoanSchedule getSchedule();
}
