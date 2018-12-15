package client.controllers;

import client.app.ApplicationManager;
import client.app.main.AbstractWindow;
import client.app.main.MainWindowManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class ImagePicker extends AbstractWindow {
    @FXML
    private AnchorPane imageViewPane;

    private ImagePreview imagePreview;

    private Consumer<WritableImage> onOk = b->{};
    @FXML
    private Button okButton;
    @FXML
    private void onClickCancel(){
        close();
    }
    @FXML
    private void initialize()
    {
        imagePreview = new ImagePreview(100);
        Group g = imagePreview.getRoot();
        imageViewPane.getChildren().add(g);
        g.translateXProperty().bind(imageViewPane.widthProperty().subtract(g.getBoundsInParent().getWidth()).divide(2));
        g.translateYProperty().bind(imageViewPane.heightProperty().subtract(g.getBoundsInParent().getHeight()).divide(2));

    }
    public static ImagePicker create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(ImagePicker.class.getResource("ImagePicker.fxml"));
        loader.load();
        return loader.getController();
    }
    @FXML
    private void onClickOk(ActionEvent event)
    {

        WritableImage image = null;
        try {
            image = getCroppedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        onOk.accept(image);
        MainWindowManager.getInstance().closeWindow();
        event.consume();
    }
    @FXML
    private void pickImage()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбрать картинку...");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение формата .png, .jpg",
                "*.png","*.jpg","*.jpeg"));
        File picture = chooser.showOpenDialog(ApplicationManager.getInstance().getMainStage());
        if (picture != null)
        {
            System.out.println(picture.getPath());
            Image image = new Image("file:///"+picture.getPath());
            System.out.println(picture.getPath());
            imagePreview.setPreview(image);
        }
    }
    //WritableImage
    public void setOnImageSelect(Consumer<WritableImage> callback)
    {
        this.onOk = callback;
    }
    private WritableImage getCroppedImage() throws IOException
    {
        double[] d = imagePreview.getBounds();
        Image image = imagePreview.getImage();

        PixelReader reader = image.getPixelReader();
        WritableImage wi = new WritableImage(reader, (int)d[0], (int)d[1], (int)d[2], (int) d[2]);

        return wi;

    }


}
