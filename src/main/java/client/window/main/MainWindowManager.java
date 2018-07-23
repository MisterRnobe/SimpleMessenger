package client.window.main;

import client.application.ApplicationBank;
import client.network.queries.GetDialogQuery;
import client.network.queries.GetDialogsQuery;
import client.window.main.listeners.DialogInfoListener;
import client.window.main.listeners.DialogListener;
import client.window.main.listeners.MessageListener;
import common.objects.Dialog;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowManager {
    private static MainWindowManager instance;

    public static MainWindowManager getInstance() {
        return instance;
    }
    private MainWindowController mainWindowController;
    private MainWindowManager(){
        Platform.runLater(() ->
        {
            Stage stage = ApplicationBank.getInstance().getStage();
            stage.setTitle("Simple messenger");
            stage.setResizable(true);
            try {
                mainWindowController = MainWindowController.create();
                Scene scene = new Scene((AnchorPane)mainWindowController.getRoot());
                ApplicationBank.getInstance().getStage().setScene(scene);
                ApplicationBank.getInstance().getStage().show();
                ApplicationBank.getInstance().addDialogInfoListener(new DialogInfoListener(mainWindowController));
                ApplicationBank.getInstance().addDialogListener(new DialogListener(mainWindowController));
                ApplicationBank.getInstance().addMessageListener(new MessageListener(mainWindowController));
                GetDialogsQuery.sendQuery(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void start(){
        instance = new MainWindowManager();

    }
    public void setDialog(int dialogId)
    {
        Dialog d;
        if ((d = ApplicationBank.getInstance().getDialogById(dialogId)) == null) {
            try {
                GetDialogQuery.sendQuery(dialogId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            mainWindowController.setDialog(d);
        }
    }

}
