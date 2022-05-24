package DTOs;

import BankActions.Loan;
import BankActions.LoanStatus;
import BankActions.Payment;
import javafx.beans.property.SimpleStringProperty;
//import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class LoanDTOs implements Serializable {
    private SimpleStringProperty nameOfLoan = new SimpleStringProperty(this, "nameOfLoan");
    private SimpleStringProperty category = new SimpleStringProperty(this, "category");
    private SimpleStringProperty statusName = new SimpleStringProperty(this, "status");
    private SimpleStringProperty nameOfLoaner = new SimpleStringProperty(this, "nameOfLoaner");
    private final int originalAmount;
    private final int durationOfTheLoan;
    private final int paymentFrequency;
    private final int interest;
    private final List<?> listOfLenders;// TODO: do we need it?
    private final int howManyUnitsOfTimeAreLeft; //TODO: wtf does it mean
    private final float interestPayedSoFar;
    private final float theInterestYetToBePaidOnTheLoan;
    private final int theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar;
    private final int theAmountOfTheFundYetToBePaid;
    private final LoanStatus status;
    private final List<Payment> loansPayments;


    public LoanDTOs(Loan loan){
        nameOfLoan.set(loan.getNameOfLoan());
        category.set(loan.getCategory());
        originalAmount = loan.getOriginalAmount();
        nameOfLoaner.set(loan.getNameOfLoaner());
        durationOfTheLoan = loan.getDurationOfTheLoan();
        paymentFrequency = loan.getPaymentFrequency();
        howManyUnitsOfTimeAreLeft = loan.getHowManyYazAreLeft();
        interest = loan.getInterest();
        listOfLenders = Collections.unmodifiableList(loan.getListOfLenders());
        interestPayedSoFar = loan.getInterestPayedSoFar();
        theInterestYetToBePaidOnTheLoan = loan.getTheInterestYetToBePaidOnTheLoan();
        theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar = loan.getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar();
        theAmountOfTheFundYetToBePaid = loan.getTheAmountOfPrincipalPaymentYetToBePaid();
        status = loan.getStatus();
        statusName.set(status.name());

        loansPayments = loan.getPayments();
    }

   public String getNameOfLoan() {
        return nameOfLoan.get();
    }

    public String getCategory() {
        return category.get();
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public String getNameOfLoaner() {
        return nameOfLoaner.get();
    }

    public int getDurationOfTheLoan() {
        return durationOfTheLoan;
    }

    public int getPaymentFrequency() {
        return paymentFrequency;
    }

    public int getInterest() {
        return interest;
    }

    public List<?> getListOfLenders() {
        return listOfLenders;
    }

    public float getInterestPayedSoFar() {
        return interestPayedSoFar;
    }

    public float getTheInterestYetToBePaidOnTheLoan() {
        return theInterestYetToBePaidOnTheLoan;
    }

    public int getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar() {
        return theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar;
    }

    public int getTheAmountOfTheFundYetToBePaid() {
        return theAmountOfTheFundYetToBePaid;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName.get();
    }

    public List<Payment> getLoansPayments() {
        return loansPayments;
    }

    public SimpleStringProperty statusNameProperty() {
        return statusName;
    }

    public SimpleStringProperty nameOfLoanProperty() {
        return nameOfLoan;
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public SimpleStringProperty nameOfLoanerProperty() {
        return nameOfLoaner;
    }
}
