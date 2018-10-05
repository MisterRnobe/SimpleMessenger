package client.app.registration;

import client.network.queries.RegistrationQuery;
import client.network.queries.VerifyDataRequest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RegistrationController {
    private static PageController controller;
    private static Stage STAGE;
    private static String login, password, name, email, info;
    public static void startRegistration()
    {
        STAGE = new Stage();
        RegistrationQuery.setErrorListener(RegistrationController::onError);
        RegistrationQuery.setSuccessListener(code -> onSuccess());
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
    static void verify(String login, String password, String name, String email, String info)
    {
        try {
            VerifyDataRequest.send(login, password, name, email, info, code->{
                if (code == 0)
                    next();
                else
                    onError(code);
            });
            RegistrationController.login = login;
            RegistrationController.password = password;
            RegistrationController.name = name;
            RegistrationController.email = email;
            RegistrationController.info = info;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void finish(byte[] avatar)
    {
        try {
            RegistrationQuery.sendQuery(login, password, name, email, info, avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void next()
    {
        Platform.runLater(()->{
            try {
                controller = SecondPageController.create();
                STAGE.setScene(new Scene(controller.getRoot()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void cancel()
    {
        STAGE.close();
    }

    private static void onError(Integer code)
    {
        Platform.runLater(() -> controller.displayMessage("Ошибка. Код: "+code));
    }
    private static void onSuccess()
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
