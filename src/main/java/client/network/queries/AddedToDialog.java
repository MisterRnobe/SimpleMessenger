package client.network.queries;

import com.alibaba.fastjson.JSONObject;
import common.Response;

import java.io.IOException;

public class AddedToDialog {
    public static void onHandle(Response r) {
        if (r.getStatus() == Response.OK) {
            System.out.println("Добавили в диалог!");
            int dialogId = r.getBody().getInteger("dialogId");
            try {
                GetDialogInfoQuery.send(dialogId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
