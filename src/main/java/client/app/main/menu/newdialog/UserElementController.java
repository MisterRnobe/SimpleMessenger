package client.app.main.menu.newdialog;

import client.utils.ApplicationBank;
import client.utils.DialogBean;
import client.app.main.MainWindowManager;
import common.objects.DialogInfo;
import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserElementController {
    @FXML
    private Label login, name;
    @FXML
    private AnchorPane root;
    private User bindUser;

    public AnchorPane getRoot() {
        return root;
    }
    @FXML
    private void onClick(MouseEvent e)
    {
        List<DialogBean> dialogs = ApplicationBank.getInstance().getDialogs();
        Optional<DialogBean> dialogBean = dialogs.stream()
                .filter(d-> d.type == DialogInfo.DIALOG)
                .filter(d->
                    d.getPartners().stream().anyMatch(s->s.equalsIgnoreCase(bindUser.getLogin())))
                .findFirst();
        if (dialogBean.isPresent())
        {
            MainWindowManager.getInstance().setDialog(dialogBean.get().dialogId);
        }
        else
            MainWindowManager.getInstance().createEmptyDialog(bindUser);
        e.consume();
    }

    public static UserElementController create(User u) throws IOException {
        FXMLLoader loader = new FXMLLoader(UserElementController.class.getResource("UserElement.fxml"));
        loader.load();
        UserElementController controller = loader.getController();
        controller.login.setText(u.getLogin());
        controller.name.setText(u.getName());
        controller.bindUser = u;
        return controller;
    }
}
