package client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

@Deprecated
public class ControllerLoader {
    @Deprecated
    public static Parent create(URL fileName) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(fileName);
        return (Parent) loader.load();
    }
}
