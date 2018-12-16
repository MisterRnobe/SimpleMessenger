package client.controllers.dialog.info;

import client.app.main.AbstractWindow;
import client.app.main.MainWindowManager;
import client.controllers.UserListController;
import client.network.queries.GetUsersInDialogQuery;
import client.suppliers.AbstractDialogBean;
import client.suppliers.DialogManager;
import client.suppliers.GroupBean;
import common.objects.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class GroupInfoController extends AbstractWindow {
    @FXML
    private Text title;
    @FXML
    private AnchorPane userList;
    private int dialogId;


    public static GroupInfoController create(int dialogId) throws IOException {
        FXMLLoader loader = new FXMLLoader(GroupInfoController.class.getResource("GroupInfo.fxml"));
        loader.load();
        GroupInfoController c = loader.getController();
        c.init(dialogId);
        return c;
    }

    @FXML
    private void onClick() {
        MainWindowManager.getInstance().replaceWindow(() -> AddUsersToGroupController.create(dialogId));
    }

    private void init(int dialogId) throws IOException {
        this.dialogId = dialogId;
        AbstractDialogBean dialogBean = DialogManager.getInstance().getDialogById(dialogId);
        title.textProperty().bind(dialogBean.title());
        GetUsersInDialogQuery.send(dialogId, partners -> Platform.runLater(() -> {
            try {
                UserListController controller = UserListController.create();
                controller.setTextListener(controller::search).setSelectable(true).setUserList(partners.getUsers());
                userList.getChildren().add(controller.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
