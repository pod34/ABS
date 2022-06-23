package login;

import clientController.ClientController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label WelcomeLogin;
    @FXML
    private ImageView LogoLogin;
    @FXML
    private TextField userNameTextFiled;
    @FXML
    private Label errorMassage;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    private ClientController customerAppMainController;



    @FXML
    public void initialize() {
        errorMassage.textProperty().bind(errorMessageProperty);
        //TODO manage cookies for login (not sure what i need to do, take a 2 look in aviad login app)
    }


    @FXML
    void LoginClicked(ActionEvent event) {
        String userName = userNameTextFiled.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        //TODO update customerAppController what is the name of this current customer
                        //TODO Show main customer window
                    });
                }
            }
        });
    }


    @FXML
    void exitClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void userNameTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    public void setCustomerAppMainController(ClientController customerAppMainController) {
        this.customerAppMainController = customerAppMainController;
    }
}

