package client.window.registration;

import client.application.ControllerLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FirstPageController {
    private static final String FILE_NAME = "FirstPageForm.fxml";
    @FXML
    private TextField login, firstName, lastName, phoneNumber, password1, password2;
    @FXML
    private Label errorLabel;

    @FXML
    private void onCancel()
    {
        RegistrationController.cancel();
    }
    @FXML
    private void onNext()
    {
        RegistrationController.secondPage();
    }


    public static Parent create() throws IOException {
        return ControllerLoader.create(FirstPageController.class.getResource(FILE_NAME));
    }
}
