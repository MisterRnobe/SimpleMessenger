package client.controllers;

import client.app.main.AbstractWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class ErrorController extends AbstractWindow {

    @FXML
    private Label message;
    @FXML
    private Label title;


    private void setText(String text) {
        message.setText(text);
    }

    @FXML
    public void reconnect() {
        setText("JOPAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    public static ErrorController create(String s) throws IOException {
        FXMLLoader loader = new FXMLLoader(ErrorController.class.getResource("Error.fxml"));
        loader.load();
        return loader.getController();
    }

}
