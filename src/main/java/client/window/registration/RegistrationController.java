package client.window.registration;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {
    private static Parent CURRENT_PARENT;
    private static Stage STAGE;
    public static void startRegistration()
    {
        STAGE = new Stage();

        try {
            CURRENT_PARENT = FirstPageController.create();
            STAGE.setScene(new Scene(CURRENT_PARENT));
            STAGE.initModality(Modality.APPLICATION_MODAL);
            STAGE.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void secondPage()
    {
        try
        {
            CURRENT_PARENT = SecondPageController.create();
            STAGE.getScene().setRoot(CURRENT_PARENT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return STAGE;
    }
    public static void finish()
    {

    }
    public static void cancel()
    {
        STAGE.close();
    }

}
