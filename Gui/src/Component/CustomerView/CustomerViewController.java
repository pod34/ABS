package Component.CustomerView;
import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
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
    @FXML private ScrollPane AccountTransInfo;
    @FXML private ScrollPane LoanerLoansTable;
    @FXML private ScrollPane PaymentControl;
    @FXML private VBox ChargeOrWithdraw;
    @FXML private ScrollPane NotificationsTable;
    @FXML private AnchorPane LoansAsLender;
    @FXML private AnchorPane LoansAsLoaner;
    @FXML private BorderPane customerViewBorderPane;
    @FXML private Label errorAmountToInvest;
    @FXML private Label howManyLoansFound;
    @FXML private TextField amountToInvest;
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
    private Map<String, List<String>> notifications;
    @FXML private TextField AmountTB;
    @FXML private Button ChargeBT;
    @FXML private Button WithdrawBT;
    @FXML private ListView<String> notificationsView;
    @FXML private ListSelectionView<String> choosingLoans = new ListSelectionView<>();//TODO:build this after building loans inlay
    @FXML private Button payFullyOnLoansBT;
    @FXML private Button resetSearch;
    @FXML private CheckBox selectAllLoansToInvest;


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
        tmp.prefWidthProperty().bind(LoansAsLoaner.widthProperty());
        tmp.prefHeightProperty().bind(LoansAsLoaner.heightProperty());

        LoansAsLoaner.getChildren().setAll(tmp);

    }

    private void setLenderLoans(String nameOfCustomer){
        TableView<LoanDTOs> tmp = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLenderData();
        tmp.prefWidthProperty().bind(LoansAsLender.widthProperty());
        tmp.prefHeightProperty().bind(LoansAsLender.heightProperty());
        LoansAsLender.getChildren().setAll(tmp);

    }

    public void addCategoriesToScramble(List<String> i_categories){
        categories.getItems().addAll(i_categories);
    }

    private List<LoanDTOs> getRelevantLoansByUserParameters(){
        int invesment,minYaz = 0,i_minInterest = 0,i_maxOpenLoansForLoanOwner = mainController.getSystemLoans().size();

        List<String> chosenCategories = categories.getCheckModel().getCheckedItems();
        if(chosenCategories.isEmpty()){
            chosenCategories = categories.getItems();
        }

        if(!minimumYaz.getText().isEmpty())
            minYaz = Integer.parseInt(minimumYaz.getText());

        if(!minInterest.getText().isEmpty())
            i_minInterest = Integer.parseInt(minInterest.getText());

        if(!(amountToInvest.getText().isEmpty()))
             invesment = Integer.parseInt(amountToInvest.getText());
        if(!(maxOpenLoans.getText().isEmpty()))
            i_maxOpenLoansForLoanOwner = Integer.parseInt(maxOpenLoans.getText());

        return mainController.scrambleActivation(chosenCategories,minYaz,i_minInterest,i_maxOpenLoansForLoanOwner);
    }

    @FXML
    void findLoansBtClicked(ActionEvent event) {
        disableFilterFields(true);
        startTask();
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
    void resetSearchBtClicked(ActionEvent event) {
        resetScrambleTab();
    }

    @FXML
    void selectAllLoansToInvestBtClicked(ActionEvent event) {
        if(selectAllLoansToInvest.isSelected())
            checkLoansToInvest.getCheckModel().checkAll();
        else{
            checkLoansToInvest.getCheckModel().clearChecks();
        }

    }

}




