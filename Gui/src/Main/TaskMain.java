package Main;

import BankSystem.BankSystem;
import BankSystem.SystemImplement;
import Component.CustomerView.CustomerViewController;
import Component.MainComponent.BankController;
import common.BankResourcesConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class TaskMain extends Application {

    public static void main(String[] args) {
    
      launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL BankControllerFXML = getClass().getResource(BankResourcesConstants.BANKCONTROLLER_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(BankControllerFXML);
        BorderPane root = loader.load();

        BankController bankController = loader.getController();
        BankSystem bankEngine = new SystemImplement(bankController);
        bankController.setBankEngine(bankEngine);
        bankController.setPrimaryStage(primaryStage);

        FXMLLoader customerLoder = new FXMLLoader();
        URL CustomerControllerFXML = getClass().getResource(BankResourcesConstants.CUSTOMERCONTROLLER_FXML_RESOURCE_IDENTIFIER);
        customerLoder.setLocation(CustomerControllerFXML);
        //TODO: set the controller and load/
        AnchorPane customerRoot = customerLoder.load();
        CustomerViewController customerViewController = customerLoder.getController();
        bankController.setCustomerController(customerRoot);
        bankController.setViewByCustomerController(customerViewController);

        primaryStage.setTitle("Alternative Banking System");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
