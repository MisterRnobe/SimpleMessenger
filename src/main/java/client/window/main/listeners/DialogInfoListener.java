package client.window.main.listeners;

import client.application.Listener;
import client.window.main.MainWindowController;
import common.objects.DialogInfo;

public class DialogInfoListener implements Listener<DialogInfo> {
    private MainWindowController mainWindowController;

    public DialogInfoListener(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @Override
    public void onHandle(DialogInfo dialogInfo) {
        mainWindowController.addDialogInfo(dialogInfo);
    }
}
