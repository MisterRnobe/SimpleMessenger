package client.app.main.menu.newchannel;

import client.network.queries.CreateDialogQuery;
import client.app.main.menu.AbstractWindow;
import client.app.main.menu.newgroup.CreateGroupController;

import java.io.IOException;

public class ChannelWindowController extends AbstractWindow {

    private ChannelWindowController(){}
    public static ChannelWindowController create() throws IOException
    {
        CreateGroupController controller = CreateGroupController.create();
        ChannelWindowController channelWindowController = new ChannelWindowController();
        controller.setOnClick(CreateDialogQuery::sendChannelQuery);
        channelWindowController.root = controller.getRoot();
        return channelWindowController;
    }
}
