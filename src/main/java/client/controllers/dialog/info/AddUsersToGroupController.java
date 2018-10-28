package client.controllers.dialog.info;

import client.app.main.AbstractWindow;
import client.app.main.MainWindowManager;
import client.controllers.UserListController;
import client.network.queries.GroupModificationQuery;
import client.suppliers.DialogManager;
import client.suppliers.GroupBean;
import client.suppliers.UserSupplier;
import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class AddUsersToGroupController extends AbstractWindow {
    @FXML
    private AnchorPane wrapper;
    private int dialogId;
    private Supplier<List<String>> listSupplier = ()-> null;


    private void init(int dialogId) throws IOException
    {
        this.dialogId = dialogId;
        UserListController listController = UserListController.create().setSelectable(true);
        listController.setTextListener(listController::search).setUserList(getFriendsList(dialogId));
        wrapper.getChildren().add(listController.getRoot());
        listSupplier = listController::getSelected;
    }
    @FXML
    private void onOk()
    {
        try{
            GroupModificationQuery.addUsers(dialogId, listSupplier.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainWindowManager.getInstance().closeWindow();
    }
    @FXML
    private void onCancel()
    {
        MainWindowManager.getInstance().closeWindow();
    }
    public static AddUsersToGroupController create(int dialogId) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(AddUsersToGroupController.class.getResource("AddUsersToGroup.fxml"));
        loader.load();
        AddUsersToGroupController controller = loader.getController();
        controller.init(dialogId);
        return controller;
    }
    private List<User> getFriendsList(int dialogId)
    {
        //DialogBean bindDialog = ApplicationBank.getInstance().getDialogById(dialogId);
        //DialogBean bindDialog = DialogManager.getInstance().getDialogById(dialogId);
        //List<User> partners = bindDialog.getPartners().stream().map(s->ApplicationBank.getInstance().getUserByLogin(s)).collect(Collectors.toList());
        List<User> partners = ((GroupBean)DialogManager.getInstance().getDialogById(dialogId)).getMembers();
        List<User> friends = UserSupplier.getInstance().getFriendList();
        friends.removeAll(partners);
        return friends;
    }
}
