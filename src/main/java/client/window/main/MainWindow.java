package client.window.main;

import client.network.queries.GetDialogsQuery;
import common.objects.DialogInfo;
import common.objects.DialogList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Comparator;


public class MainWindow {
    private static MainWindow instance;
    public static MainWindow getInstance()
    {
        return instance;
    }

    @FXML
    private VBox chooserBox;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label title;
    @FXML
    private Label info;
    @FXML
    private BorderPane messageWindow;
    @FXML
    private ScrollPane messagePane;
    @FXML
    private TextField messageField;

    @FXML
    private void initialize()
    {
        instance = this;
        try {
            GetDialogsQuery.sendQuery(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //DialogChooserController c = DialogChooserController.create();
    }
    public void addDialogs(DialogList list)
    {
        Platform.runLater(()-> {
            list.getDialogs().sort(Comparator.comparingLong(d -> d.getLastMessage().getTime()));
            list.getDialogs().forEach(d -> {
                try {
                    chooserBox.getChildren().add(DialogChooserController.create(d).getPane());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }


}
