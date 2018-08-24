package client.window.main.dialog.info;

import client.application.ApplicationBank;
import client.application.DialogBean;
import client.window.main.menu.AbstractWindow;
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

    public static GroupInfoController create(int dialogId) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(GroupInfoController.class.getResource("GroupInfo.fxml"));
        loader.load();
        GroupInfoController c = loader.getController();
        c.init(dialogId);
        return c;
    }
    private void init(int dialogId)
    {
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        title.setText(dialogBean.titleProperty().getValue());
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
