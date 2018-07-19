package common.objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DialogList extends Body{
    private List<DialogInfo> dialogs = new ArrayList<>();

    public DialogList() {

    }

    public List<DialogInfo> getDialogs() {
        return dialogs;
    }

    public void setDialogs(List<DialogInfo> dialogs) {
        this.dialogs = dialogs;
    }
    public void addDialog(DialogInfo dialogInfo)
    {
        dialogs.add(dialogInfo);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONArray array = dialogs.stream().map(d -> (Object) d).collect(Collectors.toCollection(JSONArray::new));
        JSONObject o = new JSONObject();
        o.put("dialogs", array);
        return o;
    }
}
