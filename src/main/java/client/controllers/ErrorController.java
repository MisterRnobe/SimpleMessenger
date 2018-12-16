package client.controllers;

import client.app.main.AbstractWindow;
import client.network.queries.FindUsersQuery;
import client.utils.AvatarSupplier;
import common.objects.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ErrorController extends AbstractWindow {

    @FXML
    private Label message;


    private void setText(String text)
    {
        message.setText(text);
    }
    @FXML
    public void reconnect(){
        setText("JOPAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
   }
    public static ErrorController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(ErrorController.class.getResource("Error.fxml"));
        loader.load();
        return loader.getController();
    }
}
