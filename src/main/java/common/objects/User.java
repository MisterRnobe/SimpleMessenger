package common.objects;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class User extends Body {
    private String login;
    private String name;
    private boolean isOnline;
    private long lastOnline;
    private byte[] avatar;
    private Image image = null;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
    public Image FXImage()
    {
        if (avatar == null)
            return null;
        if (image == null) {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(avatar)) {
                BufferedImage bi = ImageIO.read(stream);
                this.image = SwingFXUtils.toFXImage(bi, null);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Не удалось загрузить фотку((");
            }
        }
        return image;
    }
}
