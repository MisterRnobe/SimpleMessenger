package client.utils;

import client.network.queries.GetFileQuery;
import common.objects.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 *
 */
public class AvatarSupplier {

    private static AvatarSupplier instance;
    private CallbackMap<Image> callbackMap = new CallbackMap<>();
    private TreeMap<String, Image> avatars = new TreeMap<>();

    private AvatarSupplier(){}

    public void getAvatar(String path, Consumer<Image> callback)
    {
        if (path == null)
            return;
        if (avatars.containsKey(path))
            callback.accept(avatars.get(path));
        else
        {
            try {
                callbackMap.put(path, callback);
                GetFileQuery.sendQuery(path, this::callbackImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void callbackImage(File file)
    {
        if (file.getFile() == null)
            return;
        Image image = fileToImage(file);
        callbackMap.get(file.getPath()).forEach(c->c.accept(image));
        avatars.put(file.getPath(), image);
    }
    public static Image paintDefaultAvatar(String name)
    {
        int size = 200;
        List<Color> availableColors = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0 || i == 1 && j == 1 && k == 1)
                        continue;
                    availableColors.add(new Color(i*255, j*255, k*255));
                }
            }
        }
        Character[] letters = Arrays.stream(name.split(" ")).map(s->s.charAt(0)).toArray(Character[]::new);

        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setPaint(availableColors.get( letters[0] % availableColors.size()));
        graphics.fillRect(0,0, size, size);
        graphics.setPaint(Color.white);
        String text = letters[0].toString().toUpperCase();
        graphics.setFont(new Font("Arial", Font.BOLD, 120));
        if (letters.length > 1)
            text += letters[1];
        FontMetrics fm = graphics.getFontMetrics();
        int x = (size - fm.stringWidth(text))/2;
        int y = fm.getHeight();
        graphics.drawString(text, x, y);
        // TODO: 12.10.2018 implement

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }


    public static AvatarSupplier getInstance() {
        if (instance == null)
            instance = new AvatarSupplier();
        return instance;
    }
    private static Image fileToImage(File f)
    {
        Image image = null;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(f.getFile())) {
            BufferedImage bi = ImageIO.read(stream);
            image = SwingFXUtils.toFXImage(bi, null);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить фотку((");
        }
        return image;
    }
}
