package client.app.initial;

import client.app.ApplicationManager;
import client.app.registration.RegistrationController;
import client.network.queries.AuthorizationQuery;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.*;


public class InitialWindowController {
    @FXML
    private TextField login, password;
    @FXML
    private CheckBox remember;
    @FXML
    private Label errorLabel;


    @FXML
    private void initialize()
    {
        File file = new File("login.txt");
        if (file.exists())
        {
            try(BufferedReader r = new BufferedReader(new FileReader(file)))
            {
                String login = r.readLine();
                String pass = r.readLine();
                this.login.setText(login);
                this.password.setText(pass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onSignIn()
    {
        errorLabel.setText("");
        try {
            AuthorizationQuery
                    .sendLoginAndPassword(login.getText(), password.getText(),
                            ApplicationManager::launchMainWindow, this::onFail);
            if (remember.isSelected())
            {
                try(BufferedWriter w = new BufferedWriter(new FileWriter("login.txt")))
                {
                    w.write(login.getText()+"\n"+password.getText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onFail(Integer code)
    {
        Platform.runLater(()->errorLabel.setText("Ошибка: "+code));
    }

    @FXML
    private void onSignUp()
    {
        RegistrationController.startRegistration();
    }
    public static Pane create() throws IOException {
        FXMLLoader loader = new FXMLLoader(InitialWindowController.class.getResource("SignInWindow.fxml"));
        return loader.load();
    }

}
