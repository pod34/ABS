package Component.CustomerView;
import Component.MainComponent.BankController;
import DTOs.LoanDTOs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerViewController {
    @FXML private ScrollPane LoanerInfoTable;
    @FXML private ScrollPane LoansInfoTable;
    @FXML private ScrollPane AccountTransInfo;
    @FXML private ScrollPane LoanerLoansTable;
    @FXML private ScrollPane PaymentControl;
    @FXML private ScrollPane NotificationsTable;
    @FXML private TableView<LoanDTOs> LoanerLoan;
    @FXML private TableView<LoanDTOs> lenderLoans;
    private Map<String, List<String>> notifications;
    @FXML private BankController mainController;
    @FXML private TextField AmountTB;
    @FXML private Button ChargeBT;
    @FXML private Button WithdrawBT;
    @FXML private ListView<String> notificationsView;
    @FXML private ListSelectionView<String> choosingLoans = new ListSelectionView<>();//TODO:build this after building loans inlay
    @FXML private Button payFullyOnLoansBT;

    public void setMainController(BankController mainController) {
        this.mainController = mainController;
    }

    public void messageMaker(String customerName, int amount, String loanName, String msg) {
        StringBuilder message = new StringBuilder();
        message.append("Hello " + customerName + "\n" + msg + loanName);
        message.append("\n" + "The amount for payment is: " + amount);
        if (notifications.containsKey(customerName))
            notifications.get(customerName).add(msg.toString());
        else
            notifications.put(customerName, new ArrayList<>(Collections.singleton(message.toString())));
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
    void withdrawClicked(ActionEvent event) {//TODO: add limit check
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

    public void setMessagesViewToCustomer(String customerName) {
        notificationsView.getItems().clear();
        ObservableList<String> items = null;
        if (notifications != null) {
            if (notifications.containsKey(customerName))
                items = FXCollections.observableArrayList(notifications.get(customerName));
        }
        else
            items = FXCollections.observableArrayList("No messages to " + customerName);

        notificationsView.setItems(items);
    }

    @FXML
    public void paySelectedLoansClicked(ActionEvent event) {
        List<String> loanNames = choosingLoans.getTargetItems().stream().collect(Collectors.toList());
        mainController.fullyLoansPaymentActivation(loanNames);
    }
}




