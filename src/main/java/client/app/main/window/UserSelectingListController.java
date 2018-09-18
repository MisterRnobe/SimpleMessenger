package client.app.main.window;

import client.app.main.menu.newdialog.UserElementController;
import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class UserSelectingListController{
    @FXML
    private AnchorPane root;
    @FXML
    private TextField searchField;
    @FXML
    private VBox vBox;
    @FXML
    private Button button;

    private List<UserSelectingElementController> elementControllerList = new LinkedList<>();
    private List<String> selected = new LinkedList<>();
    private Consumer<List<String>> listener;

    public static UserSelectingListController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(UserSelectingListController.class.getResource("UserSelectingList.fxml"));
        loader.load();
        return loader.getController();
    }
    public void loadUsers(Collection<User> users)
    {
        users.forEach(u->
        {
            try {
                UserSelectingElementController controller = UserSelectingElementController.create(u);
                controller.setOnClick(selected::add, selected::remove);
                elementControllerList.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        elementControllerList.sort(Comparator.comparing(UserSelectingElementController::getLogin));
        elementControllerList.forEach(u->vBox.getChildren().add(u.getRoot()));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            vBox.getChildren().clear();
            Predicate<UserSelectingElementController> predicate = newValue.length() == 0? c->true: u-> u.getName().contains(newValue) || u.getLogin().contains(newValue);
            elementControllerList.stream().filter(predicate)
                    .forEach(u->vBox.getChildren().add(u.getRoot()));
        });
        button.setOnAction(e->listener.accept(selected));
    }
    public void setOnClick(Consumer<List<String>> listener)
    {
        this.listener = listener;
    }

    public AnchorPane getRoot() {
        return root;
    }

    public Button getButton() {
        return button;
    }
    public List<String> getSelected()
    {
        return new ArrayList<>(selected);
    }
}
