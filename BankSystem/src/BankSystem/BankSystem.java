package BankSystem;

import BankActions.Loan;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import SystemExceptions.InccorectInputType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public interface BankSystem {
    boolean ReadingTheSystemInformationFile(String FileName) throws InccorectInputType;
    void ViewInformationOnExistingLoansAndTheirStatus();
    void DisplayInformationAboutSystemCustomers();
    void DepositToAccount(int amount, String nameOfCostumerToDepositTo);
    boolean WithdrawFromTheAccount(int amount,String nameOfCostumerToDepositTo);
    List<LoanDTOs> ActivationOfAnInlay(List<String> chosenCategories, int minimumDuration, int minimumInterestForSingleYaz, String name);
    void IncreaseYaz();
    List<CustomerDTOs> getListOfDTOsCustomer();
    List<LoanDTOs> getListOfLoansDTO();
    List<LoanDTOs> getListOfLoansDtoByListOfNamesOFLoans(List<String> i_loansName);
    SimpleStringProperty getYazProperty();
    void fullPaymentOnLoans(List<String> loanNames, String customerName);
}
