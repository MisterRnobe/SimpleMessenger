package client.app.registration;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public abstract class PageController {
    @FXML
    private Pane root;

    public Pane getRoot() {
        return root;
    }
    public abstract void displayMessage(String message);
}
