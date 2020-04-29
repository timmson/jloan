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
