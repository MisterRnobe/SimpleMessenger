package client.app.initial;

import client.utils.ControllerLoader;
import client.network.queries.AuthorizationQuery;
import client.app.registration.RegistrationController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;


public class InitialWindowController {
    private static final String FILE_NAME = "SignInWindow.fxml";
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
        try {
            AuthorizationQuery.sendLoginAndPassword(login.getText(), password.getText());
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

    @FXML
    private void onSignUp()
    {
        RegistrationController.startRegistration();
    }
    public static Parent create() throws IOException {
        return ControllerLoader.create(InitialWindowController.class.getResource(FILE_NAME));
    }

}
