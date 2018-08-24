package client.window.main.menu.newdialog;

import client.network.queries.FindUsersQuery;
import client.window.main.menu.AbstractWindow;
import common.objects.UserList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class UserSearchController extends AbstractWindow {
    @FXML
    private TextField textField;
    @FXML
    private VBox userBox;


    @FXML
    private void initialize()
    {
        textField.textProperty().addListener((observable, oldValue, newValue) ->
                send(newValue));
    }
    private void send(String text)
    {
        if (text.length() != 0)
        {
            try {
                FindUsersQuery.sendQuery(text, this::onReceiveUserList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onReceiveUserList(UserList userList)
    {
        Platform.runLater(()->userBox.getChildren().clear());
        userList.getUsers().forEach(u->{
            try {
                UserElementController c = UserElementController.create(u);
                Platform.runLater(()->userBox.getChildren().add(c.getRoot()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static UserSearchController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(UserSearchController.class.getResource("UserSearch.fxml"));
        loader.load();
        return loader.getController();
    }
}
