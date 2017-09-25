package window.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendPanel extends JPanel {
    private JTextField textField;
    private JButton send;
    SendPanel() {
        super(new BorderLayout());
        textField = new JTextField();
        send = new JButton("Send");
        send.addActionListener(new Send());
        this.add(textField, BorderLayout.CENTER);
        this.add(send, BorderLayout.EAST);


    }


    class Send implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // TODO: 24.09.17
        }
    }
}
