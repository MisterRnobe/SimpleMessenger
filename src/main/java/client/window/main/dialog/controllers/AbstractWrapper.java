package client.window.main.dialog.controllers;

import client.application.ApplicationBank;
import client.application.DialogBean;
import client.network.queries.SendMessageQuery;
import client.window.main.dialog.DialogController;
import client.window.main.dialog.MessageController;
import common.objects.DialogInfo;
import common.objects.Message;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public abstract class AbstractWrapper {

    protected DialogController dialogController;
    protected final int dialogId;
    public AbstractWrapper(int dialogId) throws IOException {
        dialogController = DialogController.create();
        dialogController.setOnSend(this::onSend);
        this.dialogId = dialogId;
    }

    public void onSend(){
        String text = dialogController.getMessageField().getText();
        if (text.length() != 0)
        {
            try {
                SendMessageQuery.sendQuery(dialogId, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    protected void addMessage(Message message) {
        Platform.runLater(()->{
                    try {
                        dialogController.getMessageBox().getChildren()
                                .add(MessageController.create(message));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        });
    }
    public Pane getRoot()
    {
        return dialogController.getMessageWindow();
    }
    protected void bindMessages(DialogBean dialogBean)
    {
        dialogBean.messages().forEach(this::addMessage);
        dialogBean.messages().addListener((ListChangeListener<Message>) c -> {
            while (c.next())
            {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(this::addMessage);
                }
            }
        });
    }

    public static AbstractWrapper createOf(int dialogId) throws IOException {
        int type = ApplicationBank.getInstance().getDialogById(dialogId).type;
        if (type == DialogInfo.DIALOG)
            return new DialogWrapper(dialogId);
        else if (type == DialogInfo.GROUP)
            return new GroupWrapper(dialogId);
        else if (type == DialogInfo.CHANNEL)
            return new ChannelWrapper(dialogId);
        throw new RuntimeException("Wrong dialog type!");
    }
}
