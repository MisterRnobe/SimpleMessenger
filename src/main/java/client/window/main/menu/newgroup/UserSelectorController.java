package client.window.main.menu.newgroup;

import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

public class UserSelectorController {
    @FXML
    private Label login, name;
    @FXML
    private AnchorPane root;
    private boolean selected = false;
    
    void setOnClick(Consumer<String> select, Consumer<String> deselect)
    {
        root.setOnMouseClicked(e->
        {

            if (selected)
            {
                deselect.accept(login.getText());
                selected = !selected;
                root.setStyle("-fx-background-color: white");
            }
            else 
            {
                select.accept(login.getText());
                selected = !selected;
                root.setStyle("-fx-background-color: lightblue");

            }
        });
    }
    public Pane getRoot()
    {
        return root;
    }

    public static UserSelectorController create(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(UserSelectorController.class.getResource("UserSelector.fxml"));
        loader.load();
        UserSelectorController c = loader.getController();
        c.login.setText(user.getLogin());
        c.name.setText(user.getName());
        return c;
    }
}
