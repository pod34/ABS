package Component.CustomerView;
import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
import DTOs.AccountTransactionDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.StatusBar;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomerViewController {
    @FXML private ScrollPane LoanerInfoTable;
    @FXML private ScrollPane LoansInfoTable;
    @FXML private AnchorPane AccountTransInfo;
    @FXML private ScrollPane LoanerLoansTable;
    @FXML private ScrollPane PaymentControl;
    @FXML private VBox ChargeOrWithdraw;
    @FXML private ScrollPane NotificationsTable;
    @FXML private TableView LoansAsLender;
    @FXML private TableView LoansAsLoaner;
    @FXML private AnchorPane LoansAsLoanerTableForPaymentTab;
    @FXML private BorderPane customerViewBorderPane;
    @FXML private Label errorAmountToInvest;
    @FXML private Label howManyLoansFound;
    @FXML private TextField amountToInvest;
    @FXML private Button yazlyPayment;
    @FXML private CheckComboBox<String> categories;
    @FXML private TextField minimumYaz;
    @FXML private TextField maxOpenLoans;
    @FXML private TextField maxLoanOwner;
    private SimpleStringProperty howManyMatchingLoansFoundProp = new SimpleStringProperty("");
    @FXML private TextField minInterest;
    @FXML private Label errorMaxLoanOwner;
    @FXML private TableView relevantLoans;
    @FXML private CheckListView<String> checkLoansToInvest;
    @FXML private Button invest;
    @FXML private AnchorPane customerViewWindow;
    @FXML private StatusBar FindLoansProgress;
    @FXML private Button findLoans;
    private Map<String, CustomerDataToPresent> DataOfCustomerTOPresentInCustomerView = new HashMap<>();
    private Map<String, List<String>> messages;
    @FXML private BankController mainController;
    private Map<String, List<String>> notifications = new HashMap<>();
    @FXML private TextField AmountTB;
    @FXML private Button ChargeBT;
    @FXML private Button WithdrawBT;
    @FXML private ListView<String> notificationsView;
    @FXML private ListSelectionView<String> choosingLoans;//TODO:build this after building loans inlay
    @FXML private Button payFullyOnLoansBT;
    @FXML private Button resetSearch;
    @FXML private CheckBox selectAllLoansToInvest;
    @FXML private TableView<?> TransactionTable;
    @FXML private Label welcomeCustomer;
    @FXML private Label balanceOfCustomer;
    @FXML private Button fullPayment;
    private String curCustomerName;
    @FXML private Label loansInAbsLb;
    @FXML private Label customersInAbsLb;


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

        notificationsView.getItems().addAll(notifications.get(customerName));
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

        amountToInvest.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountToInvest.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        minimumYaz.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    minimumYaz.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        maxOpenLoans.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxOpenLoans.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        maxLoanOwner.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxLoanOwner.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        minInterest.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    minInterest.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        howManyLoansFound.textProperty().bind(howManyMatchingLoansFoundProp);
    }

    public void setMessagesViewToCustomer(String customerName) {
        if(!notificationsView.getItems().isEmpty())
            notificationsView.getItems().clear();
        ObservableList<String> items = null;
        if (notifications != null) {
            if (notifications.containsKey(customerName)) {
                items = FXCollections.observableArrayList(notifications.get(customerName));
            } else {
                items = FXCollections.observableArrayList("No messages to " + customerName);
            }
        }

        notificationsView.setItems(items);
    }

    public void setDataOfCustomerTOPresentInCustomerView(List<CustomerDTOs> i_bankCustomer){
        for(CustomerDTOs curCustomer : i_bankCustomer){
            DataOfCustomerTOPresentInCustomerView.put(curCustomer.getName(),new CustomerDataToPresent(curCustomer,mainController));
        }
    }

    public void setViewByCustomerData(String nameOfCustomer){
        CustomerDTOs curCustomer = mainController.getCustomerByName(nameOfCustomer);
        DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).updateLoansTables(curCustomer);
        setLenderLoans(nameOfCustomer);
        setLonerLoan(nameOfCustomer);
        setLoansAsLoanerForPaymentTab(nameOfCustomer);
        setAccountTransInfo(nameOfCustomer);
        curCustomerName = nameOfCustomer;
        welcomeCustomer.setText("Hello " + nameOfCustomer);
        //balanceOfCustomer.setText("Balance: " + );//TODO bind with current balance
    }

    public void updateCustomersLoansData(){
        for(Map.Entry<String,CustomerDataToPresent> curCustomerData : DataOfCustomerTOPresentInCustomerView.entrySet()){
            String nameOfCustomer = curCustomerData.getKey();
            CustomerDTOs curCustomer = mainController.getCustomerByName(nameOfCustomer);
            curCustomerData.getValue().updateLoansTables(curCustomer);
        }
        if(curCustomerName != null)
            setViewByCustomerData(curCustomerName);

    }

    private void setLoansAsLoanerForPaymentTab(String nameOfCustomer){
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLoanerDataForPaymentTab();
        tmp.prefWidthProperty().bind(LoansAsLoanerTableForPaymentTab.widthProperty());
        tmp.prefHeightProperty().bind(LoansAsLoanerTableForPaymentTab.heightProperty());
        LoansAsLoanerTableForPaymentTab.getChildren().setAll(tmp);
    }

    private void setLonerLoan(String nameOfCustomer){
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLoanerData();
        tmp.prefWidthProperty().bind(LoansAsLoaner.widthProperty());
        tmp.prefHeightProperty().bind(LoansAsLoaner.heightProperty());
        List<LoanDTOs> loans = tmp.getItems();
        if(LoansAsLoaner.getColumns().isEmpty()){
            ViewLoansInfoController tableBuilder = new ViewLoansInfoController();
            tableBuilder.buildLoansTableView(LoansAsLoaner,loans);
            LoansAsLoaner.refresh();
        }
        else{
            if(!LoansAsLoaner.getItems().isEmpty()){
                LoansAsLoaner.getItems().clear();
            }
            LoansAsLoaner.getItems().addAll(loans);
            LoansAsLoaner.refresh();
        }

    }

    private void setLenderLoans(String nameOfCustomer){
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLenderData();
        tmp.prefWidthProperty().bind(LoansAsLender.widthProperty());
        tmp.prefHeightProperty().bind(LoansAsLender.heightProperty());
        CustomerDTOs curCustomer =  mainController.getCustomerByName(nameOfCustomer);
        List<LoanDTOs> loans = tmp.getItems();
        if(LoansAsLoaner.getColumns().isEmpty()){
            ViewLoansInfoController tableBuilder = new ViewLoansInfoController();
            tableBuilder.buildLoansTableView(LoansAsLender,loans);
            LoansAsLender.refresh();
        }
        else{
            if(!LoansAsLender.getItems().isEmpty()){
                LoansAsLender.getItems().clear();
            }
            LoansAsLender.getItems().addAll(loans);
            LoansAsLender.refresh();
        }
    }

    private void setAccountTransInfo(String nameOfCustomer){
        TableView<AccountTransactionDTO> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getTransactionTable();
        tmp.prefWidthProperty().bind(AccountTransInfo.widthProperty());
        tmp.prefHeightProperty().bind(AccountTransInfo.heightProperty());
        AccountTransInfo.getChildren().setAll(tmp);
    }

    public void addCategoriesToScramble(List<String> i_categories){
        categories.getItems().addAll(i_categories);
    }

    private List<LoanDTOs> getRelevantLoansByUserParameters(){
        int investment,minYaz = 0,i_minInterest = 0,i_maxOpenLoansForLoanOwner = mainController.getSystemLoans().size();

        List<String> chosenCategories = categories.getCheckModel().getCheckedItems();
        if(chosenCategories.isEmpty()){
            chosenCategories = categories.getItems();
        }

        if(!minimumYaz.getText().isEmpty())
            minYaz = Integer.parseInt(minimumYaz.getText());

        if(!minInterest.getText().isEmpty())
            i_minInterest = Integer.parseInt(minInterest.getText());

        if(!(amountToInvest.getText().isEmpty()))
             investment = Integer.parseInt(amountToInvest.getText());
        if(!(maxOpenLoans.getText().isEmpty()))
            i_maxOpenLoansForLoanOwner = Integer.parseInt(maxOpenLoans.getText());
        return mainController.scrambleActivation(chosenCategories,minYaz,i_minInterest,i_maxOpenLoansForLoanOwner);
    }

    @FXML
    void findLoansBtClicked(ActionEvent event) {
        if(mainController.checkIfCustomerHasEnoughMoneyToInvest(Integer.parseInt(amountToInvest.getText()))) {
            disableFilterFields(true);
            startTask();
        }
        else{
            errorAmountToInvest.setText("You can't invest more than you have!");
            errorAmountToInvest.setStyle("-fx-text-fill: #e70d0d; -fx-font-size: 16px;");//TODO not visible after invesment reset
        }

    }

    private void disableFilterFields(boolean disable){
        amountToInvest.setDisable(disable);
        categories.setDisable(disable);
        minimumYaz.setDisable(disable);
        maxOpenLoans.setDisable(disable);
        maxLoanOwner.setDisable(disable);
        minInterest.setDisable(disable);
    }

    @FXML
    void investBtClicked(ActionEvent event) {
        int maxOwnerShipOfTheLoan = 100;
        if(!maxLoanOwner.getText().isEmpty()){
            maxOwnerShipOfTheLoan = Integer.parseInt(maxLoanOwner.getText());
        }
        mainController.activateLoansInlay(checkLoansToInvest.getCheckModel().getCheckedItems(),Integer.parseInt(amountToInvest.getText()),maxOwnerShipOfTheLoan);
        List<LoanDTOs> LoansCurCustomerInvestedIn = mainController.getSystemCustomerLoansByListOfLoansName(checkLoansToInvest.getCheckModel().getCheckedItems());
        checkLoansToInvest.getCheckModel().clearChecks();
        DataOfCustomerTOPresentInCustomerView.get(curCustomerName).updateLoansAsLender(LoansCurCustomerInvestedIn);
        resetScrambleTab();
    }

    void resetScrambleTab(){
        disableFilterFields(false);
        amountToInvest.clear();
        categories.getCheckModel().clearChecks();
        minimumYaz.clear();
        maxOpenLoans.clear();
        maxLoanOwner.clear();
        minInterest.clear();
        selectAllLoansToInvest.setSelected(false);
        checkLoansToInvest.getItems().clear();
        howManyMatchingLoansFoundProp.set("");
        errorAmountToInvest.setText("");
        relevantLoans.getItems().clear();

    }

    private void startTask() {
         Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                updateMessage("Looking for relevant Loans...");
                Thread.sleep(2500);
                int max = 100000;
                for (int i = 0; i < max; i++) {
                    if(i == 300)
                        updateMessage("Scanning Loans in ABS");
                    if(i % 1000 == 0)
                        updateMessage("finding Loans according your requirements");
                    updateProgress(i, max);
                }
                List<LoanDTOs> matchingLoans = getRelevantLoansByUserParameters();
                ViewLoansInfoController loansInfoController = new ViewLoansInfoController();
                loansInfoController.setMainController(mainController);
                loansInfoController.buildLoansTableView(relevantLoans,matchingLoans);
                howManyMatchingLoansFoundProp.set("Found " + matchingLoans.size() + " matching loans!");
                howManyLoansFound.setStyle("-fx-text-fill: #e70d0d; -fx-font-size: 16px;");//TODO not visible after invesment reset

                checkLoansToInvest.getItems().addAll(matchingLoans.stream().collect(Collectors.toMap(LoanDTOs::getNameOfLoan,loan -> loan)).
                        keySet().stream().collect(Collectors.toList()));

                updateProgress(0, 0);
                done();
                return null;
            }
        };

        FindLoansProgress.textProperty().bind(task.messageProperty());
        FindLoansProgress.progressProperty().bind(task.progressProperty());

        // remove bindings again
        task.setOnSucceeded(event -> {
            FindLoansProgress.textProperty().unbind();
            FindLoansProgress.progressProperty().unbind();
        });
        new Thread(task).start();
    }

    @FXML
    private void resetSearchBtClicked(ActionEvent event) {
        resetScrambleTab();
    }

    @FXML
    void fullPaymentClicked(ActionEvent event) {
        List<String> LoansToClose = DataOfCustomerTOPresentInCustomerView.get(curCustomerName).getLoansAsLoanerDataForPaymentTab().getItems().stream()
                .filter(L -> L.isSelected())
                .collect(Collectors.toMap(LoanDTOs::getNameOfLoan,loan -> loan))
                .keySet().stream().collect(Collectors.toList());
        mainController.fullyLoansPaymentActivation(LoansToClose);
    }

    @FXML
    private void selectAllLoansToInvestBtClicked(ActionEvent event) {
        if(selectAllLoansToInvest.isSelected())
            checkLoansToInvest.getCheckModel().checkAll();
        else{
            checkLoansToInvest.getCheckModel().clearChecks();
        }

    }

    public void updateTransactionToTransactionTable(){
        TableView<AccountTransactionDTO> tmp = (TableView<AccountTransactionDTO>) AccountTransInfo.getChildren().get(0);
        if(!tmp.getItems().isEmpty())
            tmp.getItems().clear();
        tmp.getItems().setAll(mainController.getCustomerByName(curCustomerName).getDtosTransactions());
        AccountTransInfo.getChildren().setAll(tmp);
    }

    @FXML
    void yazlyPaymentClicked(ActionEvent event) {
        List<LoanDTOs> loansToPay = DataOfCustomerTOPresentInCustomerView.get(curCustomerName).getLoansAsLoanerDataForPaymentTab().getItems().stream()
                .filter(L->L.getNextYazPayment() == mainController.getCurrentYaz()).collect(Collectors.toList());
        Map<String,Integer> loansToPayAndAmountOfPayment = new HashMap<>();
       for(LoanDTOs curLoan : loansToPay){
            loansToPayAndAmountOfPayment.put(curLoan.getNameOfLoan(),Integer.parseInt(curLoan.getAmountToPay()));
       }
       if(loansToPay.size() != 0)
           mainController.yazlyPaymentOfGivenLoansActivation(loansToPayAndAmountOfPayment);

  }
}




