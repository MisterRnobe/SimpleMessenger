package client.app.registration;

import client.utils.ControllerLoader;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SecondPageController {
    @FXML
    private VBox box;

    private ImagePreview preview;
    private static final String FILE_NAME = "SecondPageForm.fxml";


    @FXML
    private void onClickLoad()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбрать картинку...");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение формата .png, .jpg",
                "*.png","*.jpg","*.jpeg"));
        File avatar = chooser.showOpenDialog(RegistrationController.getStage());
        if (avatar != null)
        {
            System.out.println(avatar.getPath());
            Image image = new Image("file:///"+avatar.getPath());
            preview.setPreview(image);
            //preview.setFill(new ImagePattern(image,0,0,1,1,true));
        }
    }

    @FXML
    private void onClickNext()
    {
        double[] d = preview.getBounds();
        Image image = preview.getImage();

        PixelReader reader = image.getPixelReader();
        WritableImage wi = new WritableImage(reader, (int)d[0], (int)d[1], (int)d[2], (int) d[2] );

        BufferedImage bi = SwingFXUtils.fromFXImage(wi, null);
        //RegistrationController.finish();
        //RegistrationController.getQuery().setAvatar(TransmittableImage.createFrom(bi));
        //RegistrationHandler.enableThirdPage();
    }

    @FXML
    private void onCancel()
    {
        RegistrationController.cancel();
    }
    @FXML
    private void initialize()
    {
        preview = new ImagePreview(100);
        box.getChildren().add(0, preview.getRoot());
    }
    public static Parent create() throws IOException
    {
        return ControllerLoader.create(SecondPageController.class.getResource(FILE_NAME));
    }
}
