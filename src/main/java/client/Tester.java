package client;

import client.app.registration.ImagePreview;
import client.app.registration.SecondPageController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.Message;
import common.objects.requests.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.concurrent.Future;

public class Tester extends Application {
    public static void main(String[] args) {
        Tester.launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Pane root = SecondPageController.create().getRoot();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void foo(Stage primaryStage)
    {
        VBox root = new VBox();
        Button b = new Button("DOROWA");
        root.setSpacing(5d);
        ImagePreview preview = new ImagePreview(100);
        root.getChildren().addAll(preview.getRoot(), b);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
