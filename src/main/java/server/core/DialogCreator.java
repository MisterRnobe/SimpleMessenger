package server.core;

import common.Errors;
import common.Request;
import server.DatabaseConnector;

import java.util.TreeMap;

public class DialogCreator {
    public static Response createConversation(Request request)
    {
        Response response = new Response();
        response.setType(Response.DIALOG_CREATION);
        String creator = request.getBody().get("creator"),
                partner = request.getBody().get("partner"),
                initialMessage = request.getBody().get("initialMessage");

        int dialogId = DatabaseConnector.getInstance().createDialog(creator, partner);
        if (dialogId == -1)
        {
            response.setStatus(Response.ERROR);
            response.setCode(Errors.INTERNAL_ERROR);
        }
        else
        {
            int messageId = DatabaseConnector.getInstance().addMessage(Integer.toString(dialogId), creator, initialMessage);
            DatabaseConnector.getInstance().setLastMessage(Integer.toString(dialogId), Integer.toString(messageId));
            response.setStatus(Response.OK);
            response.setBody(new TreeMap<String,String>(){{this.put("dialog_id",Integer.toString(dialogId));}});
        }

        return response;
    }
}
