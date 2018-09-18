package client.app.main.dialog.info;

import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserInfoElementController {
    @FXML
    private Text login, name;
    @FXML
    private AnchorPane root;

    public static UserInfoElementController create(User u) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(UserInfoElementController.class.getResource("UserInfoElement.fxml"));
        loader.load();
        UserInfoElementController controller = loader.getController();
        controller.init(u);
        return controller;
    }
    private void init(User user)
    {
        login.setText(user.getLogin());
        name.setText(user.getName());
    }
    public AnchorPane getRoot()
    {
        return root;
    }
}
