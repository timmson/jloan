package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanInterestRateShould {

    @Test
    void instantiateFromBigDecimalOrFloat() {
        final var result1 = new LoanInterestRate(16.8f);
        final var result2 = new LoanInterestRate(valueOf(16.8));

        assertEquals(0, result1.compareTo(result2));
    }

    @Test
    void calculateAmountOfPercentWhenDaysGiven() {
        final var loanYear = new LoanInterestRate(valueOf(16.8));

        BigDecimal result = loanYear.calculate(valueOf(1000), 30, false);

        assertEquals(valueOf(13.81), result);
    }

    @Test
    void calculateAmountOfPercentWhenDatesGiven() {
        final var loanYear = new LoanInterestRate(valueOf(16.8));

        BigDecimal result = loanYear.calculate(valueOf(1000), of(2015,11,10), of(2015,12,10));

        assertEquals(valueOf(13.81), result);
    }

    @Test
    void calculateAmountOfPercentWhenDatesInDifferentYearGiven() {
        final var loanYear = new LoanInterestRate(valueOf(16.7));

        BigDecimal result = loanYear.calculate(valueOf(1000), of(2015,12,10), of(2016,1,10));

        assertEquals(valueOf(14.17), result);
    }

}
