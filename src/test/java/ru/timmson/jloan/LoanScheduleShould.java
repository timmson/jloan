package ru.timmson.jloan;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoanScheduleShould {

    private final LoanPayment firstPayment = LoanPayment.builder().finalBalance(ONE).build();
    private final LoanPayment lastPayment = LoanPayment.builder().initialBalance(ONE).build();
    private final LoanSchedule loanSchedule = LoanSchedule.build(new LinkedList<>(List.of(firstPayment, lastPayment)));

    @Test
    void transformToString() {
        final var result = loanSchedule.toString();

        assertNotNull(result);
    }

    @Test
    void returnUnmodifiableList() {
        assertThrows(UnsupportedOperationException.class, () -> loanSchedule.getPayments().add(null));
    }
}