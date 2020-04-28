package ru.timmson.jloan;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Period;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Getter
@ToString
class LoanSchedule {

    private final BigDecimal overallInterest;
    private final BigDecimal firstPayment;
    private final BigDecimal lastPayment;
    private final BigDecimal minPaymentAmount;
    private final BigDecimal maxPaymentAmount;
    private final int termInMonth;
    private final BigDecimal amount;
    private final BigDecimal efficientRate;
    private final BigDecimal fullAmount;
    private final List<LoanPayment> payments;

    public LoanSchedule(List<LoanPayment> payments) {
        this.payments = payments;
        final var firstPayment = payments.get(0);
        final var lastPayment = payments.get(payments.size() - 1);

        this.firstPayment = payments.get(1).getAmount();
        this.lastPayment = lastPayment.getAmount();

        this.minPaymentAmount = payments.stream().map(LoanPayment::getAmount).min(Comparator.naturalOrder()).orElse(ZERO);
        this.maxPaymentAmount = payments.stream().map(LoanPayment::getAmount).max(Comparator.naturalOrder()).orElse(ZERO);

        this.termInMonth = Period.between(firstPayment.getDate(), lastPayment.getDate()).getMonths();
        this.amount = firstPayment.getFinalBalance();

        this.overallInterest = payments.stream().map(LoanPayment::getInterestAmount).reduce(BigDecimal::add).orElse(ZERO);
        this.efficientRate = this.overallInterest.divide(this.amount, MathContext.DECIMAL32).multiply(valueOf(100)).setScale(2, HALF_UP);
        this.fullAmount = this.overallInterest.add(this.amount);
    }

    public static LoanSchedule build(List<LoanPayment> payments) {
        return new LoanSchedule(payments);
    }

    public List<LoanPayment> getPayments() {
        return Collections.unmodifiableList(this.payments);
    }

}
