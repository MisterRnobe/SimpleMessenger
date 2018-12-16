package server.core;

import common.objects.Body;
import common.objects.requests.DialogListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.dto.responses.DialogCommonData;
import server.dto.responses.DialogListData;
import server.service.DialogProviderService;

import java.sql.SQLException;
import java.util.List;

@Component
public class GetDialogsHandler extends AbstractHandler<DialogListRequest> {

    private DialogProviderService dialogProviderService;

    @Autowired
    public GetDialogsHandler(DialogProviderService dialogProviderService) {
        super(DialogListRequest.class, new String[]{"count"}, null);
        this.dialogProviderService = dialogProviderService;
    }

    @Override
    protected Body onHandle(DialogListRequest body) throws HandleError {
        List<DialogCommonData> dialogs = dialogProviderService.findDialogsForUser(login, 0, 0);
        DialogListData dialogListData = new DialogListData();
        dialogListData.setDialogCommonData(dialogs);
        //DialogList dialogList = new DialogList().setDialogs(DatabaseManager.getExtractor().getDialogs(login, body.getCount()));

        /*dialogList.getDialogs().stream().filter(di->di.getType() == DialogInfo.DIALOG).forEach(di-> {
            Optional<UserDB> o = di.getUserDBS().stream().filter(u->!u.getLogin().equalsIgnoreCase(login)).findFirst();
            di.setPhoto(o.map(UserDB::getAvatar).orElse(null));
        });*/
        return dialogListData;
    }

}
