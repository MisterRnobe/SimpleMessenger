package client.window.main.menu.newgroup;

import client.application.ApplicationBank;
import client.network.queries.CreateDialogQuery;
import client.window.main.MainWindowController;
import client.window.main.MainWindowManager;
import client.window.main.menu.AbstractMenuWindow;
import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UserSelectingListController extends AbstractMenuWindow {
    @FXML
    private TextField title, searchField;
    @FXML
    private VBox userListBox;
    @FXML
    private VBox root;

    private List<String> selected = new LinkedList<>();

    @FXML
    private void onClick()
    {
        try {
            CreateDialogQuery.sendGroupQuery(title.getText(), selected);
            MainWindowManager.getInstance().closeWindow();
        } catch (IOException e) {
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
                controller.setOnClick(s1->selected.add(s1), s2->selected.remove(s2));
                userListBox.getChildren().add(controller.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Pane getRoot() {
        return root;
    }
}
