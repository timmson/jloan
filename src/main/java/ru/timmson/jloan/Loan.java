package ru.timmson.jloan;

/**
 *  Loan interface
 */
public interface Loan {

    /**
     * Generate schedule
     *
     * @return - loan schedule {@link LoanSchedule}
     */
    LoanSchedule getSchedule();
}
