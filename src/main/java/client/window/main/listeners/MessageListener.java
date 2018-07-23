package client.window.main.listeners;

import client.application.Listener;
import client.window.main.MainWindowController;
import common.objects.Message;

public class MessageListener implements Listener<Message> {
    private final MainWindowController controller;

    public MessageListener(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void onHandle(Message message) {
        controller.updateDialog(message.getDialogId());
    }
}
