package client.controllers.dialog;

import client.network.queries.GetUserQuery;
import client.suppliers.AbstractDialogBean;
import client.suppliers.DialogManager;
import client.suppliers.UserSupplier;
import common.objects.Message;
import common.objects.User;
import common.objects.requests.DialogTypes;
import javafx.application.Platform;
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
    private Label senderLabel;
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
        if (m.getIsSystem())
        {
            vBox.getChildren().remove(senderLabel);
            box.setAlignment(Pos.BASELINE_CENTER);
            vBox.setAlignment(Pos.CENTER);
        }

        else if (m.getSender().equals(UserSupplier.getInstance().getMyLogin()))
        {
            vBox.getChildren().remove(senderLabel);
            box.setAlignment(Pos.BASELINE_RIGHT);
            vBox.setAlignment(Pos.TOP_LEFT);
        }
        else
        {
            box.setAlignment(Pos.BASELINE_LEFT);
            vBox.setAlignment(Pos.TOP_LEFT);
            AbstractDialogBean b = DialogManager.getInstance().getDialogById(m.getDialogId());
            if (b.type == DialogTypes.DIALOG)
                vBox.getChildren().remove(senderLabel);
            else if (b.type == DialogTypes.GROUP) {
                UserSupplier.getInstance().supply(m.getSender(), u-> Platform.runLater(()->setUser(u)));
            }
            else
            {
                senderLabel.setText(b.title().getValue()+" :");
            }
        }

    }
    private void setUser(User user)
    {
        senderLabel.setText(user.getName()+" :");
    }
    public static Node create(Message m) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(MessageController.class.getResource("MessageDB.fxml"));
        Node node = loader.load();
        MessageController controller = loader.getController();
        controller.setMessage(m);
        return node;
    }
}
