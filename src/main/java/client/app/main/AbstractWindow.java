package client.app.main;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public abstract class AbstractWindow {
    @FXML
    protected Pane root;
    protected Timeline onOpen = null, onClose = null;
    protected final MainWindowManager mainWindowManager = MainWindowManager.getInstance();

    public Pane getRoot()
    {
        return root;
    }
    public void attach()
    {
        Pane pane = (Pane)root.getParent();
        root.translateXProperty().bind(pane.widthProperty().subtract(root.widthProperty()).divide(2));//.add(-root.getPrefWidth()).divide(2));
        root.translateYProperty().bind(pane.heightProperty().subtract(root.heightProperty()).divide(2));//.add(-root.getPrefHeight()).divide(2));
    }

    public Timeline getOnOpen() {
        return onOpen;
    }

    public Timeline getOnClose() {
        return onClose;
    }
    protected final void close()
    {
        mainWindowManager.closeWindow();
    }
}
