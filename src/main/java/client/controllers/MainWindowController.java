package client.controllers;

import client.app.main.AbstractWindow;
import client.app.main.menu.CreateChannel;
import client.app.main.menu.CreateGroup;
import client.app.main.menu.UserSearch;
import common.objects.Message;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;


public class MainWindowController {
    @FXML
    private VBox chooserBox;
    @FXML
    private Pane root;

    @FXML
    private AnchorPane dialogPane;
    @FXML
    private AnchorPane menu;

    private AnchorPane locker;
    private Timeline fadeAnimation, appearAnimation;

    private AbstractWindow window, replaced;
    private MenuWindow menuWindow;

    private static final double ANIMATION_TIME = 1/5d;

    @FXML
    protected void initialize()
    {
        locker = new AnchorPane();
        AnchorPane.setBottomAnchor(locker, 0d);
        AnchorPane.setTopAnchor(locker, 0d);
        AnchorPane.setLeftAnchor(locker, 0d);
        AnchorPane.setRightAnchor(locker, 0d);

        locker.setOpacity(0.0);
        locker.setStyle("-fx-background-color: black;");
        locker.setOnMouseClicked(e->{
            closeTopWindow();
            e.consume();
        });


        KeyFrame[] appear =  new KeyFrame[]{
                new KeyFrame(Duration.ZERO, new KeyValue(locker.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(ANIMATION_TIME), new KeyValue(locker.opacityProperty(), 0.5d))
        };
        appearAnimation = new Timeline(appear);
        KeyFrame[] fade =  new KeyFrame[]{
                new KeyFrame(Duration.ZERO, new KeyValue(locker.opacityProperty(), 0.5d)),
                new KeyFrame(Duration.seconds(ANIMATION_TIME), new KeyValue(locker.opacityProperty(), 0.0))
        };
        fadeAnimation = new Timeline(fade);
        fadeAnimation.setOnFinished(event -> root.getChildren().remove(locker));
        menuWindow = new MenuWindow(menu);
        root.getChildren().remove(menu);
    }
    @FXML
    private void showMenu()
    {
        displayWindow(menuWindow);
    }
    @FXML
    private void onEnter(MouseEvent e)
    {
        AnchorPane pane = (AnchorPane) e.getSource();
        pane.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        e.consume();
    }
    @FXML
    private void onExit(MouseEvent e)
    {
        AnchorPane pane = (AnchorPane) e.getSource();
        pane.setStyle("-fx-background-color: rgba(255,255,255,0);");
        e.consume();
    }
    public void replaceWindow(AbstractWindow newWindow) {
        if (window == null) {
            displayWindow(newWindow);
            return;
        }
        replaced = window;
        window = newWindow;
        root.getChildren().remove(replaced.getRoot());
        root.getChildren().add(window.getRoot());
        window.attach();
    }
    public void closeTopWindow()
    {
        if (replaced != null)
        {
            root.getChildren().remove(window.getRoot());
            root.getChildren().add(replaced.getRoot());
            window = replaced;
            replaced = null;
            return;
        }
        Timeline close = window.getOnClose();
        if (close == null) {
            root.getChildren().removeAll(locker, window.getRoot());
            window = null;
        }
        else {
            close.setOnFinished(e->
            {
                root.getChildren().remove(window.getRoot());
                window = null;
            });
            ParallelTransition t = new ParallelTransition(close, fadeAnimation);
            t.setCycleCount(1);
            t.play();
        }

    }
    @FXML
    private void showUserSearch()
    {
        try {
            displayWindow(new UserSearch());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void prepareNewGroup()
    {
        try {
            displayWindow(new CreateGroup());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void prepareNewChannel()
    {
        try {
            displayWindow(new CreateChannel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void displayWindow(AbstractWindow newWindow)
    {
        if (window != null)
        {
            Platform.runLater(()->root.getChildren().remove(window.getRoot()));
        }
            Pane p = newWindow.getRoot();

            if (window == null)
            {
                locker.setOpacity(0.5d);
                Platform.runLater(()->root.getChildren().add(locker));
            }
            Platform.runLater(()->root.getChildren().add(p));

            Timeline open = newWindow.getOnOpen();
            if (open == null) {
                newWindow.attach();
            }
            else
            {
                if (window == null)
                {
                    ParallelTransition transition = new ParallelTransition(open, appearAnimation);
                    transition.setCycleCount(1);
                    transition.play();
                }
            }
            window = newWindow;

    }


    public void addDialogInfo(DialogChooserController controller)
    {

        Platform.runLater(()-> {
            chooserBox.getChildren().add(1,controller.getPane());
            controller.setOnMessageReceived(this::onNewMessage);
        });
    }
    public void showDialog(Parent p)
    {
        dialogPane.getChildren().clear();
        dialogPane.getChildren().add(p);
    }

    private void onNewMessage(DialogChooserController c, Message m)
    {
        chooserBox.getChildren().remove(c.getPane());
        chooserBox.getChildren().add(1, c.getPane());
    }
    public static MainWindowController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
        loader.load();
        return loader.getController();
    }

    static class MenuWindow extends AbstractWindow
    {
        MenuWindow(AnchorPane root)
        {
            this.root = root;
            setOnOpenAnimation();
            setOnCloseAnimation();

        }
        private void setOnOpenAnimation()
        {
            double width = root.getPrefWidth();

            KeyFrame[] slide = new KeyFrame[]{
                    new KeyFrame(Duration.ZERO, new KeyValue(root.translateXProperty(), -width)),
                    new KeyFrame(Duration.seconds(ANIMATION_TIME), new KeyValue(root.translateXProperty(), 0))
            };
            this.onOpen = new Timeline(slide);
        }
        private void setOnCloseAnimation()
        {
            double width = root.getPrefWidth();

            KeyFrame[] slide = new KeyFrame[]{
                    new KeyFrame(Duration.seconds(ANIMATION_TIME), new KeyValue(root.translateXProperty(), -width)),
                    new KeyFrame(Duration.ZERO, new KeyValue(root.translateXProperty(), 0))
            };
            this.onClose = new Timeline(slide);
        }
    }
    public Pane getRoot()
    {
        return root;
    }

}
