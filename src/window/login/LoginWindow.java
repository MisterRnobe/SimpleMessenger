package window.login;

import javax.swing.*;
import java.awt.*;

public class LoginWindow {
    private JFrame frame;
    public static final int HEIGHT = 200;
    public static final int WIDTH = 300;

    private static LoginWindow instance;

    private LoginWindow()
    {
        frame = new JFrame("Login");
        frame.setSize(WIDTH,HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter as:"));
        panel.add(new EnterPanel());

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        //frame.setContentPane(panel);

        frame.setVisible(true);
        instance = this;
    }
    public static void start()
    {
        if (instance == null)
            new LoginWindow();
    }
    public static void stop()
    {
        if (instance!= null)
            instance.frame.dispose();
    }
}
