package client;

import client.app.registration.ImagePreview;
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
import javafx.stage.Stage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.LinkedList;
import java.util.concurrent.Future;

public class Tester extends Application {
    public static void main(String[] args) {
        Tester.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane anchorPane = new AnchorPane();
        ImagePreview preview = new ImagePreview(100);
        anchorPane.getChildren().add(preview.getRoot());


        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
