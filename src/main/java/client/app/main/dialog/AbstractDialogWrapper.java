package client.app.main.dialog;

import client.controllers.dialog.DialogController;
import client.controllers.dialog.MessageController;
import client.network.queries.ReadMessageQuery;
import client.network.queries.SendMessageQuery;
import client.suppliers.AbstractDialogBean;
import common.objects.Message;
import common.objects.requests.DialogTypes;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;

import java.io.IOException;

public abstract class AbstractDialogWrapper<D extends AbstractDialogBean> {

    protected DialogController dialogController;
    protected final D dialogBean;

    AbstractDialogWrapper(D dialogBean) throws IOException
    {
        dialogController = DialogController.create();
        dialogController.setOnSend(this::onSend);
        this.dialogBean = dialogBean;
        if (dialogBean != null && dialogBean.unreadCountProperty().get() != 0)
            ReadMessageQuery.sendQuery(dialogBean.dialogId);
    }

    public void onSend(){
        String text = dialogController.getMessageField().getText();
        if (text.length() != 0)
        {
            try {
                SendMessageQuery.sendQuery(dialogBean.dialogId, text);
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
    void bindMessages(D dialogBean)
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

    public static AbstractDialogWrapper createOf(AbstractDialogBean dialogBean) throws IOException {
        int type = dialogBean.type;
        if (type == DialogTypes.DIALOG)
            return new DialogWrapper(dialogBean);
        else if (type == DialogTypes.GROUP)
            return new GroupWrapper(dialogBean);
        else if (type == DialogTypes.CHANNEL)
            return new ChannelWrapper(dialogBean);
        throw new RuntimeException("Undefined dialog type!");
    }

    public int getDialogId() {
        return dialogBean.dialogId;
    }
}
