package client.window.main.listeners;

import client.application.Listener;
import client.window.main.MainWindowController;
import common.objects.Dialog;

public class DialogListener implements Listener<Dialog> {
    private MainWindowController mainWindowController;

    public DialogListener(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @Override
    public void onHandle(Dialog dialog) {
        mainWindowController.setDialog(dialog);
    }
}
