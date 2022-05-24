package Costumers;

import BankActions.AccountTransaction;
import BankActions.NegativeTransaction;
import BankActions.positiveTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer implements Serializable {
    private String name;
    private int moneyInAccount = 0;
    private List<String> LoansAsALender;
    private List<String> LoansAsABorrower;
    private List<AccountTransaction> Transactions;

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

    public void DepositMoney(int amount,int time){
        positiveTransaction depositMoney = new positiveTransaction(amount,time,moneyInAccount);
        moneyInAccount += amount;
        Transactions.add(depositMoney);
    }

    public boolean WithdrawMoney(int amount,int time){
        if(moneyInAccount < amount){
            return false;
        }
        else {
            NegativeTransaction withdrawMoney = new NegativeTransaction(amount, time, moneyInAccount);
            moneyInAccount -= amount;
            Transactions.add(withdrawMoney);
        }
        return true;
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

