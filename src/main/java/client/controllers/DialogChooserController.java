package client.controllers;

import client.app.main.MainWindowManager;
import client.network.queries.ReadMessageQuery;
import client.suppliers.AbstractDialogBean;
import client.utils.AvatarSupplier;
import common.objects.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.function.BiConsumer;

public class DialogChooserController {
    @FXML
    private Label title;
    @FXML
    private Label lastMessage, unreadCount;
    @FXML
    private Circle avatar;
    @FXML
    private AnchorPane pane;

    private String stdColor;
    private AbstractDialogBean bindDialog;
    private BiConsumer<DialogChooserController, Message> onNewMessage = (d,m)->{};


    @FXML
    private void initialize()
    {
        stdColor = "ffffff";
        pane.setBackground(new Background(new BackgroundFill(Color.web("#"+stdColor),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @FXML
    private void onMouseEnter()
    {
        String color = "8EF98A";
        pane.setBackground(new Background(new BackgroundFill(Color.web("#"+color),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }
    @FXML
    private void onMouseExit()
    {
        pane.setBackground(new Background(new BackgroundFill(Color.web("#"+stdColor),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }
    @FXML
    private void onMouseClicked()
    {
        MainWindowManager.getInstance().setDialog(bindDialog.dialogId);
    }
    public Parent getPane()
    {
        return pane;
    }
    public static DialogChooserController create(AbstractDialogBean dialogInfo) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(DialogChooserController.class.getResource("DialogChooser.fxml"));
        loader.load();
        DialogChooserController controller = loader.getController();
        controller.bindDialog(dialogInfo);
        return controller;
    }
    private void bindDialog(AbstractDialogBean dialogBean)
    {
        title.textProperty().bind(dialogBean.title());
        dialogBean.lastMessageProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(()->{
                    lastMessage.setText(newValue.getText());
                    onNewMessage.accept(this, newValue);
                    if (newValue.getDialogId() != MainWindowManager.getInstance().getOpenedDialogId())
                    {
                        bindDialog.addUnreadCount(1);
                    }
                    else
                    {
                        try {
                            ReadMessageQuery.sendQuery(newValue.getDialogId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
        lastMessage.setText(dialogBean.lastMessageProperty().getValue().getText());
        dialogBean.unreadCountProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(()->setUnreadCount(newValue.intValue())));
        setUnreadCount(dialogBean.unreadCountProperty().get());
        bindDialog = dialogBean;

        if (dialogBean.avatarPath().getValue() != null)
            AvatarSupplier.getInstance().getAvatar(dialogBean.avatarPath().getValue(), this::setAvatar);
        else
            setAvatar(AvatarSupplier.paintDefaultAvatar(dialogBean.title().getValue()));
    }
    public void setOnMessageReceived(BiConsumer<DialogChooserController, Message> consumer)
    {
        this.onNewMessage = consumer;
    }
    private void setUnreadCount(int count)
    {
        if (count == 0)
        {
            unreadCount.setText("");
            lastMessage.setStyle("-fx-background-color: transparent");
        }
        else
        {
            unreadCount.setText(Integer.toString(count));
            lastMessage.setStyle("-fx-background-color: lightblue");
        }
    }
    public int getDialogId() {
        return bindDialog.dialogId;
    }
    private void setAvatar(Image image)
    {
        this.avatar.setFill(new ImagePattern(image));
    }
}
