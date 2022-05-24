package Component.CustomerView;
import BankActions.Loan;
import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
import DTOs.LoanDTOs;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomerViewController {
    @FXML private ScrollPane LoanerInfoTable;
    @FXML private ScrollPane LoansInfoTable;
    @FXML private ScrollPane AccountTransInfo;
    @FXML private ScrollPane LoanerLoansTable;
    @FXML private ScrollPane PaymentControl;
    @FXML private ScrollPane NotificationsTable;
    @FXML private TableView<LoanDTOs> LoanerLoan;
    @FXML private TableView<LoanDTOs> lenderLoans;
    private Map<String, List<String>> messages;
    @FXML private BankController mainController;
    @FXML private TextField AmountTB;
    @FXML private Button ChargeBT;
    @FXML private Button WithdrawBT;

    public void setMainController(BankController mainController) {
        this.mainController = mainController;
    }

    public void messageMaker(String customerName, int amount, String loanName, String msg) {
        StringBuilder message = new StringBuilder();
        message.append("Hello " + customerName + "\n" + msg + loanName);
        message.append("\n" + "The amount for payment is: " + amount);
        if (messages.containsKey(customerName))
            messages.get(customerName).add(msg.toString());
        else
            messages.put(customerName, new ArrayList<>(Collections.singleton(message.toString())));
    }

    @FXML
    void chargeClicked(ActionEvent event) {
        if (!AmountTB.getText().trim().isEmpty()) {
            mainController.chargeActivation(Integer.parseInt(AmountTB.getText()));
        }
        AmountTB.clear();
        AmountTB.setText(AmountTB.getText());
    }

    @FXML
    void withdrawClicked(ActionEvent event) {
        if (!AmountTB.getText().trim().isEmpty()) {
            mainController.withdrawActivation(Integer.parseInt(AmountTB.getText()));
        }
        AmountTB.clear();
        AmountTB.setText(AmountTB.getText());
    }

    @FXML
    private void initialize() {
        AmountTB.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    AmountTB.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}




