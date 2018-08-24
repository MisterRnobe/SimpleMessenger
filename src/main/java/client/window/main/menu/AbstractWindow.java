package client.window.main.menu;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public abstract class AbstractWindow {
    @FXML
    protected Pane root;
    protected Timeline onOpen = null, onClose = null;

    public Pane getRoot()
    {
        return root;
    }
    public void attach()
    {
        Pane pane = (Pane)root.getParent();
        root.translateXProperty().bind(pane.widthProperty().add(-root.getPrefWidth()).divide(2));
        root.translateYProperty().bind(pane.heightProperty().add(-root.getPrefHeight()).divide(2));
    }

    public Timeline getOnOpen() {
        return onOpen;
    }

    public Timeline getOnClose() {
        return onClose;
    }
}
