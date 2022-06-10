package BankActions;

import java.io.Serializable;

public class LeftToPay implements Serializable {
    private int originalAmount;
    private int amountLeftToPay;
    private int debt;
    private int amountOfPayments;
    private int debtInterest;

    public LeftToPay(int amountLeft, int amountOfPayment){
        originalAmount = amountLeft;
        this.amountLeftToPay = amountLeft;
        this.amountOfPayments = amountOfPayment;
        debt = 0;
        debtInterest = 0;
    }

    public void resetLeftToPayAfterClosingTheLoan(){
        amountLeftToPay = 0;
        resetDebt();
        amountOfPayments = 0;
    }

    public void setAmountLeftToPay() {
        if (amountLeftToPay != 0)
            amountLeftToPay -= amountLeftToPay / amountOfPayments;
    }

    public void setAmountOfPayment(){
        if(amountOfPayments != 1)
            amountOfPayments--;
    }

    public void resetDebt(){
        debt = 0;
        debtInterest = 0;
    }

    public int amountToPayThisYaz(){
        return (amountLeftToPay/amountOfPayments) + debt;
    }

    public int interestToPayThisYaz(int interest){
        return (((amountLeftToPay/amountOfPayments) * interest) /100 ) + debtInterest;
    }

    public void setDebt(int interest){
        this.debt = (amountLeftToPay/amountOfPayments) + debt;
        this.debtInterest = (((amountLeftToPay/amountOfPayments) * interest) /100 ) + debtInterest;
    }

    public void setOriginalAmount(int amount) {
        originalAmount += amount;
        amountLeftToPay += amount;
    }

    public int getAmountLeftToPayToCloseTheLoan(int interest) {
        return ((amountLeftToPay * interest) / 100) + amountLeftToPay  + debt + debtInterest;
    }


    public int getOriginalAmount() {
        return originalAmount;
    }
}
