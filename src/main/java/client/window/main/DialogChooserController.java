package client.window.main;

import client.application.ApplicationBank;
import client.network.queries.GetDialogQuery;
import common.objects.Dialog;
import common.objects.DialogInfo;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class DialogChooserController {
    @FXML
    private Label login;
    @FXML
    private Label lastMessage;
    @FXML
    private Circle avatar;
    @FXML
    private AnchorPane pane;

    private String stdColor;
    private int dialogId;


    @FXML
    private void initialize()
    {
        stdColor = "ffffff";
        pane.setBackground(new Background(new BackgroundFill(Color.web("#"+stdColor),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setTitle(String title) {
        this.login.setText(title);
    }

    private void setLastMessage(String lastMessage) {
        this.lastMessage.setText(lastMessage);
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
        MainWindowManager.getInstance().setDialog(dialogId);
    }
    public Parent getPane()
    {
        return pane;
    }
    public static DialogChooserController create(DialogInfo dialogInfo) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(DialogChooserController.class.getResource("DialogChooser.fxml"));
        loader.load();
        DialogChooserController controller = loader.getController();

        controller.setLastMessage(dialogInfo.getLastMessage().getText());
        controller.setTitle(dialogInfo.getDialogName());
        controller.dialogId = dialogInfo.getDialogId();

        return controller;
    }
    public void update()
    {
        this.setLastMessage(ApplicationBank.getInstance().getDialogInfoById(dialogId).getLastMessage().getText());
    }

    public int getDialogId() {
        return dialogId;
    }
}
