# jloan

Loan Schedule For Java

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/841ed6f10f7146bfb3d319c85d0cfc63)](https://www.codacy.com/manual/timmson/jloan)
[![Gradle Package](https://github.com/timmson/jloan/workflows/Gradle%20Package/badge.svg)](https://github.com/timmson/jloan/actions?query=workflow%3A%22Gradle%20Package%22)

## Example of usage
````java
class Example {

    public static void  main(String[] args) {
      
            final Loan loan = annuityLoanBuilder()
                    .amount(valueOf(500000)) //amount
                    .annualInterestRate(valueOf(11.5)) //interest
                    .termInMonth(12) //term
                    .paymentOnDay(25) //day of payment
                    .issueDate(of(2018, 10, 25)) //issue date
                    .build();
    
            final LoanSchedule schedule = loan.getSchedule();
    
            System.out.println(schedule);
    }
}
````

## Set fixed payment
````java
final Loan loan = annuityLoanBuilder()
...
    .paymentAmount(valueOf(57000)) //57000.00 will be setted as payment through all monthes except last
...
````

## Set production calendar
````java
final Loan loan = annuityLoanBuilder()
...
    .productionCalendar(RussianProductionCalendar.getInstance()) // you can implement ProductionCalendar youself
...
````

### Add Early repayment
````java
final Loan loan = annuityLoanBuilder()
...
    addEarlyRepayment(of(2016, 12, 26), valueOf(44000)) // you can implement ProductionCalendar youself
...
````