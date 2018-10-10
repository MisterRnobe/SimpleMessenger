package client.app.main.dialog;

import client.network.queries.ReadMessageQuery;
import client.utils.ApplicationBank;
import client.utils.DialogBean;
import client.network.queries.SendMessageQuery;
import client.controllers.dialog.DialogController;
import client.controllers.dialog.MessageController;
import common.objects.DialogInfo;
import common.objects.Message;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;

import java.io.IOException;

public abstract class AbstractDialogWrapper {

    protected DialogController dialogController;
    protected final int dialogId;
    public AbstractDialogWrapper(int dialogId) throws IOException {
        dialogController = DialogController.create();
        dialogController.setOnSend(this::onSend);
        this.dialogId = dialogId;
        DialogBean bean = ApplicationBank.getInstance().getDialogById(dialogId);
        if (bean != null && bean.unreadCountProperty().get() != 0)
            ReadMessageQuery.sendQuery(dialogId);
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
    void bindMessages(DialogBean dialogBean)
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

    public static AbstractDialogWrapper createOf(int dialogId) throws IOException {
        int type = ApplicationBank.getInstance().getDialogById(dialogId).type;
        if (type == DialogInfo.DIALOG)
            return new DialogWrapper(dialogId);
        else if (type == DialogInfo.GROUP)
            return new GroupWrapper(dialogId);
        else if (type == DialogInfo.CHANNEL)
            return new ChannelWrapper(dialogId);
        throw new RuntimeException("Undefined dialog type!");
    }

    public int getDialogId() {
        return dialogId;
    }
}
