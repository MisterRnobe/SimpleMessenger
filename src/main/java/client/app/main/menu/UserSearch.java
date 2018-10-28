package client.app.main.menu;

import client.app.main.AbstractWindow;
import client.app.main.MainWindowManager;
import client.controllers.UserListController;
import client.suppliers.DialogBean;
import client.suppliers.DialogManager;
import common.objects.User;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserSearch extends AbstractWindow {
    private static final double WIDTH = 300, HEIGHT = 400;
    public UserSearch() throws IOException {
        //this.parent = parent;
        UserListController wrapped = UserListController.create();
        wrapped.setTextListener(wrapped::search).setOnClickElement(this::onClick);
        root = new AnchorPane(wrapped.getRoot());
        root.setPrefSize(WIDTH, HEIGHT);
    }
    private void onClick(User user)
    {
        List<DialogBean> dialogs = DialogManager.getInstance().getDialogsOnly();
        Optional<DialogBean> dialogBean = dialogs.stream()
                .filter(d->
                        d.partner.getLogin().equalsIgnoreCase(user.getLogin()))
                .findFirst();
        this.close();
        if (dialogBean.isPresent())
            MainWindowManager.getInstance().setDialog(dialogBean.get().dialogId);
        else
            MainWindowManager.getInstance().createEmptyDialog(user);

    }
}
