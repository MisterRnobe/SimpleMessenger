package manager;

import window.main.MainPanel;
import window.main.OnlinePanel;
import window.main.SendPanel;
import window.main.TextPanel;

public class Manager {
    private OnlinePanel onlinePanel;
    private SendPanel sendPanel;
    private TextPanel textPanel;
    private static Manager instance;

    private Manager(OnlinePanel onlinePanel, SendPanel sendPanel, TextPanel textPanel) {
        this.onlinePanel = onlinePanel;
        this.sendPanel = sendPanel;
        this.textPanel = textPanel;
    }
    public static Manager instance(OnlinePanel onlinePanel, SendPanel sendPanel, TextPanel textPanel)
    {
        if (instance == null)
            instance = new Manager(onlinePanel, sendPanel, textPanel);
        return instance;
    }

}
