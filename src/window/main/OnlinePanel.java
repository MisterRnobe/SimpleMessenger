package window.main;

import javax.swing.*;
import java.awt.*;

public class OnlinePanel extends JPanel {
    public static final int WIDTH = MainWindow.WIDTH/6;
    OnlinePanel()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Online: "));
        // TODO: 24.09.17  
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, super.getPreferredSize().height);
    }

    public void update()
    {
        // TODO: 24.09.17  
    }
}
