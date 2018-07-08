package server.core;

import common.Request;
import server.DatabaseConnector;

import java.util.TreeMap;

public class Message {
    public static Response sendMessage(Request r)
    {
        Response response = new Response();
        response.setType(Response.SEND_MESSAGE);
        String sender = r.getBody().get("login"),
                dialogId = r.getBody().get("dialog_id"),
                text = r.getBody().get("text");
        int messageId = DatabaseConnector.getInstance().addMessage(dialogId, sender, text);
        DatabaseConnector.getInstance().setLastMessage(dialogId, Integer.toString(messageId));
        response.setCode(Response.OK);
        TreeMap<String, String> reply = new TreeMap<>();
        reply.put("sender", sender);
        reply.put("dialog_id", dialogId);
        reply.put("text", text);
        reply.put("time", Long.toString(System.currentTimeMillis()));
        response.setBody(reply);
        return response;
    }
}
