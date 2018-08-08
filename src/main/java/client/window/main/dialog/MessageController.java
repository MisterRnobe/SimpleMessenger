package client.window.main.dialog;

import client.application.ApplicationBank;
import common.objects.Message;
import common.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Date;


public class MessageController {
    @FXML
    private Label msgLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private VBox vBox;
    @FXML
    private HBox box;

    private Message message;

    @FXML
    private void initialize()
    {
        vBox.setBackground(new Background(new BackgroundFill(Color.rgb(148, 234, 255),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }
    private void setMessage(Message m)
    {
        this.message = m;
        msgLabel.setText(message.getText());
        timeLabel.setText(String.format("%tR", new Date(m.getTime())));
        if (m.getSender().equals(ApplicationBank.getInstance().getLogin()))
        {
            vBox.getChildren().remove(userLabel);
            box.setAlignment(Pos.BASELINE_RIGHT);
            vBox.setAlignment(Pos.TOP_LEFT);
        }
        else if (m.getSender().equals("null"))
        {
            vBox.getChildren().remove(userLabel);
            box.setAlignment(Pos.BASELINE_CENTER);
            vBox.setAlignment(Pos.CENTER);
        }
        else
        {
            User u = ApplicationBank.getInstance().getUserByLogin(m.getSender());
            userLabel.setText(u.getName()+" :");
            box.setAlignment(Pos.BASELINE_LEFT);
            vBox.setAlignment(Pos.TOP_LEFT);
        }

    }
    public static Node create(Message m) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(MessageController.class.getResource("Message.fxml"));
        Node node = loader.load();
        MessageController controller = loader.getController();
        controller.setMessage(m);
        return node;
    }
}
