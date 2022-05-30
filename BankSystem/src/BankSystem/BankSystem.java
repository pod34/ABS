package BankSystem;

import BankActions.LoanStatus;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import SystemExceptions.InccorectInputType;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;
import java.util.Map;

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
    Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsBorrower(String customerName);
    Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsLender(String customerName);
    Map<String, SimpleStringProperty> getLoanDataByStatusPropertyFromSystemMap(String loanName);
}
