package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanPaymentShould {

    @Test
    void transformToString() {
        final var loanPayment = LoanPayment.builder().build();

        final var result = loanPayment.toString();
        System.out.println("result = " + result);

        assertEquals(97, result.length());
    }
}