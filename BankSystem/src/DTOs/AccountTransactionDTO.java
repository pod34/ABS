package DTOs;

import BankActions.AccountTransaction;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class AccountTransactionDTO implements Serializable {

    private SimpleBooleanProperty TransactionType = new SimpleBooleanProperty(this, "TransactionType");//to do change it to + or - for now true is possitive (+)
    private SimpleIntegerProperty amount = new SimpleIntegerProperty(this, "amount");
    private SimpleIntegerProperty yazOfAction = new SimpleIntegerProperty(this, "yazOfAction");
    private SimpleIntegerProperty previousBalance = new SimpleIntegerProperty(this, "previousBalance");
    private SimpleIntegerProperty curBalance = new SimpleIntegerProperty(this, "curBalance");

    public AccountTransactionDTO(AccountTransaction Transaction){
        amount.set(Transaction.getAmountOfTransaction());
        yazOfAction.set(Transaction.getTimeOfAction());
        previousBalance.set(Transaction.getAmountBefore());
        curBalance.set(Transaction.getAmountAfter());
        if(previousBalance.get() < curBalance.get()){
            TransactionType.set(true);
        }
        else {
            TransactionType.set(false);
        }
    }

    public boolean isTransactionType() {
        return TransactionType.get();
    }

    public SimpleBooleanProperty transactionTypeProperty() {
        return TransactionType;
    }

    public int getAmount() {
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public int getYazOfAction() {
        return yazOfAction.get();
    }

    public SimpleIntegerProperty yazOfActionProperty() {
        return yazOfAction;
    }

    public int getPreviousBalance() {
        return previousBalance.get();
    }

    public SimpleIntegerProperty previousBalanceProperty() {
        return previousBalance;
    }

    public int getCurBalance() {
        return curBalance.get();
    }

    public SimpleIntegerProperty curBalanceProperty() {
        return curBalance;
    }
}
