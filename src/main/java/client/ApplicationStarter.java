package client;

import client.application.ApplicationBank;
import client.network.SocketConnection;
import client.window.initial.InitialWindowController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.jetty.client.HttpClient;

public class ApplicationStarter extends Application{
    public void start(Stage primaryStage) throws Exception {
        SocketConnection.connect();
        primaryStage.setResizable(false);
        ApplicationBank.setStage(primaryStage);
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
        SocketConnection.close();
    }
}
