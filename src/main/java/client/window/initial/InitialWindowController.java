package client.window.initial;

import client.application.ControllerLoader;
import client.network.queries.AuthorizationQuery;
import client.window.main.MainWindowStarter;
import client.window.registration.RegistrationController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class InitialWindowController {
    private static final String FILE_NAME = "SignInWindow.fxml";
    @FXML
    private TextField login, password;
    @FXML
    private CheckBox remember;
    @FXML
    private Label errorLabel;




    @FXML
    private void onSignIn()
    {
        try {
            AuthorizationQuery.sendLoginAndPassword(login.getText(), password.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSignUp()
    {
        RegistrationController.startRegistration();
    }
    public static Parent create() throws IOException {
        return ControllerLoader.create(InitialWindowController.class.getResource(FILE_NAME));
    }

}
