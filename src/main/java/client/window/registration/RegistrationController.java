package client.window.registration;

import client.network.queries.RegistrationQuery;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RegistrationController {
    private static FirstPageController controller;
    private static Stage STAGE;
    public static void startRegistration()
    {
        STAGE = new Stage();
        RegistrationQuery.setErrorListener(RegistrationController::onError);
        RegistrationQuery.setSuccessListener(RegistrationController::onSuccess);
        try {
            controller = FirstPageController.create();
            STAGE.setScene(new Scene(controller.getRoot()));
            STAGE.initModality(Modality.APPLICATION_MODAL);
            STAGE.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Stage getStage() {
        return STAGE;
    }
    public static void finish(String login, String password, String name, String email, String info)
    {
        try {
            RegistrationQuery.sendQuery(login, password, name, email, info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void cancel()
    {
        STAGE.close();
    }

    private static void onError(Integer code)
    {
        Platform.runLater(() -> controller.displayError("Ошибка. Код: "+code));
    }
    private static void onSuccess(Integer code)
    {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Регистрация успешно завершена!");
            alert.setContentText("Вы успешно зарегистрировались. Нажмите ОК и войдите в свою учетную запись, используя" +
                    "логин и пароль, который вы указали при регистрации!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                alert.close();
                STAGE.close();
            }
        });
    }

}
