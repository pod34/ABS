package DTOs;

import BankActions.AccountTransaction;

import java.io.Serializable;

public class AccountTransactionDTO implements Serializable {
    protected final int timeOfAction;
    protected final int amountOfTransaction;
    protected final int amountBefore;
    protected final int amountAfter;


    public AccountTransactionDTO(AccountTransaction Transaction){
        timeOfAction = Transaction.getTimeOfAction();
        amountOfTransaction = Transaction.getAmountOfTransaction();
        amountBefore = Transaction.getAmountBefore();
        amountAfter = Transaction.getAmountAfter();
    }

    public int getTimeOfAction() {
        return timeOfAction;
    }

    public int getAmountOfTransaction() {
        return amountOfTransaction;
    }

    public int getAmountBefore() {
        return amountBefore;
    }

    public int getAmountAfter() {
        return amountAfter;
    }
}
