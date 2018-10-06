package client.utils;

import client.network.queries.GetFileQuery;
import common.objects.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 *
 */
public class FileManager {

    private static FileManager instance;
    private CallbackMap<Image> callbackMap = new CallbackMap<>();
    private TreeMap<String, Image> avatars = new TreeMap<>();

    private FileManager(){}

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


    public static FileManager getInstance() {
        if (instance == null)
            instance = new FileManager();
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
