package client.app.main.window;

import common.objects.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class UserSelectingElementController {
    @FXML
    private AnchorPane root;
    @FXML
    private Text login, name;
    @FXML
    private Circle avatar;
    private boolean selected = false;

    void setOnClick(Consumer<String> select, Consumer<String> deselect)
    {
        root.setOnMouseClicked(e->
        {

            if (selected)
            {
                deselect.accept(login.getText());
                selected = !selected;
                root.setStyle("-fx-background-color: white");
            }
            else
            {
                select.accept(login.getText());
                selected = !selected;
                root.setStyle("-fx-background-color: lightblue");

            }
        });
    }
    public Pane getRoot()
    {
        return root;
    }
    public String getName()
    {
        return name.getText();
    }
    public String getLogin()
    {
        return login.getText();
    }
    private void setPhoto(byte[] avatar)
    {
        if (avatar == null)
            return;
        try(ByteArrayInputStream stream = new ByteArrayInputStream(avatar))
        {
            BufferedImage bi = ImageIO.read(stream);
            Image im = SwingFXUtils.toFXImage(bi, null);
            this.avatar.setFill(new ImagePattern(im));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить фотку((");
        }
    }

    public static UserSelectingElementController create(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(UserSelectingElementController.class.getResource("UserSelectingElement.fxml"));
        loader.load();
        UserSelectingElementController c = loader.getController();
        c.login.setText(user.getLogin());
        c.name.setText(user.getName());
        //c.setPhoto(user.getAvatar());
        return c;
    }
}
