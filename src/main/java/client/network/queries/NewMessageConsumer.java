package client.network.queries;

import common.Response;

import java.io.IOException;

public class NewMessageConsumer {
    public static void onHandle(Response r) {
        System.out.println("Мне пришло новое сообщение!");
        try {
            GetDialogQuery.sendQuery(r.getBody().getInteger("messageId"), d-> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
