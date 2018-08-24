package client.window.main.menu.newgroup;

import client.application.ApplicationBank;
import client.network.queries.CreateDialogQuery;
import client.window.main.MainWindowManager;
import client.window.main.menu.AbstractWindow;
import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class UserSelectingListController extends AbstractWindow {
    @FXML
    private TextField title, searchField;
    @FXML
    private VBox userListBox;


    private final List<String> selected = new LinkedList<>();
    private final List<UserSelectorController> selectors = new LinkedList<>();
    private BiConsumer<String, List<String>> onClick = CreateDialogQuery::sendGroupQuery;



    @FXML
    private void onClick()
    {
        try {
            onClick.consume(title.getText(), selected);
            MainWindowManager.getInstance().closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static UserSelectingListController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(UserSelectorController.class.getResource("UserSelectingList.fxml"));
        loader.load();
        UserSelectingListController c = loader.getController();
        c.init();
        return c;
    }
    private void init()
    {
        List<User> list = ApplicationBank.getInstance().getFriendList();
        list.forEach(u->
        {
            try {
                UserSelectorController controller = UserSelectorController.create(u);
                controller.setOnClick(selected::add, selected::remove);
                selectors.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        selectors.sort(Comparator.comparing(UserSelectorController::getName));
        selectors.forEach(c->userListBox.getChildren().add(c.getRoot()));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            userListBox.getChildren().clear();
            if (newValue.length() == 0)
                selectors.forEach(c->userListBox.getChildren().add(c.getRoot()));
            else
            {
                selectors.stream().filter(u-> u.getName().contains(newValue) || u.getLogin().contains(newValue))
                        .forEach(u->userListBox.getChildren().add(u.getRoot()));
            }
        });
    }

    public void setOnClick(BiConsumer<String, List<String>> onClick) {
        this.onClick = onClick;
    }

    @FunctionalInterface
    public interface BiConsumer<E, T>
    {
        void consume(E e, T t) throws Exception;
    }
}
