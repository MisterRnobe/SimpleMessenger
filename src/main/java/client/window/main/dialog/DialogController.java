package client.window.main.dialog;

import client.application.ApplicationBank;
import client.application.DialogBean;
import client.application.Listener;
import client.network.queries.SendMessageQuery;
import common.objects.Message;
import common.objects.User;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DialogController {
    @FXML
    private VBox messageBox;
    @FXML
    private Label title;
    @FXML
    private Label info;
    @FXML
    private BorderPane messageWindow;
    @FXML
    private ScrollPane messagePane;
    @FXML
    private TextField messageField;

    private int dialogId;


    public Parent getRoot()
    {
        return messageWindow;
    }
    public static DialogController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(DialogController.class.getResource("Dialog.fxml"));
        loader.load();
        return loader.getController();
    }
    public static DialogController create(int dialogId) throws IOException {
        DialogController controller = create();
        controller.bindDialog(dialogId);
        return controller;
    }
    private void addMessages(List<Message> messages) {
        messages.forEach(m->
            Platform.runLater(()->{
                try {
                    messageBox.getChildren().add(MessageController.create(m));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }));
    }
    private void bindDialog(int dialogId)
    {
        this.dialogId = dialogId;
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        title.textProperty().bind(dialogBean.titleProperty());
        addMessages(dialogBean.messages());

        ApplicationBank.getInstance().addUserStatusListener(new UserStatusListener());
//        messageWindow.parentProperty().addListener((observable, oldValue, newValue) -> {
//            try {
//                GetUserStatusQuery.sendQuery(
//                        ApplicationBank.getInstance().getDialogById(dialogId)
//                                .getPartners(ApplicationBank.getInstance().getLogin()).get(0)
//                );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
        dialogBean.messages().addListener((ListChangeListener<Message>) c -> {
            while (c.next())
            {
                if (c.wasAdded()) {
                    //WTF?!?
                    List<Message> l = c.getAddedSubList().stream().map((Function<Message, Message>)m->m)
                            .collect(Collectors.toList());
                    addMessages(l);
                }
            }
        });

    }
    @FXML
    private void sendMessage()
    {
        String text = messageField.getText();
        if (text.length() != 0)
        {
            try {
                SendMessageQuery.sendQuery(dialogId, text);
                messageField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class UserStatusListener implements Listener<User>
    {

        @Override
        public void onHandle(User user) {
            Platform.runLater(()-> {
                if (user.isOnline()) {
                    info.setText("Online");
                } else {
                    Date d = new Date(user.getLastOnline());
                    info.setText(String.format("Last seen %tc", d));
                }
            });

        }
    }
    //    public void updateMessages()
//    {
//        MessageList l = Bank.getDialogs().get(currentDialog);
//        Calendar mainCalendar = Calendar.getInstance();
//        mainCalendar.setTimeInMillis(System.currentTimeMillis());
//        int lastDifference = 0;
//        for (int i = messageCount; i < l.messages.size(); i++) {
//            try {
//                Message m = l.messages.get(i);
//                User u = currentDialog.getUserByID(m.usedID);
//                String sender = (u == null)? null: u.getFullName();
//
//                Calendar tempCalendar = Calendar.getInstance();
//                tempCalendar.setTimeInMillis(m.time);
//                if (mainCalendar.get(Calendar.DAY_OF_YEAR) - tempCalendar.get(Calendar.DAY_OF_YEAR) != lastDifference)
//                {
//                    lastDifference = mainCalendar.get(Calendar.DAY_OF_YEAR) - tempCalendar.get(Calendar.DAY_OF_YEAR);
//                    Label separator = new Label(tempCalendar.get(Calendar.DAY_OF_MONTH)+ " "+
//                            (tempCalendar.get(Calendar.MONTH)+1));
//                    separator.setTextFill(Color.grayRgb(255));
//                    separator.setBackground(
//                            new Background(new BackgroundFill(
//                                    Color.grayRgb(91, 0.6471),
//                                    new CornerRadii(2d),
//                                    Insets.EMPTY
//                            ))
//                    );
//                    BorderPane borderPane = new BorderPane();
//                    borderPane.setCenter(separator);
//                    messageBox.getChildren().add(borderPane);
//
//                }
//
//                messageBox.getChildren().add(MessageController.create(l.messages.get(i), sender));
//                messageCount++;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
