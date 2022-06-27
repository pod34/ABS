package Costumers;

import BankActions.*;
import DTOs.AccountTransactionDTO;

import java.io.Serializable;
import java.util.*;

public class Customer implements Serializable {
    private String name;
    private int moneyInAccount = 0;
    private List<String> LoansAsALender;
    private List<String> LoansAsABorrower;
    private List<AccountTransaction> Transactions;
    private List<String> notifications = new ArrayList<>();

    public List<String> getNotifications() {
        return notifications;
    }

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


    public void makeAnInvestment(String loanName, int amount, int yaz){
        this.WithdrawMoney(amount, yaz);
        if(!LoansAsALender.contains(loanName)){
            LoansAsALender.add(loanName);
        }
    }
}

