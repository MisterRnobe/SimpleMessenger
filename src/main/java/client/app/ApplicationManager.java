package client.app;

import client.app.initial.InitialWindowController;
import client.app.main.MainWindowManager;
import client.controllers.ErrorController;
import client.controllers.ImagePicker;
import client.network.SocketConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationManager extends Application{
    private static ApplicationManager instance;
    private Stage mainStage;
    public void start(Stage primaryStage) throws Exception {
        SocketConnection.connect();
        primaryStage.setResizable(false);
        this.mainStage = primaryStage;
        Parent p = InitialWindowController.create();
        primaryStage.setScene(new Scene(p));
        primaryStage.show();
        instance = this;
    }

    public static void main(String[] args) {
         launch(args);
    }
    @Override
    public void stop() {
        SocketConnection.close();
    }

    public static void launchMainWindow()
    {
        Platform.runLater(()->{
            try {

                MainWindowManager.start(instance.mainStage);
            } catch (IOException e) {
                e.printStackTrace();
            }});
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public static ApplicationManager getInstance(){
        return instance;
    }
}
