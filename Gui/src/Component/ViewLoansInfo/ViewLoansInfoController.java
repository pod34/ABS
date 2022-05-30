package Component.ViewLoansInfo;
import BankActions.Loan;
import BankActions.Payment;
import Component.MainComponent.BankController;
import DTOs.LoanDTOs;
import common.BankResourcesConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.controlsfx.control.table.TableRowExpanderColumn;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class ViewLoansInfoController {
    @FXML private GridPane LoansView;
    @FXML private Label PaymentFrequency;
    @FXML private Label intrest;
    @FXML private Label OriginalIntrestAmount;
    @FXML private Label TotalDuration;
    @FXML private Label originalAmount;
    @FXML private Label status;
    @FXML private Label LoanDataByStatus;
    @FXML private Accordion PaymentsDetails;
    @FXML private Accordion LenderDetails;
    @FXML private BankController mainController;


    public void setMainController(BankController mainController) {this.mainController = mainController;}

    public void buildLoansTableView(TableView<LoanDTOs> i_LoansData,List<LoanDTOs> allLoans){
        TableRowExpanderColumn<LoanDTOs> expanderColumn = new TableRowExpanderColumn<>(param -> {
            try {
                return expandLoanInfo(param);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        expanderColumn.setPrefWidth(45);

        TableColumn<LoanDTOs, String> nameOfLoan = new TableColumn<>("Name of Loan");
        nameOfLoan.setCellValueFactory(new PropertyValueFactory<>("nameOfLoan"));
        nameOfLoan.setPrefWidth(125);

        TableColumn<LoanDTOs, String> nameOfLoaner = new TableColumn<>("Name of loaner");
        nameOfLoaner.setCellValueFactory(new PropertyValueFactory<>("nameOfLoaner"));
        nameOfLoaner.setPrefWidth(125);

        TableColumn<LoanDTOs, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<LoanDTOs, String> category = new TableColumn<>("Category");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));


        i_LoansData.getColumns().addAll(expanderColumn, nameOfLoan, nameOfLoaner, category, status);
        i_LoansData.getItems().addAll(FXCollections.observableArrayList(allLoans));
    }

    private GridPane expandLoanInfo(TableRowExpanderColumn.TableRowDataFeatures<LoanDTOs> param) throws IOException {
        GridPane workSpace = new GridPane();
        workSpace.setHgap(10);
        workSpace.setVgap(5);

        LoanDTOs loan = param.getValue();

        String originalAmount = Integer.toString(loan.getOriginalAmount());
        String paymentFrequency = Integer.toString(loan.getPaymentFrequency());
        String interest = Integer.toString(loan.getInterest());
        String theTotalInterestAmountOfTheLoan =Float.
                toString(loan.getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar()
                        + loan.getTheInterestYetToBePaidOnTheLoan());
        String duration = Integer.toString(loan.getDurationOfTheLoan());
        String status = loan.getStatusName();

        FXMLLoader loader = new FXMLLoader();
        URL LoansViewFXML = getClass().getResource(BankResourcesConstants.VIEWLOANSDETAILEXPANDED_RESOURCE_IDENTIFIRE);
        loader.setLocation(LoansViewFXML);
        GridPane LoanExpandedDetails = loader.load();
        ViewLoansInfoController expendedLoansDetailsController = loader.getController();
        LoanExpandedDetails.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        expendedLoansDetailsController.setLoanDataByStatus(loan, mainController);
        expendedLoansDetailsController.setInterestValue(interest);
        expendedLoansDetailsController.setOriginalAmountValue(originalAmount);
        expendedLoansDetailsController.setPaymentFrequencyValue(paymentFrequency);
        expendedLoansDetailsController.setTotalDurationValue(duration);
        expendedLoansDetailsController.setOriginalInterestAmountValue(theTotalInterestAmountOfTheLoan);
        expendedLoansDetailsController.setStatusValue(status, loan.getNameOfLoan(), mainController);
        expendedLoansDetailsController.setLenderDetails(loan);
        expendedLoansDetailsController.setPaymentsDetails(loan);

        return LoanExpandedDetails;
    }

    public void setPaymentFrequencyValue(String paymentFrequencyVal) {
        PaymentFrequency.setText("Payment Frequency: " + paymentFrequencyVal);
    }

    public void setInterestValue(String i_interestVal) {
        intrest.setText("Interest: " + i_interestVal);
    }

    public void setOriginalInterestAmountValue(String i_originalInterestAmountVal) {
        OriginalIntrestAmount.setText("Total interest to be paid: " + i_originalInterestAmountVal);
    }

    public void setTotalDurationValue(String i_totalDurationVal) {
        TotalDuration.setText("Total Duration: " + i_totalDurationVal);
    }

    public void setOriginalAmountValue(String i_originalAmountVal) {
        originalAmount.setText("The original amount: " + i_originalAmountVal);
    }

    public void setStatusValue(String i_statusVal, String loanName, BankController i_mainController) {
        status.setText(i_statusVal);
        status.textProperty().bind(i_mainController.getLoanDataByStatusPropertyAndStatusMapFromMainController(loanName).get("statusProperty"));
    }

    public void setLenderDetails(LoanDTOs  Loan) {
        List<String> listOfLenders = (List<String>) Loan.getListOfLenders();
        TitledPane[] lenders = new TitledPane[listOfLenders.size()];
        for(int counter = 0; counter < listOfLenders.size(); counter++){
            lenders[counter] = new TitledPane(listOfLenders.get(counter),new Label("details to be added"));//TODO change details to be added to part of the loan this lender has
        }
        LenderDetails.getPanes().addAll(lenders);

    }

    public void setPaymentsDetails(LoanDTOs Loan){
        List<Payment> listOfPayments = Loan.getLoansPayments();
        TitledPane[] Payments = new TitledPane[listOfPayments.size()];
        int counter = 0;
        for(Payment curPayment : listOfPayments){
            Payments[counter] = new TitledPane(Integer.toString(curPayment.getPaymentDate()),new Label(curPayment.PaymentDetails()));//TODO we need to warp it in scroll pane
            counter++;
        }
        PaymentsDetails.getPanes().addAll(Payments);

    }

    public void setLoanDataByStatus(LoanDTOs loan, BankController i_mainController){
        switch (loan.getStatusName()){
            case "NEW":
                LoanDataByStatus.setText("0$ Raised so far ");
                break;
            case "PENDING":
                LoanDataByStatus.setText(/*TODO add method to loanDto how much money reised so far*/"___ $ Raised so far  " /* //TODO how much left to raise*/ + "____$ left to make the Loan active ");
                break;
            case "ACTIVE":
                LoanDataByStatus.setText("the yaz the loan became active. next payment yaz to pay, do we need to use enum here maya?");//TODO
                break;
            case "RISK":
                LoanDataByStatus.setText("Same data as active + to add here how much payment got delayed and how much do we need to pay asap");//TODO
                break;
            case "FINISHED":
                LoanDataByStatus.setText("Start YAZ ----->  End YAZ");//TODO
                break;
            default:
                break;

        }
        LoanDataByStatus.textProperty().bind(i_mainController.getLoanDataByStatusPropertyAndStatusMapFromMainController(loan.getNameOfLoan()).get("LoanDataByStatusProperty"));
    }
}

