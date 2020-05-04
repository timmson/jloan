package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoanPaymentShould {

    @Test
    void transformToString() {
        final var loanPayment = LoanPayment.builder().build();

        final var result = loanPayment.toString();

        assertNotNull(result);
    }
}