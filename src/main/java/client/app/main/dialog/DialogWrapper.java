package client.app.main.dialog;

import client.controllers.dialog.info.UserProfileController;
import client.network.queries.GetUserProfileQuery;
import client.network.queries.GetUserStatusQuery;
import client.suppliers.AbstractDialogBean;
import client.suppliers.DialogBean;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Date;

public class DialogWrapper extends AbstractDialogWrapper<DialogBean> {
    private String partner;
    private UserProfileController c = UserProfileController.create();

    DialogWrapper(AbstractDialogBean dialogBean) throws IOException {
        super((DialogBean) dialogBean);
        bindDialog();
        dialogController.setInfoWindow(()->{
            GetUserProfileQuery.getProfile(partner, c::setUser);
            return c;
        });
    }

    private void bindDialog()
    {
        dialogController.getTitle().textProperty().bind(dialogBean.title());
        bindMessages(dialogBean);

        partner = dialogBean.partner.getLogin();

        try {
            GetUserStatusQuery.hookToUser(partner, user -> Platform.runLater(()-> {
                if (user.isOnline()) {
                    dialogController.getInfo().setText("В сети");
                } else {
                    Date d = new Date(user.getLastOnline());
                    dialogController.getInfo().setText(String.format("Был в сети %tc", d));
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
