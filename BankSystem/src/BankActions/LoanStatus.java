package BankActions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum LoanStatus implements Serializable {
    NEW{
        void PrintForCustomer (Loan curLoanToPrint){
            System.out.println("Status is: New\n");
        }
        void PrintForLoan(Loan curLoan){
            System.out.println("Status is: New\n");
        }
    },

        PENDING{
            void PrintForCustomer (Loan curLoanToPrint){
                System.out.println("Status is: Pending\n" + "The amount left for the loan to be active: "
                        + curLoanToPrint.getTheAmountLeftToMakeTheLoanActive());
            }
            void PrintForLoan(Loan curLoan){
                System.out.println("Status is: Pending");
                /*
                for (Map.Entry<String, Integer> entry: curLoan.getMapOfLenders().entrySet()) {
                    System.out.println("The name of the Lender is: " + entry.getKey()
                            + "\n" + "His investment as part of the loan amount: " + entry.getValue());
                }
                */
                PrintingPendingInfo(curLoan);
                System.out.println("The total amount raised:" + (curLoan.getOriginalAmount() - curLoan.getTheAmountLeftToMakeTheLoanActive()));
                System.out.println("The amount left to make the loan active: " + curLoan.getTheAmountLeftToMakeTheLoanActive());

            }
        },
        ACTIVE{
            void PrintForCustomer(Loan curLoanToPrint) {
                System.out.println("The status is: Active\n" + "The next payment is in: "
                        + curLoanToPrint.getNextYazForPayment()
                        +"\n"
                        + "The total amount of the loan to be paid: "
                        + (curLoanToPrint.getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar()
                        + curLoanToPrint.getTheAmountOfPrincipalPaymentYetToBePaid()));
            }
            void PrintForLoan(Loan curLoan){
                int principalPaymentTotal = 0;
                int interestPaymentTotal = 0;
                System.out.println("Status is: Active");
                PrintingPendingInfo(curLoan);
                PrintActiveInfo(curLoan);
            }
        },
        RISK{
            void PrintForCustomer(Loan curLoanToPrint){
                System.out.println("Status is: Risk");
                int totalAmountNotPaid = 0;
                System.out.println("The payments that were not paid: ");
                curLoanToPrint.getPayments().stream().filter(P -> P.getPaid().equals(false)).forEach(Payment::printPaymentTotalAmount);
                List<Payment> allNotPaidPayments = curLoanToPrint.getPayments().stream().filter(P -> P.getPaid().equals(false)).collect(Collectors.toList());
                for (Payment curPayment: allNotPaidPayments) {
                    totalAmountNotPaid += curPayment.getTotalAmountPaid();
                }
                System.out.println("The total number of payments that were not paid: " + allNotPaidPayments.size()
                + "\nThe total amount that were not paid for those payments: " + totalAmountNotPaid);
            }
            void PrintForLoan(Loan curLoan){
                System.out.println("Status is: Risk");
                PrintingPendingInfo(curLoan);
                PrintActiveInfo(curLoan);
                int totalAmountNotPaid = 0;
                System.out.println("The payments that were not paid: ");
                curLoan.getPayments().stream().filter(P -> P.getPaid().equals(false)).forEach(Payment::printPaymentTotalAmount);
                List<Payment> allNotPaidPayments = curLoan.getPayments().stream().filter(P -> P.getPaid().equals(false)).collect(Collectors.toList());
               /* for (Payment curPayment: allNotPaidPayments) {
                    totalAmountNotPaid += curPayment.getTotalAmountPaid();
                }*/
                System.out.println("The total number of payments that were not paid and still need to be paid: " + curLoan.getTotalMissedYazNeedToPayBack()
                        + "\nThe total amount that were not paid for those payments: " + allNotPaidPayments.get(allNotPaidPayments.size() -1 ).getTotalAmountPaid());
            }
        },
        FINISHED{
            void PrintForCustomer(Loan curLoanToPrint){
                System.out.println("The status is: Finished\n" + "The starting date is: "
                        + curLoanToPrint.getStartingDate() + "\n" + "The finish date is: "
                        + curLoanToPrint.getFinishDate());
            }
            void PrintForLoan(Loan curLoan){
                System.out.println("Status is: Finished");
                PrintingPendingInfo(curLoan);
                System.out.println("The starting date is: "
                        + curLoan.getStartingDate() + "\n" + "The finish date is: "
                        + curLoan.getFinishDate());
                printPaidPayments(curLoan);
            }
        };

        private static void PrintingPendingInfo(Loan curLoan)
        {
            if(curLoan.getMapOfLenders() == null)
                System.out.println("There are no lenders for this loan.");
            else {
                for (Map.Entry<String, LeftToPay> entry : curLoan.getMapOfLenders().entrySet()) {
                    System.out.println("The name of the Lender is: " + entry.getKey()
                            + "\n" + "His investment as part of the loan amount: " + entry.getValue().getOriginalAmount());
                }
            }
        }

        private static void PrintActiveInfo(Loan curLoan) {
            System.out.println("The date the loan became active:" + curLoan.getStartingDate());
            System.out.println("The next payment is in: " + curLoan.getNextYazForPayment());
            printPaidPayments(curLoan);
        }

        private static void printPaidPayments(Loan curLoan){
            int principalPaymentTotal = 0;
            int interestPaymentTotal = 0;
            for (Payment curPayment : curLoan.getPayments()) {
                if (curPayment.getPaid()) {
                   // curPayment.printPayment();
                    principalPaymentTotal += curPayment.getPrincipalPayment();
                    interestPaymentTotal += curPayment.getInterestPayment();
                }
            }
            System.out.println("The principal payment total: " + principalPaymentTotal);
            System.out.println("The interest payment total: " + interestPaymentTotal);
            if(curLoan.getStatus().equals((LoanStatus.ACTIVE)) || curLoan.getStatus().equals((LoanStatus.RISK))) {
                System.out.println("The principal payment yet to be paid:" + curLoan.getTheAmountOfPrincipalPaymentYetToBePaid());
                System.out.println("The interest payment yet to be paid:" + curLoan.getTheInterestYetToBePaidOnTheLoan());
            }
        }
    abstract void PrintForCustomer(Loan curLoanToPrint);
    abstract void PrintForLoan(Loan curLoan);
}
