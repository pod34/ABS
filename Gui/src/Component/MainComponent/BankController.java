package Component.MainComponent;
import BankActions.LoanStatus;
import BankSystem.BankSystem;
import Component.AdminView.AdminViewController;
import Component.CustomerView.CustomerViewController;
import DTOs.AccountTransactionDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import SystemExceptions.InccorectInputType;
import common.BankResourcesConstants;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankController {


   @FXML
   private Button OkBt;
   @FXML
   private ComboBox<String> viewBy;
   @FXML
   private Label filePath;
   @FXML
   private Label CurrentYazLabel;
   @FXML
   private AnchorPane viewByAdmin;
   @FXML
   private AdminViewController viewByAdminController;
   @FXML
   private AnchorPane subComponent;
   @FXML
   private AnchorPane viewByCustomer;
   @FXML
   private CustomerViewController viewByCustomerController;
   @FXML
   private BorderPane mainContainerComponent;
   private SimpleStringProperty curCustomerViewBy;
   @FXML
   private ComboBox<String> Skins;
   @FXML ImageView image;
   private Alert errorAlert = new Alert(Alert.AlertType.ERROR);


   private Stage primaryStage;
   BankSystem bankEngine;

   @FXML
   private void initialize() throws IOException {
      if (viewByAdminController != null) {
         viewByAdminController.setMainController(this);
      }
      filePath.textProperty().bind(viewByAdminController.getSelectedFileProperty());
      curCustomerViewBy = new SimpleStringProperty("Admin");
      FXMLLoader loader = new FXMLLoader();
      URL BankControllerFXML = getClass().getResource(BankResourcesConstants.ERRORPOPUPWINDOW);
      loader.setLocation(BankControllerFXML);
      filePath.setId("file-path-label");
      Skins.getItems().addAll("Blue mode", "Red mode");

   }

   public void setMainContainer(BorderPane main) {
      this.mainContainerComponent = main;
   }

   public int getCurrentYaz() {
      return bankEngine.getCurrentYaz();
   }

   public Boolean checkIfCustomerHasEnoughMoneyToInvest(int amount) {
      return bankEngine.checkIfCustomerHasEnoughMoneyToInvestByGivenAmount(curCustomerViewBy.getValue(), amount);
   }

   public void setBankEngine(BankSystem m_bankEngine) {
      bankEngine = m_bankEngine;
   }

   public void setPrimaryStage(Stage primaryStage) {
      this.primaryStage = primaryStage;
   }

   public void setCustomerController(AnchorPane customerScene) {
      this.viewByCustomer = customerScene;
      viewByCustomer.prefWidthProperty().bind(viewByAdmin.widthProperty());
      viewByCustomer.prefHeightProperty().bind(viewByAdmin.heightProperty());
      
   }

   public void setViewByCustomerController(CustomerViewController viewByCustomerController) {
      this.viewByCustomerController = viewByCustomerController;
      viewByCustomerController.setMainController(this);

   }

   public void activateLoansInlay(List<String> nameOfLoansToInvestIn, int amountOfInvestment, int maxOwnerShipOfTheLoan) {
      if (maxOwnerShipOfTheLoan <= 100) {
         if (bankEngine.LoansInlay(nameOfLoansToInvestIn, amountOfInvestment, curCustomerViewBy.getValue(), maxOwnerShipOfTheLoan) != null) {
            viewByAdminController.updateLoansInBankInAdminView();
         } else {
            errorAlert.setContentText("You can't invest more money than you have in your balance!");
            errorAlert.show();
         }
      }
   }
   

   public File ShowFileChooserDialog(FileChooser i_fileChooser) {
      return i_fileChooser.showOpenDialog(primaryStage);
   }

   public boolean LoadFileActivation() throws InccorectInputType {
      boolean flag = bankEngine.ReadingTheSystemInformationFile(filePath.getText());
      if (flag) {
         addCustomersToComboBox();
         viewByCustomerController.addCategoriesToScramble(bankEngine.getAllCategories().getCategories());
         viewByCustomerController.setDataOfCustomerTOPresentInCustomerView(bankEngine.getListOfDTOsCustomer());
         CurrentYazLabel.textProperty().bind(bankEngine.getYazProperty());

      }
      //Duration = 2.5 seconds
      Duration duration = Duration.millis(2500);
      //Create new rotate transition
      RotateTransition rotateTransition = new RotateTransition(duration, image);
      //Rotate by 200 degree
      rotateTransition.setByAngle(360);
      rotateTransition.play();
      return flag;
   }

   public void addCustomersToComboBox() {
      List<String> customersNames = new ArrayList<>();
      viewBy.getItems().add("Admin");
      List<CustomerDTOs> allCustomers = bankEngine.getListOfDTOsCustomer();
      for (CustomerDTOs curCustomer : allCustomers) {
         customersNames.add(curCustomer.getName());
      }
      viewBy.getItems().addAll(customersNames);
   }

   public List<LoanDTOs> getSystemLoans() {
      return bankEngine.getListOfLoansDTO();
   }

   public List<CustomerDTOs> getSystemCustomers() {
      return bankEngine.getListOfDTOsCustomer();
   }

   public List<LoanDTOs> getSystemCustomerLoansByListOfLoansName(List<String> i_LoansName) {
      return bankEngine.getListOfLoansDtoByListOfNamesOFLoans(i_LoansName);
   }

   public void setMessage(String customerName, int amount, String loanName, String msg) {
      viewByCustomerController.messageMaker(customerName, amount, loanName, msg);
   }

   @FXML
   private void viewByChooserClicked(ActionEvent event) {
      if (!viewBy.getValue().equals("Admin")) {
         curCustomerViewBy.set(viewBy.getValue());
         viewByCustomer.prefWidthProperty().bind(subComponent.widthProperty());
         viewByCustomer.prefHeightProperty().bind(subComponent.heightProperty());
         subComponent.getChildren().setAll(viewByCustomer);

         viewByCustomerController.setViewByCustomerData(viewBy.getValue());
         viewByCustomerController.setMessagesViewToCustomer(curCustomerViewBy.getValue());
         viewByCustomerController.updateTransactionToTransactionTable();
      }
      else{
         if(!subComponent.equals(viewByAdmin))
            subComponent.getChildren().setAll(viewByAdmin);
      }
   }



 

   public void withdrawActivation(int amount) {
      if(bankEngine.checkIfMoneyCanBeWithdraw(amount, curCustomerViewBy.getValue())) {
         AccountTransactionDTO tmp = bankEngine.WithdrawFromTheAccount(amount, curCustomerViewBy.getValue());
         if (tmp != null)
           viewByCustomerController.updateTransactionToTransactionTable();
      }
  else {
         errorAlert.setContentText("You cant withdraw more then what you have!");
         errorAlert.show();
      }
      viewByAdminController.updateLoansInBankInAdminView();
      viewByCustomerController.updateCustomersLoansData();
   }

   public void chargeActivation(int amount){
      AccountTransactionDTO tmp = bankEngine.DepositToAccount(amount,curCustomerViewBy.getValue());
      viewByAdminController.updateLoansInBankInAdminView();
      viewByCustomerController.updateCustomersLoansData();


   }


   public void increaseYazActivation() {
      bankEngine.IncreaseYaz();
      viewByCustomerController.updateCustomersLoansData();
      //Duration = 2.5 seconds
      Duration duration = Duration.millis(2500);
      //Create new translate transition
      TranslateTransition transition = new TranslateTransition(duration, image);
      //Move in X axis by +200
      transition.setByX(200);
      //Move in Y axis by +100
      transition.setByY(0);
      //Go back to previous position after 2.5 seconds
      transition.setAutoReverse(true);
      //Repeat animation twice
      transition.setCycleCount(2);
      transition.play();
   }

   public void fullyLoansPaymentActivation(List<String> loanNames) {
      bankEngine.fullPaymentOnLoans(loanNames, curCustomerViewBy.getValue());
      viewByAdminController.updateLoansInBankInAdminView();
      viewByCustomerController.updateCustomersLoansData();
      viewByCustomerController.updateTransactionToTransactionTable();

   }

   public void yazlyPaymentOfGivenLoansActivation(Map<String, Integer> loansToPayTo) {
      bankEngine.YazlyPaymentForGivenLoans(loansToPayTo);
      viewByCustomerController.updateCustomersLoansData();
      viewByAdminController.updateLoansInBankInAdminView();
      viewByCustomerController.updateTransactionToTransactionTable();

   }

   public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyOfLoansAsBorrower(String customerName) {
      return bankEngine.getCustomerPropertyForLoanAsBorrower(customerName);
   }

   public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyOfLoansAsLender(String customerName) {
      return bankEngine.getCustomerPropertyForLoanAsLender(customerName);
   }

   public Map<String, SimpleStringProperty> getLoanDataByStatusPropertyAndStatusMapFromMainController(String loanName) {
      return bankEngine.getLoanDataByStatusPropertyFromSystemMap(loanName);
   }

   public List<LoanDTOs> scrambleActivation(List<String> chosenCategories, int minDuration, int minInterestForSingleYaz, int i_maxOpenLoansForLoanOwner) {
      return bankEngine.ActivationOfAnInlay(chosenCategories, minDuration, minInterestForSingleYaz, i_maxOpenLoansForLoanOwner, curCustomerViewBy.getValue());//TODO add more param later according to new filters
   }

   public CustomerDTOs getCustomerByName(String nameOfCustomer) {
      return bankEngine.getCustomerByName(nameOfCustomer);
   }

   public List<String> checkWhatLoansCanBeFullyPaid(List<String> loanNames) {
      return bankEngine.checkWhatLoansCanBeFullyPaidSystem(loanNames, curCustomerViewBy.getValue());
   }

   @FXML
   private void skinsChooserClicked(ActionEvent event) {
      if (Skins.getValue().equals("Light mode")) {
         if (!mainContainerComponent.getStylesheets().equals("resources/lightMode.css")) {
            mainContainerComponent.getStylesheets().remove("resources/darkMode.css");
            viewByCustomer.getStylesheets().remove("resources/darkMode.css");
            viewByAdmin.getStylesheets().remove("resources/darkMode.css");
            mainContainerComponent.getStylesheets().add("resources/lightMode.css");
            viewByCustomer.getStylesheets().add("resources/lightMode.css");
            viewByAdmin.getStylesheets().add("resources/lightMode.css");

         }
      } else {
         if (!mainContainerComponent.getStylesheets().equals("resources/darkMode.css")) {
            mainContainerComponent.getStylesheets().remove("resources/lightMode.css");
            viewByCustomer.getStylesheets().remove("resources/lightMode.css");
            viewByAdmin.getStylesheets().remove("resources/lightMode.css");
            mainContainerComponent.getStylesheets().add("resources/darkMode.css");
            viewByCustomer.getStylesheets().add("resources/darkMode.css");
            viewByAdmin.getStylesheets().add("resources/darkMode.css");
         }
      }
   }

   public List<String> checkIfDividedLoansCanBePaid(Map<String, Integer> loansToPay){
      return bankEngine.checkIfCanPayAllLoans(loansToPay, curCustomerViewBy.getValue());
   }
}

