package customerMainApp;

import clientController.ClientController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import login.LoginController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.StatusBar;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMainAppController extends ClientController {

    @FXML private AnchorPane customerViewWindow;
    @FXML private VBox ChargeOrWithdraw;
    @FXML private AnchorPane AccountTransInfo;
    @FXML private TextField AmountTB;
    @FXML private Button ChargeBT;
    @FXML private Button WithdrawBT;
    @FXML private Label welcomeCustomer;
    @FXML private Label balanceOfCustomer;
    @FXML private Label LoansAsLoanerLabel;
    @FXML private Label LoansAsLenderLabel;
    @FXML private TableView<?> LoansAsLoaner;
    @FXML private TableView<?> LoansAsLender;
    @FXML private TextField amountToInvest;
    @FXML private Label errorAmountToInvest;
    @FXML private CheckComboBox<?> categories;
    @FXML private TextField minimumYaz;
    @FXML private TextField maxOpenLoans;
    @FXML private TextField maxLoanOwner;
    @FXML private Label errorMaxLoanOwner;
    @FXML private TextField minInterest;
    @FXML private StatusBar FindLoansProgress;
    @FXML private CheckListView<?> checkLoansToInvest;
    @FXML private Button invest;
    @FXML private Label howManyLoansFound;
    @FXML private Button findLoans;
    @FXML private TableView<?> relevantLoans;
    @FXML private Button resetSearch;
    @FXML private CheckBox selectAllLoansToInvest;
    @FXML private ListView<?> notificationsView;
    @FXML private Button fullPayment;
    @FXML private Button yazlyPayment;
    @FXML private AnchorPane LoansAsLoanerTableForPaymentTab;
    @FXML private MenuBar customerMenuBar;
    @FXML private Menu fileOption;
    @FXML private Menu viewOption;
    @FXML private Menu skinOptions;
    @FXML private MenuItem blueSkin;
    @FXML private MenuItem redSkin;
    @FXML private Menu HelpOption;
    @FXML private MenuItem openReadMeFile;
    @FXML private RadioMenuItem animationsBtn;
    private Stage primaryStage;
    private Scene customerAppScene;
    private Scene LoginScene;
    private SimpleStringProperty howManyMatchingLoansFoundProp = new SimpleStringProperty("");
    //private Map<String, CustomerDataToPresent> DataOfCustomerTOPresentInCustomerView = new HashMap<>();
    private Map<String, List<String>> messages;
    private String curCustomerName;
    private Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    LoginController loginController;

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


    @FXML
    void LoadFxmlClicked(ActionEvent event) {

    }


    @FXML
    void blueSkinClicked(ActionEvent event) {

    }

    @FXML
    void redSkinClicked(ActionEvent event) {

    }

    @FXML
    void chargeClicked(ActionEvent event) {
        if (!AmountTB.getText().trim().isEmpty()) {
            String AmountToCharge = AmountTB.getText();
            String finalUrl = HttpUrl
                    .parse(Constants.ChargeAccount)
                    .newBuilder()
                    .addQueryParameter("transactionAmount", AmountToCharge)
                    .addQueryParameter("typeOfTransaction","DEPOSIT")
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                   // TODO maya add alert window with failure msg
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        //Platform.runLater(() ->
                                //TODO maya alert window
                       // );
                    } else {
                        Platform.runLater(() -> {

                        });
                    }
                }
            });

        }
        AmountTB.clear();
        AmountTB.setText(AmountTB.getText());
    }

    @FXML
    void withdrawClicked(ActionEvent event) {

    }

    @FXML
    void findLoansBtClicked(ActionEvent event) {

    }

    @FXML
    void fullPaymentClicked(ActionEvent event) {

    }

    @FXML
    void investBtClicked(ActionEvent event) {

    }

    @FXML
    void logoutClicked(ActionEvent event) {

    }

    @FXML
    void openReadMeClicked(ActionEvent event) {

    }

    @FXML
    void resetSearchBtClicked(ActionEvent event) {

    }

    @FXML
    void selectAllLoansToInvestBtClicked(ActionEvent event) {

    }

    @FXML
    void yazlyPaymentClicked(ActionEvent event) {

    }

    @FXML
    void animationClicked(ActionEvent event){}

    @Override
    public void setPrimaryStage(Stage i_primaryStage) {
        this.primaryStage = i_primaryStage;
    }

    @Override
    public void switchToClientApp() {
        primaryStage.setScene(customerAppScene);
        primaryStage.show();
    }

    public void switchToLoginScene() {
        primaryStage.setScene(LoginScene);
        primaryStage.show();
    }//TODO

    @Override
    public void setCurrentClientUserName(String userName) {
        curCustomerName = userName;
    }

    @Override
    public void setRootPane(Scene i_customerAppScene) {
        customerAppScene = i_customerAppScene;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
