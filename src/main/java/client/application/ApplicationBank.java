package client.application;

import javafx.stage.Stage;

public class ApplicationBank {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ApplicationBank.stage = stage;
    }
}
