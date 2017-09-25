package window.main;

import javax.swing.*;

public class MainWindow {
    private JFrame frame;
    public static final int HEIGHT = 800;
    public static final int WIDTH = 1000;
    public static String name;
    private MainWindow(String name)
    {
        MainWindow.name = name;
        frame = new JFrame("Chat");

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainPanel());


        frame.setVisible(true);
    }
    public static void start()
    {
        new MainWindow("Name");
    }
}
