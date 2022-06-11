package BankSystem;

import BankActions.LoanStatus;
import BankActions.Loan;
import DTOs.AccountTransactionDTO;
import DTOs.CategoriesDTO;
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
    AccountTransactionDTO DepositToAccount(int amount, String nameOfCostumerToDepositTo);
    AccountTransactionDTO WithdrawFromTheAccount(int amount, String nameOfCostumerToDepositTo);
    List<LoanDTOs> ActivationOfAnInlay(List<String> chosenCategories, int minimumDuration, int minimumInterestForSingleYaz,int maxOpenLoansForLoanOwner, String name);
    CustomerDTOs LoansInlay(List<String> namesOfLoans,int amountOfMoneyUserWantToInvest,String nameOfLender,int maxOwnerShipOfTheLoan);
    void IncreaseYaz();
    List<CustomerDTOs> getListOfDTOsCustomer();
    boolean checkIfCustomerHasEnoughMoneyToInvestByGivenAmount(String i_nameOfCustomer,int amountToInvest);
    List<LoanDTOs> getListOfLoansDTO();
    List<LoanDTOs> getListOfLoansDtoByListOfNamesOFLoans(List<String> i_loansName);
    SimpleStringProperty getYazProperty();
    void fullPaymentOnLoans(List<String> loanNames, String customerName);
    Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsLender(String customerName);
    Map<String, SimpleStringProperty> getLoanDataByStatusPropertyFromSystemMap(String loanName);
    Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsBorrower(String customerName);
    CategoriesDTO getAllCategories();
    CustomerDTOs getCustomerByName(String name);
    void YazlyPaymentForGivenLoans(Map<String,Integer> loansToPay);
    int getCurrentYaz();
    List<String> checkWhatLoansCanBeFullyPaidSystem(List<String> loanNames, String customerName);
    List<String> checkIfCanPayAllLoans(Map<String,Integer> loansToPay, String customerName);
    Boolean checkIfMoneyCanBeWithdraw(int amount, String customerName);

    }
