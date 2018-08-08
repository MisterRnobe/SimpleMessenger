package client.window.main;

import client.application.ApplicationBank;
import client.application.DialogBean;
import client.network.queries.GetDialogQuery;
import client.network.queries.GetDialogsQuery;
import client.window.main.dialog.controllers.NewDialogWrapper;
import common.objects.User;
import javafx.application.Platform;
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
                ApplicationBank.getInstance().addDialogListener(change -> {
                    if (change.wasAdded())
                    {
                        mainWindowController.addDialogInfo(change.getValueAdded());
                    }
                });

                GetDialogsQuery.sendQuery(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void start(){
        instance = new MainWindowManager();

    }
    public void closeWindow()
    {
        mainWindowController.closeWindow();;
    }
    public void createEmptyDialog(User u)
    {
        mainWindowController.closeWindow();
        try {
            NewDialogWrapper wrapper = new NewDialogWrapper(u);
            mainWindowController.showDialog(wrapper.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setDialog(int dialogId)
    {
        DialogBean bean = ApplicationBank.getInstance().getDialogById(dialogId);
        if (bean.messages().size() == 0) {
            try {
                GetDialogQuery.sendQuery(dialogId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            mainWindowController.showDialog(bean.dialogId);
        }
    }

}
