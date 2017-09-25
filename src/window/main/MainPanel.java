package window.main;

import manager.Manager;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    MainPanel()
    {
        super(new BorderLayout());
        OnlinePanel o = new OnlinePanel();
        SendPanel s = new SendPanel();
        TextPanel t = new TextPanel();

        this.add(o, BorderLayout.EAST);
        this.add(s, BorderLayout.SOUTH);
        this.add(t, BorderLayout.CENTER);

        Manager.instance(o,s,t);
    }
}
