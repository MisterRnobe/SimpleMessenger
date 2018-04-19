package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ApplicationStarter extends Application{
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
        AnchorPane p = loader.load();
        primaryStage.setScene(new Scene(p));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
         launch(args);
    }

    @Override
    public void stop() {

    }
}
