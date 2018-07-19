package client.window.main;

import client.application.ApplicationBank;
import client.network.queries.GetDialogsQuery;
import common.objects.requests.DialogListRequest;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowStarter {

    public static void start(){
        Platform.runLater(() ->
                {
                    Stage stage = ApplicationBank.getStage();
                    stage.setTitle("Simple messenger");
                    stage.setResizable(true);
                    loadScene();
                });

        //stage.close();
        //ServerListener.getInstance().send(new UserQuery(login)).send(new DialogQuery(15));

        //stage.show();
    }
    public static void loadScene()
    {
        FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("MainWindow.fxml"));
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            //Platform.runLater(() -> {
                ApplicationBank.getStage().setScene(scene);
                ApplicationBank.getStage().show();
            //});
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
