package ru.timmson.jloan;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.BigDecimal.valueOf;
import static java.math.MathContext.DECIMAL32;
import static java.math.RoundingMode.HALF_UP;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * Use {@link LoanInterestRate} for interest calculation
 *
 * @author Artem Krotov
 */
class LoanInterestRate {

    private final BigDecimal interestRate;
    private final BigDecimal annualInterestRate;

    /**
     * Creates {@link LoanInterestRate}
     *
     * @param annualInterestRate - annual interest rate, e.g. 14.99
     */
    public LoanInterestRate(BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
        this.interestRate = annualInterestRate.divide(valueOf(100), DECIMAL32);
    }

    /**
     * Returns annual interest rate
     *
     * @return - annual interest rate, e.g. 14.99
     */
    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    /**
     * Calculate interest for period of days in one year.
     *
     * @param amount     - amount of loan, e.g. 5000.00
     * @param days       - period in days, e.g. 25
     * @param isLeapYear - true or false,
     * @return - interest
     */
    public BigDecimal calculate(BigDecimal amount, long days, boolean isLeapYear) {
        return this.interestRate
                .multiply(valueOf(days)).divide(valueOf(isLeapYear ? 366 : 365), DECIMAL32)
                .multiply(amount).setScale(2, HALF_UP);
    }

    /**
     * Calculate interest for period between two dates.
     *
     * @param amount - amount of loan, e.g. 5000.00
     * @param from   - start date, e.g. 12/10/2015
     * @param to     - end date, e.g. 1/10/2016
     * @return - interest
     */
    public BigDecimal calculate(BigDecimal amount, LocalDate from, LocalDate to) {
        var percentAmount = BigDecimal.ZERO;
        var slidingFrom = from;
        var lastDayOfYear = from.with(lastDayOfYear());
        while (to.isAfter(lastDayOfYear)) {
            percentAmount = percentAmount.add(calculate(amount, slidingFrom, lastDayOfYear));
            slidingFrom = lastDayOfYear;
            lastDayOfYear = lastDayOfYear.plusYears(1);
        }
        return percentAmount.add(calculate(amount, DAYS.between(slidingFrom, to), to.isLeapYear()));
    }
}
