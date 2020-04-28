package ru.timmson.jloan;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Period;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        final var lastPaymentIndex = payments.size() - 1;
        this.firstPayment = payments.get(1).getAmount();
        this.lastPayment = payments.get(lastPaymentIndex).getAmount();
        this.minPaymentAmount = payments.stream().map(LoanPayment::getAmount).min(Comparator.naturalOrder()).orElse(ZERO);
        this.maxPaymentAmount = payments.stream().map(LoanPayment::getAmount).max(Comparator.naturalOrder()).orElse(ZERO);
        this.termInMonth = Period.between(payments.get(0).getDate(), payments.get(lastPaymentIndex).getDate()).getMonths();
        this.amount = payments.get(0).getFinalBalance();
        this.overallInterest = payments.stream().map(LoanPayment::getInterestAmount).reduce((l, c) -> l = l.add(c)).orElse(ZERO);
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