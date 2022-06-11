package Main;

import BankSystem.BankSystem;
import BankSystem.SystemImplement;
import Component.CustomerView.CustomerViewController;
import Component.MainComponent.BankController;
import Component.PopUpWindows.ErrorWindow;
import common.BankResourcesConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.cssfx.CSSFX;

import java.net.URL;

public class TaskMain extends Application {

    public static void main(String[] args) {
    
      launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CSSFX.start();

        FXMLLoader loader = new FXMLLoader();
        URL BankControllerFXML = getClass().getResource(BankResourcesConstants.BANKCONTROLLER_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(BankControllerFXML);
        BorderPane root = loader.load();

        BankController bankController = loader.getController();
        BankSystem bankEngine = new SystemImplement(bankController);
        bankController.setBankEngine(bankEngine);
        bankController.setPrimaryStage(primaryStage);

        FXMLLoader customerLoader = new FXMLLoader();
        URL CustomerControllerFXML = getClass().getResource(BankResourcesConstants.CUSTOMERCONTROLLER_FXML_RESOURCE_IDENTIFIER);
        customerLoader.setLocation(CustomerControllerFXML);
        //TODO: set the controller and load/
        AnchorPane customerRoot = customerLoader.load();
        CustomerViewController customerViewController = customerLoader.getController();
        bankController.setCustomerController(customerRoot);
        bankController.setViewByCustomerController(customerViewController);

        primaryStage.setTitle("Alternative Banking System");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
