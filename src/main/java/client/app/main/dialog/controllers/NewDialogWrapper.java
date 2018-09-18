package client.app.main.dialog.controllers;

import client.network.queries.CreateDialogQuery;
import common.objects.User;

import java.io.IOException;
import java.util.Date;

public class NewDialogWrapper extends AbstractDialogWrapper {
    private User user;

    public NewDialogWrapper(User user) throws IOException {
        super(-1);
        this.user = user;
        bindUser();
    }

    @Override
    public void onSend() {
        try {
            CreateDialogQuery.sendDialogQuery(user.getLogin(), dialogController.getMessageField().getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void bindUser()
    {
        dialogController.getTitle().setText(user.getName());
        String text;
        if (user.isOnline())
            text = "В сети";
        else
        {
            Date d = new Date(user.getLastOnline());
            text = String.format("Был в сети %tc", d);
        }
        dialogController.getInfo().setText(text);
    }
}
