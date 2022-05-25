package Component.MainComponent;
import BankActions.Loan;
import BankSystem.BankSystem;
import Component.AdminView.AdminViewController;
import Component.CustomerView.CustomerViewController;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import SystemExceptions.InccorectInputType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class BankController {

   @FXML private ComboBox<String> viewBy;
   @FXML private Label filePath;
   @FXML private Label CurrentYazLabel;
   @FXML private AnchorPane viewByAdmin;
   @FXML private AdminViewController viewByAdminController;
   @FXML private AnchorPane subComponent;
   @FXML private AnchorPane viewByCustomer;
   @FXML private CustomerViewController viewByCustomerController;
   private SimpleStringProperty curCustomerViewBy;


   private Stage primaryStage;
   BankSystem bankEngine;


   public void setBankEngine(BankSystem m_bankEngine) {
      bankEngine = m_bankEngine;
   }

   public void setPrimaryStage(Stage primaryStage) {
      this.primaryStage = primaryStage;
   }

   public void setCustomerController(AnchorPane customerScene) {
      this.viewByCustomer = customerScene;
   }

   public void setViewByCustomerController(CustomerViewController viewByCustomerController) {
      this.viewByCustomerController = viewByCustomerController;
      viewByCustomerController.setMainController(this);
   }

   @FXML
   private void initialize() {
      if (viewByAdminController != null) {
         viewByAdminController.setMainController(this);
      }
      filePath.textProperty().bind(viewByAdminController.getSelectedFileProperty());
      curCustomerViewBy = new SimpleStringProperty("Admin");
   }

   public File ShowFileChooserDialog(FileChooser i_fileChooser) {
      return i_fileChooser.showOpenDialog(primaryStage);
   }

   public boolean LoadFileActivation() throws InccorectInputType {
      boolean flag = bankEngine.ReadingTheSystemInformationFile(filePath.getText());
      if (flag) {
         addCustomersToComboBox();
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
         subComponent.getChildren().setAll(viewByCustomer);
         viewByCustomerController.setMessagesViewToCustomer(curCustomerViewBy.getValue());

      }
      else{
         if(!subComponent.equals(viewByAdmin))
            subComponent.getChildren().setAll(viewByAdmin);
      }
   }

   public void chargeActivation(int amount){
      bankEngine.DepositToAccount(amount, curCustomerViewBy.getValue());
   }

   public void withdrawActivation(int amount){
      bankEngine.DepositToAccount(amount, curCustomerViewBy.getValue());
   }
}

