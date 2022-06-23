package main;

import common.CustomerBankResourcesConstans;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import login.LoginController;
import org.fxmisc.cssfx.CSSFX;

import java.net.URL;

public class CustomerClientMain extends Application {
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CSSFX.start();

        FXMLLoader loginLoader = new FXMLLoader();
        URL LoginLoaderFXML = getClass().getResource(CustomerBankResourcesConstans.LOGINCONTOLLER_FXML_RESOURCE_IDENTIFIER);
        loginLoader.setLocation(LoginLoaderFXML);
        GridPane customerRoot = loginLoader.load();
        LoginController customerViewController = loginLoader.getController();

        primaryStage.setTitle("Alternative Banking System");
        Scene scene = new Scene(customerRoot, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
