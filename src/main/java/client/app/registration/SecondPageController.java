package client.app.registration;

import client.controllers.ImagePreview;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SecondPageController extends PageController {
    @FXML
    private VBox box;

    private ImagePreview preview;


    @FXML
    private void onClickLoad() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбрать картинку...");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение формата .png, .jpg",
                "*.png", "*.jpg", "*.jpeg"));
        File avatar = chooser.showOpenDialog(RegistrationController.getStage());
        if (avatar != null) {
            System.out.println(avatar.getPath());
            Image image = new Image("file:///" + avatar.getPath());
            System.out.println(avatar.getPath());
            preview.setPreview(image);
        }
    }

    @FXML
    private void onClickNext() {
        double[] d = preview.getBounds();
        Image image = preview.getImage();

        PixelReader reader = image.getPixelReader();
        WritableImage wi = new WritableImage(reader, (int) d[0], (int) d[1], (int) d[2], (int) d[2]);

        BufferedImage bi = SwingFXUtils.fromFXImage(wi, null);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", stream);
            byte[] bytes = stream.toByteArray();
            System.out.println("SIZE: " + bytes.length);
            RegistrationController.finish(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancel() {
        RegistrationController.cancel();
    }

    @FXML
    private void skip() {
        RegistrationController.finish(null);
    }

    @FXML
    private void initialize() {
        preview = new ImagePreview(100);
        box.getChildren().add(0, preview.getRoot());
    }

    public static SecondPageController create() throws IOException {
        FXMLLoader loader = new FXMLLoader(SecondPageController.class.getResource("SecondPageForm.fxml"));
        loader.load();
        return loader.getController();
    }

    @Override
    public void displayMessage(String message) {
        System.out.println("Ошибка: " + message);
    }
}
