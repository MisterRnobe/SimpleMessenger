package client.network.queries;

import client.suppliers.DialogManager;
import client.network.ClientSocket;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.requests.DialogListRequest;

import java.io.IOException;
import java.util.Arrays;

public class GetDialogsQuery {
    public static void sendQuery(int count) throws IOException {
        DialogListRequest request = new DialogListRequest();
        request.setCount(count);
        Request r = new Request().setMethod(Methods.GET_DIALOGS).setBody(request.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            JSONArray array = response.getBody().getJSONArray("dialogs");
            JSONObject[] objects = new JSONObject[array.size()];
            Arrays.setAll(objects, array::getJSONObject);
            Arrays.stream(objects).forEach(o-> DialogManager.getInstance().addDialog(o));
        }

    }
}
