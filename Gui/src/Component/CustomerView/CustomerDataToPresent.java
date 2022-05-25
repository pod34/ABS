package Component.CustomerView;

import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
import Costumers.Customer;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class CustomerDataToPresent {

    CustomerDTOs customer;
    private TableView<LoanDTOs> LoansAsLoanerData = new TableView<>();
    private TableView<LoanDTOs> LoansAsLenderData = new TableView<>();
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
    }

    public TableView<LoanDTOs> getLoansAsLoanerData() {
        return LoansAsLoanerData;
    }

    public TableView<LoanDTOs> getLoansAsLenderData() {
        return LoansAsLenderData;
    }
}
