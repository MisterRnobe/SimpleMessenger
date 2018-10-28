package common.objects;

public class FullDialog extends Body {
    private DialogMarker info;
    private Dialog dialog;

    public DialogMarker getInfo() {
        return info;
    }

    public void setInfo(DialogMarker info) {
        this.info = info;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

}
