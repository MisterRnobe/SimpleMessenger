package client.window.main;

import client.application.DialogBean;
import common.objects.Message;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private Label title;
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
    public static DialogChooserController create(DialogBean dialogInfo) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(DialogChooserController.class.getResource("DialogChooser.fxml"));
        loader.load();
        DialogChooserController controller = loader.getController();
        controller.bindDialog(dialogInfo);
        return controller;
    }
    private void bindDialog(DialogBean dialogBean)
    {
        title.textProperty().bind(dialogBean.titleProperty());
        dialogBean.lastMessageProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(()->lastMessage.setText(newValue.getText())));
        lastMessage.setText(dialogBean.lastMessageProperty().getValue().getText());
        dialogId = dialogBean.dialogId;

    }

    public int getDialogId() {
        return dialogId;
    }
}
