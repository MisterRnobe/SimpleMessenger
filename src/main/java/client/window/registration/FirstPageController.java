package client.window.registration;

import client.application.ControllerLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FirstPageController {
    private static final String FILE_NAME = "FirstPageForm.fxml";
    @FXML
    private TextField login, firstName, lastName, email, password1, password2;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane root;

    @FXML
    private void onCancel()
    {
        RegistrationController.cancel();
    }
    @FXML
    private void onNext()
    {
        if (password1.getText().equals(password2.getText())) {
            RegistrationController.finish(login.getText(), password1.getText(),
                    firstName.getText()+" "+lastName.getText(), email.getText(), null);
        }
        else
        {
            displayError("Пароли не совпадают");
        }
    }
    public void displayError(String text)
    {
        errorLabel.setText(text);
    }


    public static FirstPageController create() throws IOException {
        FXMLLoader loader = new FXMLLoader(FirstPageController.class.getResource(FILE_NAME));
        loader.load();
        return loader.getController();
    }

    public AnchorPane getRoot() {
        return root;
    }
}
