package Costumers;

import BankActions.*;
import DTOs.AccountTransactionDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.*;

public class Customer implements Serializable {
    private String name;
    private int moneyInAccount = 0;
    private List<String> LoansAsALender;
    private List<String> LoansAsABorrower;
    private List<AccountTransaction> Transactions;
    private Map<LoanStatus, SimpleIntegerProperty> numberOfLoansAsBorrowerByStatus;
    private Map<LoanStatus, SimpleStringProperty> stringOfLoansAsBorrowerByStatus;
    private Map<LoanStatus, SimpleIntegerProperty> numberOfLoansAsLenderByStatus;
    private Map<LoanStatus, SimpleStringProperty> stringOfLoansAsLenderByStatus;

    public Customer(String name, int balance, List<String> loansAsABorrowerNames) {
        this.name = name;
        moneyInAccount = balance;
        LoansAsALender = new ArrayList<>();
        /*if (loansAsABorrowerNames.size() == 0)
            LoansAsABorrower = null;
        else

         */
        LoansAsABorrower = loansAsABorrowerNames;
        Transactions = new ArrayList<>();
        numberOfLoansAsBorrowerByStatus = new HashMap<>();
        stringOfLoansAsBorrowerByStatus = new HashMap<>();
        numberOfLoansAsLenderByStatus = new HashMap<>();
        stringOfLoansAsLenderByStatus = new HashMap<>();
        initiativePropertyByStatus();

    }

    private void initiativePropertyByStatus(){
        for (LoanStatus status: LoanStatus.values()) {
            if(status.equals(LoanStatus.NEW)) {
                numberOfLoansAsLenderByStatus.put(status, new SimpleIntegerProperty(0));
                stringOfLoansAsLenderByStatus.put(status, new SimpleStringProperty(status.name() + ": " + 0));
                numberOfLoansAsBorrowerByStatus.put(status, new SimpleIntegerProperty(LoansAsABorrower.size()));
                stringOfLoansAsBorrowerByStatus.put(status, new SimpleStringProperty(status.name() + ": " + LoansAsABorrower.size()));
            }
            else{
                numberOfLoansAsLenderByStatus.put(status, new SimpleIntegerProperty(0));
                stringOfLoansAsLenderByStatus.put(status, new SimpleStringProperty(status.name() + ": " + 0));
                numberOfLoansAsBorrowerByStatus.put(status, new SimpleIntegerProperty(0));
                stringOfLoansAsBorrowerByStatus.put(status, new SimpleStringProperty(status.name() + ": " + 0));
            }
        }
    }

    public Map<LoanStatus, SimpleIntegerProperty> getNumberOfLoansAsBorrowerByStatus() {
        return numberOfLoansAsBorrowerByStatus;
    }

    public Map<LoanStatus, SimpleStringProperty> getStringOfLoansAsBorrowerByStatus() {
        return stringOfLoansAsBorrowerByStatus;
    }

    public Map<LoanStatus, SimpleIntegerProperty> getNumberOfLoansAsLenderByStatus() {
        return numberOfLoansAsLenderByStatus;
    }

    public Map<LoanStatus, SimpleStringProperty> getStringOfLoansAsLenderByStatus() {
        return stringOfLoansAsLenderByStatus;
    }

    public List<AccountTransaction> getTransactions() {
       /* if (Transactions.size() == 0)
            return null;
        else

        */
            return Collections.unmodifiableList(Transactions);
    }

    public List<String> getLoansAsALender() {
        /*if (LoansAsALender == null)
            return null;
        else

         */
            return Collections.unmodifiableList(LoansAsALender);
    }

    public List<String> getLoansAsABorrower() {
       /* if (LoansAsABorrower == null)
            return null;
        else

        */
            return Collections.unmodifiableList(LoansAsABorrower);
    }

    public String getName() {
        return name;
    }

    public int getMoneyInAccount() {
        return moneyInAccount;
    }

    public AccountTransactionDTO DepositMoney(int amount, int time){
        positiveTransaction depositMoney = new positiveTransaction(amount,time,moneyInAccount);
        moneyInAccount += amount;
        Transactions.add(depositMoney);
        return new AccountTransactionDTO(depositMoney);
    }

    public AccountTransactionDTO WithdrawMoney(int amount,int time){
        if(moneyInAccount < amount){
            return null;
        }
            NegativeTransaction withdrawMoney = new NegativeTransaction(amount, time, moneyInAccount);
            moneyInAccount -= amount;
            Transactions.add(withdrawMoney);
        return new AccountTransactionDTO(withdrawMoney);
    }

    public void PrintCustomerTransactions(){
        if(Transactions.size() == 0)
            System.out.println("There are no transactions for " + name + " for this moment.");
        else{
            System.out.println("This is the Transactions for " + name);
            for (AccountTransaction curTransaction : Transactions)
                curTransaction.PrintTransaction();
        }
    }

    public void makeAnInvestment(String loanName, int amount, int yaz){
        this.WithdrawMoney(amount, yaz);
        LoansAsALender.add(loanName);
    }
}

