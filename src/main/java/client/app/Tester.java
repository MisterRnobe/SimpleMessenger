package client.app;

import client.app.initial.InitialWindowController;
import client.app.main.MainWindowManager;
import client.controllers.ImagePicker;
import client.network.SocketConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Tester extends Application {
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);

        Parent p = ImagePicker.create().getRoot();
        primaryStage.setScene(new Scene(p));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
