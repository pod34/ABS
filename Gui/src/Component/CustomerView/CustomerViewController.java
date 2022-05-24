package Component.CustomerView;
import BankActions.Loan;
import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import javafx.fxml.FXML;
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
    @FXML private AnchorPane LoansAsLoaner;
    @FXML private AnchorPane LoansAsLender;

    private Map<String, List<String>> messages;
    @FXML private BankController mainController;
    private Map<String, CustomerDataToPresent> DataOfCustomerTOPresentInCustomerView = new HashMap<>();

    public void setMainController(BankController mainController) {
        this.mainController = mainController;
    }

    public void messageMaker(String customerName, int amount, String loanName, String msg){
        StringBuilder message = new StringBuilder();
        message.append("Hello " + customerName + "\n" + msg + loanName);
        message.append("\n" + "The amount for payment is: " + amount);
        if (messages.containsKey(customerName))
            messages.get(customerName).add(msg.toString());
        else
            messages.put(customerName, new ArrayList<>(Collections.singleton(message.toString())));
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
        LoansAsLoaner.getChildren().setAll(DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLoanerData());

    }

    private void setLenderLoans(String nameOfCustomer){
       // lenderLoans = DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLenderData();
        LoansAsLender.getChildren().setAll(DataOfCustomerTOPresentInCustomerView.get(nameOfCustomer).getLoansAsLenderData());
    }

}



