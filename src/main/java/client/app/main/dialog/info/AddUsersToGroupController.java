package client.app.main.dialog.info;

import client.app.main.MainWindowManager;
import client.app.main.menu.AbstractWindow;
import client.app.main.window.UserSelectingListController;
import client.network.queries.GroupModificationQuery;
import client.utils.ApplicationBank;
import client.utils.DialogBean;
import common.objects.GroupModification;
import common.objects.User;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AddUsersToGroupController extends AbstractWindow {

    private AddUsersToGroupController(UserSelectingListController listController, int dialogId){
        root = new AnchorPane();
        root.setPrefSize(400, 400);
        listController.getButton().setText("Добавить");
        DialogBean b = ApplicationBank.getInstance().getDialogById(dialogId);

        List<User> partners = b.getPartners().stream().map(s->ApplicationBank.getInstance().getUserByLogin(s)).collect(Collectors.toList());
        List<User> users = ApplicationBank.getInstance().getFriendList();
        users.removeAll(partners);
        listController.loadUsers(users);
        listController.setOnClick(list -> {
            try {
                GroupModificationQuery.addUsers(dialogId, list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainWindowManager.getInstance().closeWindow();
        });


        root.getChildren().add(listController.getRoot());
    }
    public static AddUsersToGroupController create(int dialogId) throws IOException
    {
        UserSelectingListController listController = UserSelectingListController.create();
        return new AddUsersToGroupController(listController, dialogId);
    }
}
