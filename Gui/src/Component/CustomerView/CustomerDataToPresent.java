package Component.CustomerView;

import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
import Costumers.Customer;
import DTOs.AccountTransactionDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class CustomerDataToPresent {

    CustomerDTOs customer;
    private TableView<LoanDTOs> LoansAsLoanerData = new TableView<>();
    private TableView<LoanDTOs> LoansAsLenderData = new TableView<>();
    private TableView<AccountTransactionDTO> TransactionTable = new TableView<>();
    //private TableView<accountTransactions> transactions;
    ViewLoansInfoController loansInfoController = new ViewLoansInfoController();

    public CustomerDataToPresent(CustomerDTOs customer, BankController mainController) {
        //this.LoansAsLoanerData = loansAsLoanerData;
        //this.LoansAsLenderData = loansAsLenderData;
        this.customer = customer;
        List<LoanDTOs> lst = new ArrayList<>();
        loansInfoController.setMainController(mainController);
        loansInfoController.buildLoansTableView(LoansAsLoanerData,mainController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsABorrower()));
        loansInfoController.buildLoansTableView(LoansAsLenderData,mainController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsALender()));
        buildTransactionsTable(customer.getTransactions());

    }

    public TableView<LoanDTOs> getLoansAsLoanerData() {
        return LoansAsLoanerData;
    }

    public TableView<LoanDTOs> getLoansAsLenderData() {
        return LoansAsLenderData;
    }

    public TableView<AccountTransactionDTO> getTransactionTable() {
        return TransactionTable;
    }

    private void buildTransactionsTable(List<AccountTransactionDTO> customerTransactions ){

        TableColumn<AccountTransactionDTO, String> TransactionType = new TableColumn<>("Type Of Transaction");
        TransactionType.setCellValueFactory(new PropertyValueFactory<>("TransactionType"));
        TransactionType.setPrefWidth(125);

        TableColumn<AccountTransactionDTO, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //amount.setPrefWidth(50);

        TableColumn<AccountTransactionDTO, String> yazOfAction = new TableColumn<>("Yaz of action");
        yazOfAction.setCellValueFactory(new PropertyValueFactory<>("yazOfAction"));
        yazOfAction.setPrefWidth(120);

        TableColumn<AccountTransactionDTO, String> previousBalance = new TableColumn<>("Previous balance");
        previousBalance.setCellValueFactory(new PropertyValueFactory<>("previousBalance"));
        previousBalance.setPrefWidth(120);

        TableColumn<AccountTransactionDTO, String> curBalance = new TableColumn<>("Current balance");
        curBalance.setCellValueFactory(new PropertyValueFactory<>("curBalance"));
        curBalance.setPrefWidth(110);

        TransactionTable.getColumns().addAll(TransactionType, amount, yazOfAction, previousBalance,curBalance);
        TransactionTable.getItems().addAll(FXCollections.observableArrayList(customerTransactions));
    }
}
