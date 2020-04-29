package ru.timmson.jloan;

import lombok.experimental.SuperBuilder;
import ru.timmson.jloan.calendar.ProductionCalendar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractLoan} class
 *
 * @author Artem Krotov
 */
@SuperBuilder
abstract class AbstractLoan implements Loan {

    protected BigDecimal amount;
    protected BigDecimal annualInterestRate;
    protected long termInMonth;
    protected int paymentOnDay;
    protected LocalDate issueDate;
    protected ProductionCalendar productionCalendar;

    private LoanInterestRate interestRate;
    private LoanSchedule loanSchedule;

    protected final List<LoanPayment> initPayments() {
        final var payments = new ArrayList<LoanPayment>();
        payments.add(LoanPayment
                .builder()
                .date(this.issueDate)
                .interestRate(this.annualInterestRate)
                .finalBalance(this.amount)
                .build());
        return payments;
    }

    protected LoanInterestRate getLoanInterestRate() {
        synchronized (this) {
            if (interestRate == null) {
                interestRate = new LoanInterestRate(annualInterestRate);
            }
        }
        return interestRate;
    }

    /**
     * Generates schedule.
     * <p>
     * Caches it for next usage.
     *
     * @return - loan schedule {@link LoanSchedule}
     */
    @Override
    public final LoanSchedule getSchedule() {
        synchronized (this) {
            if (loanSchedule == null) {
                loanSchedule = LoanSchedule.build(getPayments());
            }
        }
        return loanSchedule;
    }

    /**
     * Generates payments
     *
     * @return list of {@link LoanPayment}
     */
    protected abstract List<LoanPayment> getPayments();

    /**
     * Returns next working date if ProductionCalendar is not null
     *
     * @param date - given date
     * @return the closest working date
     */
    protected LocalDate getNextWorkingDate(LocalDate date) {
        if (productionCalendar != null) {
            date = productionCalendar.getNextWorkDay(date);
        }
        return date;
    }

}
