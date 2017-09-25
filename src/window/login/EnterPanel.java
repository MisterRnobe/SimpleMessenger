package window.login;

import server.Server;
import window.main.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class EnterPanel extends JPanel {
    private JTextField textField;
    EnterPanel()
    {
        textField = new JTextField(10);
        this.add(textField);
        this.add(new Button());
    }

    class Button extends JButton
    {
        Button()
        {
            super("Enter");
            this.addActionListener(new ButtonListener());
        }
    }
    class ButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String login = textField.getText();
            if ( login == null)
                return;
            ChatSocket.connect(login);
            LoginWindow.stop();
            MainWindow.start();
        }
    }
}
