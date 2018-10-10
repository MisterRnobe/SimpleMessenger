package client.controllers.dialog.info;

import client.app.main.MainWindowManager;
import client.controllers.UserListController;
import client.utils.ApplicationBank;
import client.utils.DialogBean;
import client.app.main.AbstractWindow;
import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GroupInfoController extends AbstractWindow {
    @FXML
    private Text title;
    @FXML
    private AnchorPane userList;
    private int dialogId;


    public static GroupInfoController create(int dialogId) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(GroupInfoController.class.getResource("GroupInfo.fxml"));
        loader.load();
        GroupInfoController c = loader.getController();
        c.init(dialogId);
        return c;
    }
    @FXML
    private void onClick()
    {
        try {
            MainWindowManager.getInstance().replaceWindow(AddUsersToGroupController.create(dialogId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void init(int dialogId) throws IOException
    {
        this.dialogId = dialogId;
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        title.textProperty().bind(dialogBean.titleProperty());
        List<User> partners = dialogBean.getPartners().stream().map(s->ApplicationBank.getInstance().getUserByLogin(s)).collect(Collectors.toList());
        UserListController controller = UserListController.create();
        controller.setTextListener(controller::search).setSelectable(true).setUserList(partners);
        userList.getChildren().add(controller.getRoot());
    }
}
