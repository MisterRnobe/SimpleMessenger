package common.objects;

import java.util.ArrayList;
import java.util.List;


public class DialogList extends Body{
    private List<DialogMarker> dialogs = new ArrayList<>();

    public DialogList() {
    }

    public List<DialogMarker> getDialogs() {
        return dialogs;
    }

    public DialogList setDialogs(List<DialogMarker> dialogs) {
        this.dialogs = dialogs;
        return this;
    }

    public void addDialog(DialogMarker dialogInfo)
    {
        dialogs.add(dialogInfo);
    }
}
