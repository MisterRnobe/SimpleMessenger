package client.controllers;

import client.utils.AvatarSupplier;
import common.objects.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.function.Consumer;

public class UserElementController {
    @FXML
    private Label login, name;
    @FXML
    private AnchorPane root;
    @FXML
    private Circle avatar;

    private boolean selected = false;

    private User bindUser;

    public AnchorPane getRoot() {
        return root;
    }
    void setOnClick(Consumer<User> click)
    {
        root.setOnMouseClicked(event -> {
           click.accept(bindUser);
           event.consume();
        });
    }
    private void setPhoto(Image image)
    {
        Platform.runLater(()-> {
            if (image != null)
                avatar.setFill(new ImagePattern(image));
        });
    }
    private void setUser(User u)
    {
        login.setText(u.getLogin());
        name.setText(u.getName());
        bindUser = u;
        if (u.getAvatarPath() != null)
            AvatarSupplier.getInstance().getAvatar(u.getAvatarPath(), this::setPhoto);
        else
            this.setPhoto(AvatarSupplier.paintDefaultAvatar(u.getName()));
    }
    void setSelectable(Consumer<? super String> onSelect, Consumer<? super String> onDeselect)
    {
        root.setOnMouseClicked(e->
        {
            if (selected)
            {
                onDeselect.accept(bindUser.getLogin());
                selected = !selected;
                root.setStyle("-fx-background-color: white");
            }
            else
            {
                onSelect.accept(bindUser.getLogin());
                selected = !selected;
                root.setStyle("-fx-background-color: lightblue");

            }
        });
    }
    void setUnselectable()
    {
        root.setOnMouseClicked(e->{});
    }
    User getBindUser()
    {
        return bindUser;
    }
    public static UserElementController create(User u) throws IOException {
        FXMLLoader loader = new FXMLLoader(UserElementController.class.getResource("UserElement.fxml"));
        loader.load();
        UserElementController controller = loader.getController();
        controller.setUser(u);
        return controller;
    }
}
