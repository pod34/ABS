package BankSystem;

import BankActions.*;
import Component.MainComponent.BankController;
import Costumers.Customer;
import DTOs.AccountTransactionDTO;
import DTOs.CategoriesDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import SystemExceptions.InccorectInputType;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static BankSystem.FromXmlToClasses.fromXmlToObjects;

public class SystemImplement implements BankSystem , Serializable {
    private Map<String, Customer> Costumers;
    private Map<String, Loan> LoansInBank;
    private List<String> allCategories;
    private BankController mainController;
    private static int Yaz = 1;
    private SimpleStringProperty yazProperty = new SimpleStringProperty();


    public SimpleStringProperty getYazProperty() {
        return yazProperty;
    }

    public SystemImplement(BankController mainController) {
        this.mainController = mainController;
    }

    public boolean ReadingTheSystemInformationFile(String FileName) throws InccorectInputType {
        boolean flag;
        if(!checkFileName(FileName))
            throw new InccorectInputType(InccorectInputType.getWrongFileType());
        List<Customer> customersInSystem = new ArrayList<>();
        List<Loan> loansInSystem = new ArrayList<>();
        List<String> allCategoriesInSystem = new ArrayList<>();
        flag = fromXmlToObjects(customersInSystem, loansInSystem, FileName, allCategoriesInSystem);
        Costumers = customersInSystem.stream().collect(Collectors.toMap(Customer::getName, customer -> customer));
        LoansInBank = loansInSystem.stream().collect(Collectors.toMap(Loan::getNameOfLoan, loan -> loan));
        allCategories = allCategoriesInSystem;
        Yaz = 1;
        yazProperty.set("Current Yaz: " + Yaz);
        return flag;
    }

    public void ViewInformationOnExistingLoansAndTheirStatus(){
        int numOfLoan = 1;
        for (Loan curLoan: LoansInBank.values()) {
            curLoan.PrintLoan(true, numOfLoan);
            numOfLoan++;
        }
    }

    public void DisplayInformationAboutSystemCustomers(){
        for (Customer curCustomer: Costumers.values()) {
            AtomicInteger numOfLoan = new AtomicInteger(1);
            System.out.println("\n");
            System.out.println("The name of the customer: " + curCustomer.getName());
            curCustomer.PrintCustomerTransactions();
            if(curCustomer.getLoansAsABorrower().size() == 0)
                System.out.println("There are no loans as borrower for: " + curCustomer.getName());
            else {
                System.out.println("The Loans that " + curCustomer.getName() + " borrowed: ");
                LoansInBank.values().stream().filter(L -> L.getNameOfLoaner().equals(curCustomer.getName())).forEach(L -> L.PrintLoan(false, numOfLoan.getAndIncrement()));
            }
            if(curCustomer.getLoansAsALender().size() == 0)
                System.out.println("There are no loans as lender for: " + curCustomer.getName());
            else {
                System.out.println("The Loans that " + curCustomer.getName() + " lend: ");
                LoansInBank.values().stream().filter(L -> L.ifTheNameIsInTheNameList(curCustomer.getName())).forEach(L -> L.PrintLoan(false, numOfLoan.getAndIncrement()));
            }
        }
    }

    @Override
    public void DepositToAccount(int amount,String nameOfCostumer){
        Costumers.get(nameOfCostumer).DepositMoney(amount,Yaz);
    }

    @Override
    public boolean WithdrawFromTheAccount(int amount,String nameOfCostumer){
        return Costumers.get(nameOfCostumer).WithdrawMoney(amount,Yaz);
    }

    public List<CustomerDTOs> getListOfDTOsCustomer(){
        List<CustomerDTOs> customerDtosList = new ArrayList<>(Costumers.size());
        List<AccountTransactionDTO> listOFTransactinsDTOs;

        for (Customer curCustomer: Costumers.values()) {
            listOFTransactinsDTOs = getListOfTransactionsDTO(curCustomer.getTransactions());
            customerDtosList.add(new CustomerDTOs(curCustomer,listOFTransactinsDTOs));
        }
        return Collections.unmodifiableList(customerDtosList);
    }

    public List<LoanDTOs> getListOfLoansDTO(){
        List<Loan> listOfLoans = LoansInBank.values().stream().collect(Collectors.toList());
        List<LoanDTOs> listOfLoansDTO = new ArrayList<>(listOfLoans.size());
        for (Loan curLoan : listOfLoans) {
            listOfLoansDTO.add(new LoanDTOs(curLoan));
        }
        return listOfLoansDTO;
    }

    private List<AccountTransactionDTO> getListOfTransactionsDTO(List<AccountTransaction> accountTransactions){
        List<AccountTransactionDTO> ListOfTransactionDTO = new ArrayList<>();
        if(accountTransactions == null)
            return ListOfTransactionDTO = null;
        else {
            for (AccountTransaction curTransaction : accountTransactions) {
                ListOfTransactionDTO.add(new AccountTransactionDTO(curTransaction));
            }
        }
        return ListOfTransactionDTO;
    }

    public List<LoanDTOs> ActivationOfAnInlay(List<String> chosenCategories, int minimumDuration, int minimumInterestForSingleYaz,int maxOpenLoansForLoanOwner,String name){
        List<LoanDTOs> filteredListOfLoansDTO = new ArrayList<>();
        List<Loan> filteredListOfLoans = LoansInBank.values().stream()
                .filter(L-> chosenCategories.contains(L.getCategory()))
                .filter(L-> Costumers.get(L.getNameOfLoaner()).getLoansAsABorrower().size() <= maxOpenLoansForLoanOwner)
                .filter(L->(L.getDurationOfTheLoan() >= minimumDuration))
                .filter(L->(L.getInterest() >= minimumInterestForSingleYaz)).filter(L->!L.getNameOfLoaner().equals(name))
                .filter(L->((L.getStatus().equals(LoanStatus.NEW)) || (L.getStatus().equals(LoanStatus.PENDING))))
                .collect(Collectors.toList());
        for (Loan curLoan: filteredListOfLoans) {
            filteredListOfLoansDTO.add(new LoanDTOs(curLoan));
        }
        return filteredListOfLoansDTO;
    }

    public List<LoanDTOs> getListOfLoansDtoByListOfNamesOFLoans(List<String> i_loansName){
        List<LoanDTOs> listOfLoansDTO = new ArrayList<>(i_loansName.size());
        for (String loanName : i_loansName){
            if(LoansInBank.containsKey(loanName));
            listOfLoansDTO.add(new LoanDTOs(LoansInBank.get(loanName)));
        }
        return listOfLoansDTO;

    }

    public CustomerDTOs LoansInlay(List<String> namesOfLoans,int amountOfMoneyUserWantToInvest,String nameOfLender,int maxOwnerShipOfTheLoan) {//TODO check if maxOwnerShip Does work
        List<Loan> loansForInvestment = FindLoansInSystemByNames(namesOfLoans);
        int numOfLoans = loansForInvestment.size();
        int tmpMoneyInvested,moneyToInvest;
        int dividedAmount;
        int totalAmountInvested = 0;
        for (Loan loan : loansForInvestment) {
            dividedAmount = amountOfMoneyUserWantToInvest / numOfLoans;
            if(calcPercentValueForLoan(loan.getOriginalAmount(),maxOwnerShipOfTheLoan) < dividedAmount) {
                moneyToInvest = calcPercentValueForLoan(loan.getOriginalAmount(),maxOwnerShipOfTheLoan);
            }
            else{
                moneyToInvest = dividedAmount;
            }
                if (loan.getTheAmountLeftToMakeTheLoanActive() < moneyToInvest) {
                    tmpMoneyInvested = loan.getTheAmountLeftToMakeTheLoanActive();
                    loan.setAnInvestment(loan.getTheAmountLeftToMakeTheLoanActive(), nameOfLender, Yaz);
                    this.depositTheMoneyOfTheLoanInBorrowerAccount(loan.getOriginalAmount(), loan.getNameOfLoaner(), loan);
                } else {
                    tmpMoneyInvested = moneyToInvest;
                    loan.setAnInvestment(moneyToInvest, nameOfLender, Yaz);
                    this.depositTheMoneyOfTheLoanInBorrowerAccount(loan.getOriginalAmount(), loan.getNameOfLoaner(), loan);
                }


            amountOfMoneyUserWantToInvest -= tmpMoneyInvested;
            Costumers.get(nameOfLender).makeAnInvestment(loan.getNameOfLoan(),tmpMoneyInvested, Yaz);
            totalAmountInvested += tmpMoneyInvested;
            numOfLoans--;

        }
        CustomerDTOs lender = new CustomerDTOs(totalAmountInvested);
        return lender;
    }

    private int calcPercentValueForLoan(int amount,int percent){
        return (amount * percent) / 100;
    }

    private void depositTheMoneyOfTheLoanInBorrowerAccount(int amount, String nameOfLoaner, Loan loan){
        if(loan.getStatus().equals(LoanStatus.ACTIVE))
        {
            this.DepositToAccount(amount, nameOfLoaner);
        }
    }

    private List<Loan> FindLoansInSystemByNames(List<String> namesOfLoans){
        List<Loan> loans = new ArrayList<>();
        for (String loanName : namesOfLoans){
            if(!loans.contains(LoansInBank.get(loanName)))
                loans.add(LoansInBank.get(loanName));
        }
        loans.sort(Comparator.comparing(Loan::getTheAmountLeftToMakeTheLoanActive));
        return loans;
    }

    public void IncreaseYaz(){
        List<Loan> allActiveLoans = LoansInBank.values().stream().filter(L -> L.getStatus().equals(LoanStatus.ACTIVE)).filter(L -> (L.getNextYazForPayment() == Yaz)).collect(Collectors.toList());
        checkIfLoanNeedsToBeInRisk(allActiveLoans);
        Yaz++;
        yazProperty.set("Current Yaz: " + Yaz);
        for(Customer curCustomer : Costumers.values()){
            List<Loan> curCustomerLoans = LoansInBank.values().stream().filter(L -> L.getNameOfLoaner().equals(curCustomer.getName())).filter(L -> (L.getStatus().equals(LoanStatus.ACTIVE)||L.getStatus().equals(LoanStatus.RISK))).collect(Collectors.toList());
            for(Loan curLoan : curCustomerLoans){
                curLoan.setHowManyYazAreLeft();
                if(curLoan.getNextYazForPayment() == Yaz){
                    //send message to relevant customer that is payday you need to pay is: curLoan.getYazlyPaymentWithDebtsCalculation()
                    mainController.setMessage(curLoan.getNameOfLoaner(), curLoan.getYazlyPaymentWithDebtsCalculation(), curLoan.getNameOfLoan(), "The payment date has arrived on the loan: ");
                }
            }
        }
    }

    private void checkIfLoanNeedsToBeInRisk(List<Loan> allActiveLoans) {
        for (Loan curLoan : allActiveLoans) {
            List<Payment> curLoanPayments = curLoan.getPayments().stream().filter(P -> (P.getPaymentDate() == Yaz)).collect(Collectors.toList());
            if (curLoanPayments.isEmpty()) {
                int amount = curLoan.getYazlyPaymentWithDebtsCalculation();
                mainController.setMessage(curLoan.getNameOfLoaner(), amount, curLoan.getNameOfLoan(), "You did not pay on the due date: ");
                curLoan.makeRisk(Yaz);
            }
        }
    }

    private Boolean checkIfTheLoanIsFinished(Loan curLoan){
        Boolean flag = false;
        if(curLoan.getHowManyYazAreLeft() == 0) {
            flag = (curLoan.getTheInterestYetToBePaidOnTheLoan() == 0 && curLoan.getTheAmountOfPrincipalPaymentYetToBePaid() == 0);
            if (flag)
                curLoan.makeFinished(Yaz);
        }
        return flag;
    }

    public static boolean checkFileName(String FileName){
        if(FileName.endsWith("xml"))
            return true;
        return false;
    }

    public CategoriesDTO getAllCategories(){
        return new CategoriesDTO(allCategories);
    }

    private void makePayment(Loan loan){
        for (Map.Entry<String, LeftToPay> entry :loan.getMapOfLenders().entrySet()) {
            Costumers.get(entry.getKey()).DepositMoney(entry.getValue().amountToPayThisYaz() + entry.getValue().interestToPayThisYaz(loan.getInterest()), Yaz);
            entry.getValue().setAmountLeftToPay();
            entry.getValue().setAmountOfPayment();
            entry.getValue().resetDebt();
        }
    }

    public void fullPaymentOnLoans(List<String> loanNames, String customerName){
        Loan curLoanToPay;
        Customer curCustomer = Costumers.get(customerName);
        for (String curLoan: loanNames) {
            curLoanToPay = LoansInBank.get(curLoan);
            payForLoanFully(curLoanToPay, curCustomer);
        }
    }

    private void payForLoanFully(Loan loan, Customer customer){
        Boolean flag = false;
        while(!flag){
            loan.setHowManyYazAreLeft();
            loan.getYazlyPaymentWithDebtsCalculation();
            customer.WithdrawMoney(loan.getYazlyPaymentWithDebts(), Yaz);
            this.makePayment(loan);
            loan.makeLoanPayment(Yaz);
            flag = checkIfTheLoanIsFinished(loan);
        }
    }

    public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsBorrower(String customerName){
        return Costumers.get(customerName).getStringOfLoansAsBorrowerByStatus();
    }

    public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsLender(String customerName){
        return Costumers.get(customerName).getStringOfLoansAsLenderByStatus();
    }

    public Map<String, SimpleStringProperty> getLoanDataByStatusPropertyFromSystemMap(String loanName){
        return LoansInBank.get(loanName).getLoanDataByStatusPropertyAndStatusProperty();
    }
}
