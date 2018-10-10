package client.app.main.menu;

import client.app.main.AbstractMainWindow;
import client.app.main.AbstractWindow;
import client.app.main.MainWindowManager;
import client.controllers.UserListController;
import client.utils.ApplicationBank;
import client.utils.DialogBean;
import common.objects.DialogInfo;
import common.objects.User;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserSearch extends AbstractWindow {
    public static final double WIDTH = 300, HEIGHT = 400;
    private AbstractMainWindow parent;
    public UserSearch(AbstractMainWindow parent) throws IOException {
        this.parent = parent;
        UserListController wrapped = UserListController.create();
        wrapped.setTextListener(wrapped::search).setOnClickElement(this::onClick);
        root = new AnchorPane(wrapped.getRoot());
        root.setPrefSize(WIDTH, HEIGHT);
    }
    private void onClick(User user)
    {
        List<DialogBean> dialogs = ApplicationBank.getInstance().getDialogs();
        Optional<DialogBean> dialogBean = dialogs.stream()
                .filter(d-> d.type == DialogInfo.DIALOG)
                .filter(d->
                        d.getPartners().stream().anyMatch(s->s.equalsIgnoreCase(user.getLogin())))
                .findFirst();
        parent.closeTopWindow();
        if (dialogBean.isPresent())
        {
            MainWindowManager.getInstance().setDialog(dialogBean.get().dialogId);
        }
        else
            MainWindowManager.getInstance().createEmptyDialog(user);

    }
}
