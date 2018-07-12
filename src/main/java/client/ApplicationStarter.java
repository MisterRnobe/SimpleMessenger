package client;

import client.initial.InitialWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eclipse.jetty.client.HttpClient;

public class ApplicationStarter extends Application{
    public static HttpClient httpClient;
    public void start(Stage primaryStage) throws Exception {
        httpClient = new HttpClient();
        httpClient.start();
        primaryStage.setResizable(false);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInWindow.fxml"));
//        AnchorPane p = loader.load();
        Parent p = InitialWindowController.create();
        primaryStage.setScene(new Scene(p));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(new InitialWindowController().toString());
         launch(args);
    }

    @Override
    public void stop() {
        try {
            httpClient.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
