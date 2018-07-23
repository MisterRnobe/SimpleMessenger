package client.window.main;

import client.application.ApplicationBank;
import client.network.queries.GetDialogsQuery;
import client.network.queries.SendMessageQuery;
import client.window.main.dialog.DialogController;
import client.window.main.listeners.DialogInfoListener;
import client.window.main.listeners.DialogListener;
import common.objects.Dialog;
import common.objects.DialogInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MainWindowController {
    @FXML
    private VBox chooserBox;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane dialogPane;


    private DialogController currentDialog = null;

    private final Map<Integer, DialogController> dialogControllerMap = new TreeMap<>();
    private final List<DialogChooserController> chooserControllerList = new LinkedList<>();

    @FXML
    private void initialize()
    {
        //DialogChooserController c = DialogChooserController.create();
    }
    public void addDialogInfo(DialogInfo dialogInfo)
    {
        Platform.runLater(()-> {
            try {
                DialogChooserController controller = DialogChooserController.create(dialogInfo);
                chooserBox.getChildren().add(controller.getPane());
                chooserControllerList.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void setDialog(Dialog d)
    {
        Platform.runLater(()-> {
            try {
                DialogController c = dialogControllerMap.get(d.getDialogId());
                if (c == null) {
                    c = DialogController.create(d);
                    dialogControllerMap.put(d.getDialogId(), c);
                }
                dialogPane.getChildren().clear();
                dialogPane.getChildren().add(c.getRoot());
                currentDialog = c;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void updateDialog(int dialogId)
    {
        Platform.runLater(()->
        {
            chooserControllerList.stream().filter(c->c.getDialogId()==dialogId).findAny().get().update();
            DialogController controller = dialogControllerMap.get(dialogId);
            if (controller != null)
            {
                controller.update();
            }
        });
    }

    public Node getRoot()
    {
        return rootPane;
    }
    public static MainWindowController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
        loader.load();
        return loader.getController();
    }

}
