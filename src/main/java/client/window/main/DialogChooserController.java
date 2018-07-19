package client.window.main;

import common.objects.DialogInfo;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
        //MainWindow.getInstance().showDialog(this);
    }
    public Parent getPane()
    {
        return pane;
    }
//    private void setLinkedDialog(DialogController d)
//    {
//        this.linkedDialog = d;
//        this.setTitle(d.getDialog().getTitle());
//        this.setLastMessage(d.getDialog().getMessage().message);
//        this.setAvatar();
//    }
//    private void setAvatar()
//    {
//        Image image = linkedDialog.getDialog().getImage().getImage();
//        double radius = avatar.getRadius();
//        avatar.setCenterX(radius);
//        avatar.setCenterY(radius);
//        avatar.setFill(new ImagePattern(image, 0,0, 2*radius, 2*radius,false));
//    }
    public static DialogChooserController create(DialogInfo dialogInfo) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(DialogChooserController.class.getResource("DialogChooser.fxml"));
        loader.load();
        DialogChooserController controller = loader.getController();

        controller.setLastMessage(dialogInfo.getLastMessage().getText());
        controller.setTitle(dialogInfo.getDialogName());

        return controller;
    }
//    public void update()
//    {
//        this.linkedDialog.updateMessages();
//        this.setLastMessage(linkedDialog.getDialog().getMessage().message);
//    }
//    public Parent getRoot(){return pane;}
//    public DialogController getLinkedDialog() {return linkedDialog;}

}
