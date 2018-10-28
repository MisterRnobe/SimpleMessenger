package client.controllers.menu;

import client.app.main.AbstractWindow;
import client.app.main.MainWindowManager;
import client.controllers.UserListController;
import client.suppliers.UserSupplier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.List;

public class CreateGroupChannelController extends AbstractWindow
{
    @FXML
    private TextField title;
    @FXML
    private AnchorPane empty;
    @FXML
    private Circle avatarSelector;

    private UserListController listController;

    private BiConsumer<String, List<String>> onClick = (s, list) -> {};

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
    public static CreateGroupChannelController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(CreateGroupChannelController.class.getResource("CreateGroup.fxml"));
        loader.load();
        CreateGroupChannelController c = loader.getController();
        c.init();
        return c;
    }
    private void init() throws IOException {
        listController = UserListController.create();
        listController.setUserList(UserSupplier.getInstance().getFriendList());
        listController.setSize(300, 300).setTextListener(listController::filter).setSelectable(true);
        empty.getChildren().add(listController.getRoot());
    }

    public void setOnClick(BiConsumer<String, List<String>> onClick)
    {
        this.onClick = onClick;
    }

    @FunctionalInterface
    public interface BiConsumer<E, T>
    {
        void consume(E e, T t) throws Exception;
    }
}
