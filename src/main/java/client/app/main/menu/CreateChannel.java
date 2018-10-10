package client.app.main.menu;

import client.app.main.AbstractWindow;
import client.controllers.menu.CreateGroupChannelController;
import client.network.queries.CreateDialogQuery;

import java.io.IOException;

public class CreateChannel extends AbstractWindow {

    public CreateChannel() throws IOException{
        CreateGroupChannelController controller = CreateGroupChannelController.create();
        this.root = controller.getRoot();
        controller.setOnClick(CreateDialogQuery::sendChannelQuery);
    }

}
