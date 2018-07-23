package client.window.main.dialog;

import client.application.ApplicationBank;
import client.application.Listener;
import client.network.queries.GetUserStatusQuery;
import client.network.queries.SendMessageQuery;
import common.objects.Dialog;
import common.objects.Message;
import common.objects.User;
import javafx.application.Platform;
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

    private void initListener()
    {
        ApplicationBank b = ApplicationBank.getInstance();
        ApplicationBank.getInstance().addUserStatusListener(new UserStatusListener());
        try {
            GetUserStatusQuery.sendQuery(
                    ApplicationBank.getInstance().getDialogInfoById(dialogId).getPartners(ApplicationBank.getInstance().getLogin()).get(0)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public static DialogController create(Dialog d) throws IOException {
        DialogController controller = create();
        controller.dialogId = d.getDialogId();
        controller.title.setText(ApplicationBank.getInstance().getDialogInfoById(d.getDialogId()).getDialogName());
        controller.addMessages(d.getMessages());
        controller.initListener();
        return controller;
    }
    private void addMessages(List<Message> messages) {
        messages.forEach(m->{
            try {
                messageBox.getChildren().add(MessageController.create(m));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void update()
    {
        Dialog d = ApplicationBank.getInstance().getDialogById(dialogId);
        int messageCount = messageBox.getChildren().size();
        List<Message> list = d.getMessages().subList(messageCount, d.getMessages().size());
        addMessages(list);
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
