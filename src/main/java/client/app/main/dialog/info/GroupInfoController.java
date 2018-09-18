package client.app.main.dialog.info;

import client.app.main.MainWindowManager;
import client.utils.ApplicationBank;
import client.utils.DialogBean;
import client.app.main.menu.AbstractWindow;
import common.objects.requests.AddUsersToGroupRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class GroupInfoController extends AbstractWindow {
    @FXML
    private Text title;
    @FXML
    private VBox userList;
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
    private void init(int dialogId)
    {
        this.dialogId = dialogId;
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        title.textProperty().bind(dialogBean.titleProperty());
        dialogBean.getPartners().stream().map(s->ApplicationBank.getInstance().getUserByLogin(s))
                .forEach(u->{
                    try {
                        userList.getChildren().add(UserInfoElementController.create(u).getRoot());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }
}
