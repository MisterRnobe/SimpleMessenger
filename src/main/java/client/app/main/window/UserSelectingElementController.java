package client.app.main.window;

import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.function.Consumer;

public class UserSelectingElementController {
    @FXML
    private AnchorPane root;
    @FXML
    private Text login, name;
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
    public String getName()
    {
        return name.getText();
    }
    public String getLogin()
    {
        return login.getText();
    }

    public static UserSelectingElementController create(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(UserSelectingElementController.class.getResource("UserSelectingElement.fxml"));
        loader.load();
        UserSelectingElementController c = loader.getController();
        c.login.setText(user.getLogin());
        c.name.setText(user.getName());
        return c;
    }
}
