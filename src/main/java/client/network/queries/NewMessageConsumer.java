package client.network.queries;

import common.Response;

import java.io.IOException;

public class NewMessageConsumer {
    public static void onHandle(Response r) {
        System.out.println("Меня добавили в новый диалог/группу/канал!");
        try {
            GetDialogQuery.sendQuery(r.getBody().getInteger("messageId"), d-> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
