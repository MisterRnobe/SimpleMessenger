package server.core;

import common.objects.Body;
import common.objects.DialogList;
import common.objects.requests.DialogListRequest;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class GetDialogsHandler extends AbstractHandler<DialogListRequest> {
    public GetDialogsHandler(String login) {
        super(DialogListRequest.class, new String[]{"count"}, login);
    }

    @Override
    protected Body onHandle(DialogListRequest body) throws HandleError, SQLException {
        DialogList dialogList = new DialogList().setDialogs(DatabaseManager.getExtractor().getDialogs(login, body.getCount()));

        /*dialogList.getDialogs().stream().filter(di->di.getType() == DialogInfo.DIALOG).forEach(di-> {
            Optional<User> o = di.getUsers().stream().filter(u->!u.getLogin().equalsIgnoreCase(login)).findFirst();
            di.setPhoto(o.map(User::getAvatar).orElse(null));
        });*/
        return dialogList;
    }

}
