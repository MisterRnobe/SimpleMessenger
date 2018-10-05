package server.core;

import common.Errors;
import common.Response;
import common.objects.Body;
import common.objects.requests.CreateDialogRequest;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class CreateDialogHandler extends AbstractHandler<CreateDialogRequest> {


    public CreateDialogHandler(String login) {
        super(CreateDialogRequest.class, new String[]{"partner", "initialMessage"}, login);
    }

    @Override
    protected Body onHandle(CreateDialogRequest body) throws HandleError, SQLException {
        String creator = login,
                partner = body.getPartner(),
                initialMessage = body.getInitialMessage();
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        int dialogId = extractor.createDialog(creator, partner);
        if (dialogId == -1)
        {
            throw new HandleError(Errors.INTERNAL_ERROR);
        }
        int messageId = extractor.addMessage(dialogId, creator, initialMessage, System.currentTimeMillis());
        extractor.setLastMessage(dialogId, messageId);
        return extractor.getFullDialog(dialogId);
    }

    @Override
    protected void beforeSend(final Response response) {
//        if (response.getStatus() == Response.OK)
//        {
//            List<String> users = response.getBody().getJSONObject("dialogInfo")
//                    .getJSONArray("users")
//                    .stream()
//                    .map(o-> ((JSONObject)o).getString("login"))
//                    .filter(s->!s.equalsIgnoreCase(login))
//                    .collect(Collectors.toList());
//            Response r = new Response();
//            r.setStatus(response.getStatus());
//            r.setType(response.getType());
//            r.setBody(response.getBody().getJSONObject("dialogInfo"));
//            OnlineManager.getInstance().sendAll(r, users);
//        }
    }
}
