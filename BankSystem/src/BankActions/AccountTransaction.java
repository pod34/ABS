package BankActions;

import java.io.Serializable;

public abstract class AccountTransaction  implements Serializable {
   protected int timeOfAction;
   protected int amountOfTransaction;
   protected int amountBefore;
   protected int amountAfter;

    protected AccountTransaction() {
    }

    abstract public void Transaction();

    public int getTimeOfAction() {
        return timeOfAction;
    }

    public int amountOfTransaction() {
        return amountOfTransaction;
    }

    public int getAmountBefore() {
        return amountBefore;
    }

    public int getAmountAfter() {
        return amountAfter;
    }

    public int getAmountOfTransaction() {
        return amountOfTransaction;
    }

    abstract public String getSignOfTransaction();

    abstract public void setAmountAfter(int amountAfter);

    public void PrintTransaction(){
        System.out.println("Time of transaction: " + timeOfAction);
        System.out.println("The amount of transaction " + amountOfTransaction);
        System.out.println("Was the transaction negative(-) or positive (+): " + this.getSignOfTransaction());
        System.out.println("The amount before: " + amountBefore);
        System.out.println("The amount after: " + amountAfter);
    }

}

