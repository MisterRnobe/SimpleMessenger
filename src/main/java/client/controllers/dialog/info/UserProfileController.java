package client.controllers.dialog.info;

import client.app.main.AbstractWindow;
import common.objects.UserProfile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserProfileController extends AbstractWindow {
    @FXML
    private Circle avatar;
    @FXML
    private Text login, name, email;
    @FXML
    private TextArea info;

    public void setUser(UserProfile profile)
    {
        Platform.runLater(()-> {
            login.setText(profile.getLogin());
            name.setText(profile.getName());
            email.setText(profile.getEmail());
            info.setText(profile.getInfo());
        });
    }
    public static UserProfileController create() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserProfileController.class.getResource("UserProfile.fxml"));
        loader.load();
        return loader.getController();
    }
}
