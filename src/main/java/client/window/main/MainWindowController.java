package client.window.main;

import client.application.DialogBean;
import client.window.main.dialog.DialogController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    public void addDialogInfo(DialogBean dialogInfo)
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
    public void showDialog(int dialogId)
    {
        Platform.runLater(()-> {
            try {
                DialogController c = dialogControllerMap.get(dialogId);
                if (c == null) {
                    c = DialogController.create(dialogId);
                    dialogControllerMap.put(dialogId, c);
                }
                dialogPane.getChildren().clear();
                dialogPane.getChildren().add(c.getRoot());
                currentDialog = c;
            } catch (IOException e) {
                e.printStackTrace();
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
