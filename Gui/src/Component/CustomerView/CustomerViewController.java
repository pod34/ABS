package Component.CustomerView;
import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.ListSelectionView;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.util.*;

public class CustomerViewController {
    @FXML private ScrollPane LoanerInfoTable;
    @FXML private ScrollPane LoansInfoTable;
    @FXML private ScrollPane AccountTransInfo;
    @FXML private ScrollPane LoanerLoansTable;
    @FXML private ScrollPane PaymentControl;
    @FXML private ScrollPane NotificationsTable;
    @FXML private AnchorPane LoansAsLender;
    @FXML private AnchorPane LoansAsLoaner;
    private Map<String, CustomerDataToPresent> DataOfCustomerTOPresentInCustomerView = new HashMap<>();

    private Map<String, List<String>> messages;
    @FXML private BankController mainController;
    private Map<String, CustomerDataToPresent> DataOfCustomerTOPresentInCustomerView = new HashMap<>();
    private Map<String, List<String>> notifications;
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

    public void setDataOfCustomerTOPresentInCustomerView(List<CustomerDTOs> i_bankCustomer){
        for(CustomerDTOs curCustomer : i_bankCustomer){
            DataOfCustomerTOPresentInCustomerView.put(curCustomer.getName(),new CustomerDataToPresent(curCustomer,mainController));
        }
    }

    public void setViewByCustomerData(String nameOfCustomer){
        setLenderLoans(nameOfCustomer);
        setLonerLoan(nameOfCustomer);
    }

    private void setLonerLoan(String nameOfCustomer){
       // LoanerLoan = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLoanerData();
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLoanerData();
        LoansAsLoaner.getChildren().setAll(tmp);

    }

    private void setLenderLoans(String nameOfCustomer){
       // lenderLoans = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLenderData();
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLenderData();
        LoansAsLender.getChildren().setAll(tmp);

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
    void withdrawClicked(ActionEvent event) {//TODO: add limit check cant withdraw more than customer have
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

    public void setDataOfCustomerTOPresentInCustomerView(List<CustomerDTOs> i_bankCustomer){
        for(CustomerDTOs curCustomer : i_bankCustomer){
            DataOfCustomerTOPresentInCustomerView.put(curCustomer.getName(),new CustomerDataToPresent(curCustomer,mainController));
        }
    }

    public void setViewByCustomerData(String nameOfCustomer){
        setLenderLoans(nameOfCustomer);
        setLonerLoan(nameOfCustomer);
    }

    private void setLonerLoan(String nameOfCustomer){
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLoanerData();
        LoansAsLoaner.getChildren().setAll(tmp);

    }

    private void setLenderLoans(String nameOfCustomer){
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLenderData();
        LoansAsLender.getChildren().setAll(tmp);

    }

}




