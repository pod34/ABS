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
    private Map<String, Customer> Customers;
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
        Customers = customersInSystem.stream().collect(Collectors.toMap(Customer::getName, customer -> customer));
        LoansInBank = loansInSystem.stream().collect(Collectors.toMap(Loan::getNameOfLoan, loan -> loan));
        allCategories = allCategoriesInSystem;
        Yaz = 1;
        yazProperty.set("Current Yaz: " + Yaz);
        return flag;
    }

    public int getCurrentYaz(){
        int tmp = Yaz;
        return tmp;
    }

    public void ViewInformationOnExistingLoansAndTheirStatus(){
        int numOfLoan = 1;
        for (Loan curLoan: LoansInBank.values()) {
            curLoan.PrintLoan(true, numOfLoan);
            numOfLoan++;
        }
    }

    public void DisplayInformationAboutSystemCustomers(){
        for (Customer curCustomer: Customers.values()) {
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
    public AccountTransactionDTO DepositToAccount(int amount,String nameOfCostumer){
        return Customers.get(nameOfCostumer).DepositMoney(amount,Yaz);
    }

    @Override
    public AccountTransactionDTO WithdrawFromTheAccount(int amount,String nameOfCostumer){
        return Customers.get(nameOfCostumer).WithdrawMoney(amount,Yaz);
    }

    public List<CustomerDTOs> getListOfDTOsCustomer(){
        List<CustomerDTOs> customerDtosList = new ArrayList<>(Customers.size());
        List<AccountTransactionDTO> listOFTransactinsDTOs;

        for (Customer curCustomer: Customers.values()) {
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
                .filter(L-> Customers.get(L.getNameOfLoaner()).getLoansAsABorrower().size() <= maxOpenLoansForLoanOwner)
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

    public boolean checkIfCustomerHasEnoughMoneyToInvestByGivenAmount(String i_nameOfCustomer,int amountToInvest){
        return Customers.get(i_nameOfCustomer).getMoneyInAccount() >= amountToInvest;
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
            Customers.get(nameOfLender).makeAnInvestment(loan.getNameOfLoan(),tmpMoneyInvested, Yaz);
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
        int debt = 0;
        Map<Loan,Integer> newRiskLoans = new HashMap<>();
        for(Loan curLoan : LoansInBank.values().stream().filter(L -> (!L.getStatus().equals(LoanStatus.FINISHED)) && (L.getNextYazForPayment() == Yaz)).collect(Collectors.toList())){
            for(Map.Entry<String,LeftToPay> curLender :curLoan.getMapOfLenders().entrySet()){
                debt += curLender.getValue().getAmountToPayByGivenYaz(Yaz);
            }
            if(debt != 0){
                curLoan.makeRisk(Yaz,debt);
                newRiskLoans.put(curLoan,debt);
                curLoan.setDebt(debt);
            }
            else{
                curLoan.setNextYazForPayment();
                curLoan.setDebt(0);
            }
        }
        sendMassagesToNewRiskLoans(newRiskLoans);
        Yaz++;
        yazProperty.set("Current Yaz: " + Yaz);
        for(Customer curCustomer : Customers.values()){
            List<Loan> curCustomerLoans = LoansInBank.values().stream().filter(L -> L.getNameOfLoaner().equals(curCustomer.getName())).filter(L -> (L.getStatus().equals(LoanStatus.ACTIVE)||L.getStatus().equals(LoanStatus.RISK))).collect(Collectors.toList());
            for(Loan curLoan : curCustomerLoans){
                curLoan.setHowManyYazAreLeft();
                if(curLoan.getNextYazForPayment() == Yaz){
                    mainController.setMessage(curLoan.getNameOfLoaner(), curLoan.getYazlyPaymentWithDebtsCalculation(Yaz), curLoan.getNameOfLoan(), "The payment date has arrived on the loan: ");
                }
            }
        }
    }

    private void sendMassagesToNewRiskLoans(Map<Loan,Integer> i_newRiskLoans) {
        for (Map.Entry<Loan,Integer> curLoan : i_newRiskLoans.entrySet()) {
                mainController.setMessage(curLoan.getKey().getNameOfLoaner(), curLoan.getValue(), curLoan.getKey().getNameOfLoan(), "You did not pay on the due date: ");
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

    public void YazlyPaymentForGivenLoans(Map<String,Integer> loansToPay){
        int moneyPaidTmp;
        int totalAmountPaidThisYaz = 0;
        for(Map.Entry<String,Integer> entry : loansToPay.entrySet()){
            int amountUserWantToPay = entry.getValue();
            if(amountUserWantToPay != 0) {
                int amountOfLenders = LoansInBank.get(entry.getKey()).getMapOfLenders().size();
                Map<String,LeftToPay> sortedLendersByAmountToPay = LoansInBank.get(entry.getKey()).getMapOfLenders().entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getValue().getAmountToPayByGivenYaz(Yaz)))
                        .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2) -> e1,LinkedHashMap::new));

                for (Map.Entry<String, LeftToPay> curLender : sortedLendersByAmountToPay.entrySet()) {//TODO to make sure it is sorted by leftToPayValues
                    int dividedAmount = amountUserWantToPay / amountOfLenders;
                    moneyPaidTmp = makePayment(dividedAmount,curLender.getKey(),curLender.getValue());
                    amountUserWantToPay -= moneyPaidTmp;
                    amountOfLenders--;
                    totalAmountPaidThisYaz += moneyPaidTmp;
                }
                LoansInBank.get(entry.getKey()).makeLoanPayment(Yaz,totalAmountPaidThisYaz,true);
                Customers.get(LoansInBank.get(entry.getKey()).getNameOfLoaner()).WithdrawMoney(totalAmountPaidThisYaz,Yaz);
            }
        }
    }

    public int makePayment(int amountToPay,String nameOfCustomer,LeftToPay customerLeftToPay){
        int amountInvested = customerLeftToPay.setAmountPaidInGivenYaz(Yaz,amountToPay);
        Customers.get(nameOfCustomer).DepositMoney(amountInvested,Yaz);
        return amountInvested;
    }

    public void fullPaymentOnLoans(List<String> loanNames, String customerName){
        Loan curLoanToPay;
        Customer curCustomer = Customers.get(customerName);
        for (String curLoan: loanNames) {
            curLoanToPay = LoansInBank.get(curLoan);
            payForLoanFully(curLoanToPay, curCustomer);
        }

    }

    private void payForLoanFully(Loan loan, Customer customer){
        Boolean flag = false;
        int sumOfPayments = 0,curPayment = 0;
        for(Map.Entry<String,LeftToPay> curLender : loan.getMapOfLenders().entrySet()){
            curPayment = curLender.getValue().getAmountLeftToPayToCloseTheLoan(loan.getInterest());
            Customers.get(curLender.getKey()).DepositMoney(curPayment,Yaz);
            curLender.getValue().resetLeftToPayAfterClosingTheLoan();
            sumOfPayments += curPayment;
        }
        customer.WithdrawMoney(sumOfPayments,Yaz);
        loan.makeFullyPaymentToCloseLoan(Yaz,sumOfPayments - (sumOfPayments * loan.getInterest()) / 100,(sumOfPayments * loan.getInterest()) / 100);
        checkIfTheLoanIsFinished(loan);
       /* while(!flag){
            loan.setHowManyYazAreLeft();
            loan.getYazlyPaymentWithDebtsCalculation();
            customer.WithdrawMoney(loan.getYazlyPaymentWithDebts(), Yaz);
            this.makePayment(loan);
            loan.makeLoanPayment(Yaz);
            flag = checkIfTheLoanIsFinished(loan);
        }*/
    }

    public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsBorrower(String customerName){
        return Customers.get(customerName).getStringOfLoansAsBorrowerByStatus();
    }

    public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyForLoanAsLender(String customerName){
        return Customers.get(customerName).getStringOfLoansAsLenderByStatus();
    }

    public Map<String, SimpleStringProperty> getLoanDataByStatusPropertyFromSystemMap(String loanName){
        return LoansInBank.get(loanName).getLoanDataByStatusPropertyAndStatusProperty();
    }

    public CustomerDTOs getCustomerByName(String name){
        return new CustomerDTOs(Customers.get(name),getListOfTransactionsDTO(Customers.get(name).getTransactions()));
    }

    public List<String> checkWhatLoansCanBeFullyPaidSystem(List<String> loanNames, String customerName){
        List<Loan> wantedLoans = LoansInBank.values().stream().filter(L -> loanNames.contains(L.getNameOfLoan())).sorted(Comparator.comparingInt(e -> (e.getTheAmountOfPrincipalPaymentYetToBePaid() + e.getTheInterestYetToBePaidOnTheLoan()))).collect(Collectors.toList());
        List<String> loansThatCanBeFullyPaid = new ArrayList<>();
        int moneyInThatCanBeAfterPayment = Customers.get(customerName).getMoneyInAccount();
        for (Loan curLoan: wantedLoans) {
            if(moneyInThatCanBeAfterPayment >=  curLoan.getTheAmountOfPrincipalPaymentYetToBePaid() + curLoan.getTheInterestYetToBePaidOnTheLoan()){
                moneyInThatCanBeAfterPayment -= curLoan.getTheAmountOfPrincipalPaymentYetToBePaid() + curLoan.getTheInterestYetToBePaidOnTheLoan();
                loansThatCanBeFullyPaid.add(curLoan.getNameOfLoan());
            }
        }
        return loansThatCanBeFullyPaid;
    }

    public List<String> checkIfCanPayAllLoans(Map<String,Integer> loansToPay, String customerName){
        int amountInBank = Customers.get(customerName).getMoneyInAccount();
        List<String> loansThatCanBePaid = new ArrayList<>();
        Map<String,Integer> sortedLendersByAmountToPay = loansToPay.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (Map.Entry<String, Integer> curLoan: sortedLendersByAmountToPay.entrySet()) {
            if(amountInBank != 0){
                amountInBank -= curLoan.getValue();
                loansThatCanBePaid.add(curLoan.getKey());
            }
        }
        return loansThatCanBePaid;
    }

    public Boolean checkIfMoneyCanBeWithdraw(int amount, String customerName){
        return amount <= Customers.get(customerName).getMoneyInAccount();
    }
}
