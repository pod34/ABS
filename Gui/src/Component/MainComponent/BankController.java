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
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankController {



   @FXML private Button OkBt;
   @FXML private ComboBox<String> viewBy;
   @FXML private Label filePath;
   @FXML private Label CurrentYazLabel;
   @FXML private AnchorPane viewByAdmin;
   @FXML private AdminViewController viewByAdminController;
   @FXML private AnchorPane subComponent;
   @FXML private AnchorPane viewByCustomer;
   @FXML private CustomerViewController viewByCustomerController;
   @FXML private BorderPane mainContainerComponent;
   private SimpleStringProperty curCustomerViewBy;

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
   }

   public int getCurrentYaz(){
      return bankEngine.getCurrentYaz();
   }

   public Boolean checkIfCustomerHasEnoughMoneyToInvest(int amount){
     return bankEngine.checkIfCustomerHasEnoughMoneyToInvestByGivenAmount(curCustomerViewBy.getValue(),amount);
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

   public void activateLoansInlay(List<String> nameOfLoansToInvestIn,int amountOfInvestment,int maxOwnerShipOfTheLoan){
      if(bankEngine.LoansInlay(nameOfLoansToInvestIn,amountOfInvestment,curCustomerViewBy.getValue(),maxOwnerShipOfTheLoan) != null) {
         viewByAdminController.updateLoansInBankInAdminView();
      }
      else{
        //TODO errorLabel.setText("You can't invest more money than you have in your balance");
         //dialog.show();
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

   public List<LoanDTOs> getSystemCustomerLoansByListOfLoansName(List<String> i_LoansName){
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
      }
      else{
         if(!subComponent.equals(viewByAdmin))
            subComponent.getChildren().setAll(viewByAdmin);
      }
   }

   public void chargeActivation(int amount){
      viewByCustomerController.addTransactionToTransactionTable(bankEngine.DepositToAccount(amount, curCustomerViewBy.getValue()));
   }

   public void withdrawActivation(int amount){
      AccountTransactionDTO tmp = bankEngine.WithdrawFromTheAccount(amount, curCustomerViewBy.getValue());
      if(tmp != null)
         viewByCustomerController.addTransactionToTransactionTable(tmp);
      else {
         //TODO errorLabel.setText("You can't withdraw more money that you have in your balance");
         //dialog.show();
      }
   }

   public void increaseYazActivation(){
      bankEngine.IncreaseYaz();
   }

   public void fullyLoansPaymentActivation(List<String> loanNames){
      bankEngine.fullPaymentOnLoans(loanNames, curCustomerViewBy.getValue());
      viewByAdminController.updateLoansInBankInAdminView();
   }

   public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyOfLoansAsBorrower(String customerName){
      return bankEngine.getCustomerPropertyForLoanAsBorrower(customerName);
   }

   public Map<LoanStatus, SimpleStringProperty> getCustomerPropertyOfLoansAsLender(String customerName){
      return bankEngine.getCustomerPropertyForLoanAsLender(customerName);
   }

   public Map<String, SimpleStringProperty> getLoanDataByStatusPropertyAndStatusMapFromMainController(String loanName){
      return bankEngine.getLoanDataByStatusPropertyFromSystemMap(loanName);
   }

   public List<LoanDTOs> scrambleActivation(List<String> chosenCategories,int minDuration,int minInterestForSingleYaz,int i_maxOpenLoansForLoanOwner){
     return bankEngine.ActivationOfAnInlay(chosenCategories,minDuration,minInterestForSingleYaz,i_maxOpenLoansForLoanOwner,curCustomerViewBy.getValue());//TODO add more param later according to new filters
   }

   public CustomerDTOs getCustomerByName(String nameOfCustomer){
      return bankEngine.getCustomerByName(nameOfCustomer);
   }
}

