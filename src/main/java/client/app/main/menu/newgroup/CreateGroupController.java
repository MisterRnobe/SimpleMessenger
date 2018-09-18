package client.app.main.menu.newgroup;

import client.app.main.window.UserSelectingListController;
import client.utils.ApplicationBank;
import client.network.queries.CreateDialogQuery;
import client.app.main.MainWindowManager;
import client.app.main.menu.AbstractWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class CreateGroupController extends AbstractWindow {
    @FXML
    private TextField title;
    @FXML
    private AnchorPane empty;


    private UserSelectingListController listController;
    private BiConsumer<String, List<String>> onClick = CreateDialogQuery::sendGroupQuery;



    @FXML
    private void onClick()
    {
        try {
            onClick.consume(title.getText(), listController.getSelected());
            MainWindowManager.getInstance().closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static CreateGroupController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(CreateGroupController.class.getResource("CreateGroup.fxml"));
        loader.load();
        CreateGroupController c = loader.getController();
        c.init();
        return c;
    }
    private void init() throws IOException {
        listController = UserSelectingListController.create();
        listController.loadUsers(ApplicationBank.getInstance().getFriendList());
        listController.getButton().setText("Создать");
        listController.getButton().setOnAction(e->onClick());
        empty.getChildren().add(listController.getRoot());
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
