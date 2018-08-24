package client.window.main.dialog;

import client.Supplier;
import client.window.main.MainWindowController;
import client.window.main.MainWindowManager;
import client.window.main.menu.AbstractWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DialogController {
    @FXML
    protected VBox messageBox;
    @FXML
    protected Label title;
    @FXML
    protected Label info;
    @FXML
    protected BorderPane messageWindow;
    @FXML
    protected ScrollPane messagePane;
    @FXML
    protected TextField messageField;
    protected int dialogId;

    private Runnable onSend = ()->{};
    private Supplier<AbstractWindow> infoWindow;

    public Parent getRoot()
    {
        return messageWindow;
    }

    public static DialogController create() throws IOException {
        FXMLLoader loader = new FXMLLoader(DialogController.class.getResource("Dialog.fxml"));
        loader.load();
        return loader.getController();
    }


    @FXML
    private void sendMessage()
    {
        onSend.run();
        messageField.setText("");
    }
    @FXML
    private void showInfo()
    {
        try {
            AbstractWindow w = infoWindow.get();
            MainWindowManager.getInstance().displayWindow(w);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public void setInfoWindow(Supplier<AbstractWindow> infoWindow) {
        this.infoWindow = infoWindow;
    }

    public void setOnSend(Runnable onSend) {
        this.onSend = onSend;
    }

    public VBox getMessageBox() {
        return messageBox;
    }

    public Label getTitle() {
        return title;
    }

    public Label getInfo() {
        return info;
    }

    public BorderPane getMessageWindow() {
        return messageWindow;
    }

    public ScrollPane getMessagePane() {
        return messagePane;
    }

    public TextField getMessageField() {
        return messageField;
    }

    public int getDialogId() {
        return dialogId;
    }
}
