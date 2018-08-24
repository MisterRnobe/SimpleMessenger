package client.window.main.menu.newchannel;

import client.network.queries.CreateDialogQuery;
import client.window.main.menu.AbstractWindow;
import client.window.main.menu.newgroup.UserSelectingListController;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChannelWindowController extends AbstractWindow {
    private UserSelectingListController controller;

    private ChannelWindowController(){}
    public static ChannelWindowController create() throws IOException
    {
        UserSelectingListController controller = UserSelectingListController.create();
        ChannelWindowController channelWindowController = new ChannelWindowController();
        channelWindowController.controller = controller;
        controller.setOnClick(CreateDialogQuery::sendChannelQuery);
        channelWindowController.root = controller.getRoot();
        return channelWindowController;
    }
}
