package ru.timmson.jloan;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.time.temporal.ChronoUnit.MONTHS;

/**
 * LoanSchedule DTO
 *
 * @author Artem Krotov
 */
@Getter
class LoanSchedule {

    private final BigDecimal overallInterest;
    private final BigDecimal firstPayment;
    private final BigDecimal lastPayment;
    private final BigDecimal minPaymentAmount;
    private final BigDecimal maxPaymentAmount;
    private final long termInMonth;
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

        this.termInMonth = MONTHS.between(firstPayment.getDate(), lastPayment.getDate());
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String border = IntStream.range(0, 14).mapToObj(i -> "-").collect(Collectors.joining());
        String allBorder = String.format("+%s+%s+%s+%s+%s+%s+%n", border, border, border, border, border, border);
        sb.append(allBorder);
        sb.append(String.format(
                "| %-12s | %-12s | %-12s | %-27s | %-12s |%n",
                "", "", "", "Including", ""
        ));
        sb.append(String.format(
                "| %-12s | %-12s | %-12s +%s+%s+ %-12s |%n",
                "Date", "In. Balance", "Payment", border, border, "Out. Balance"
        ));
        sb.append(String.format(
                "| %-12s | %-12s | %-12s | %-12s | %-12s | %-12s |%n",
                "", "", "", "Principal", "Interest", ""
        ));
        sb.append(allBorder);
        sb.append(payments.stream().map(LoanPayment::toString).collect(Collectors.joining("")));
        sb.append(allBorder);
        sb.append(String.format(
                "| %-12s | %-12s | %-12s | %-12s | %-12s | %-12s |%n",
                "", "", fullAmount, amount, overallInterest, ""
        ));
        sb.append(allBorder);
        return sb.toString();
    }
}
