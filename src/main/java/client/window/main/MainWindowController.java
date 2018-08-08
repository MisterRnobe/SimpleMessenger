package client.window.main;

import client.application.DialogBean;
import client.window.main.dialog.controllers.AbstractWrapper;
import client.window.main.menu.AbstractMenuWindow;
import client.window.main.menu.newdialog.UserSearchController;
import client.window.main.menu.newgroup.UserSelectingListController;
import common.objects.Message;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MainWindowController {
    @FXML
    private VBox chooserBox;
    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane dialogPane;
    @FXML
    private AnchorPane menu;

    @FXML
    private AnchorPane createChannelButton;

    private AnchorPane locker;
    private ParallelTransition openAnimation, closeAnimation;
    private AbstractMenuWindow menuWindow;


    private final Map<Integer, AbstractWrapper> dialogControllerMap = new TreeMap<>();
    //private final List<DialogChooserController> chooserControllerList = new LinkedList<>();

    @FXML
    private void initialize()
    {
        double time = 1/5d;
        locker = new AnchorPane();

        AnchorPane.setBottomAnchor(locker, 0d);
        AnchorPane.setTopAnchor(locker, 0d);
        AnchorPane.setLeftAnchor(locker, 0d);
        AnchorPane.setRightAnchor(locker, 0d);

        locker.setOpacity(0.0);
        locker.setStyle("-fx-background-color: black;");
        locker.setOnMouseClicked(this::closeMenu);

        menu.setOnMouseClicked(Event::consume);

        double width = menu.getPrefWidth();
        //menu.setLayoutX(-width);

        KeyFrame[] slide1 = new KeyFrame[]{
                new KeyFrame(Duration.ZERO, new KeyValue(menu.translateXProperty(), -width)),
                new KeyFrame(Duration.seconds(time), new KeyValue(menu.translateXProperty(), 0))
        };
        KeyFrame[] fade1 =  new KeyFrame[]{
                new KeyFrame(Duration.ZERO, new KeyValue(locker.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(time), new KeyValue(locker.opacityProperty(), 0.5d))
        };
        openAnimation = new ParallelTransition(new Timeline(slide1), new Timeline(fade1));
        openAnimation.setCycleCount(1);


        KeyFrame[] slide2 = new KeyFrame[]{
                new KeyFrame(Duration.seconds(time), new KeyValue(menu.translateXProperty(), -width)),
                new KeyFrame(Duration.ZERO, new KeyValue(menu.translateXProperty(), 0))
        };
        KeyFrame[] fade2 =  new KeyFrame[]{
                new KeyFrame(Duration.ZERO, new KeyValue(locker.opacityProperty(), 0.5d)),
                new KeyFrame(Duration.seconds(time), new KeyValue(locker.opacityProperty(), 0.0))
        };
        closeAnimation = new ParallelTransition(new Timeline(slide2), new Timeline(fade2));
        closeAnimation.setCycleCount(1);
        closeAnimation.setOnFinished(event -> root.getChildren().remove(locker));
    }
    @FXML
    private void showMenu()
    {
        locker.setOnMouseClicked(this::closeMenu);
        root.getChildren().add(root.getChildren().indexOf(menu), locker);
        openAnimation.play();
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
    private void closeMenu(MouseEvent event)
    {
        closeAnimation.play();
        event.consume();
    }
    public void closeWindow()
    {
        root.getChildren().removeAll(locker, menuWindow.getRoot());
        menuWindow = null;
    }
    @FXML
    private void showUserSearch()
    {
        displayWindow(UserSearchController::create);
    }
    @FXML
    private void prepareNewGroup()
    {
        displayWindow(UserSelectingListController::create);
    }
    private void displayWindow(Supplier<AbstractMenuWindow> supplier)
    {
        locker.setOnMouseClicked(e-> closeWindow());
        menu.setTranslateX(-menu.getPrefWidth());
        try {
            menuWindow = supplier.get();
            Pane p = menuWindow.getRoot();
            root.getChildren().add(p);
            double width = p.getPrefWidth(), height = p.getPrefHeight();
            p.translateXProperty().bind(root.widthProperty().add(-width).divide(2));
            p.translateYProperty().bind(root.heightProperty().add(-height).divide(2));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void addDialogInfo(DialogBean dialogInfo)
    {
        Platform.runLater(()-> {
            try {
                DialogChooserController controller = DialogChooserController.create(dialogInfo);
                chooserBox.getChildren().add(1,controller.getPane());
                controller.setOnMessageReceived(this::onNewMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    void showDialog(Parent p)
    {
        dialogPane.getChildren().clear();
        dialogPane.getChildren().add(p);
    }

    public void showDialog(int dialogId)
    {
        Platform.runLater(()-> {
            try {
                AbstractWrapper c = dialogControllerMap.get(dialogId);
                if (c == null) {
                    c = AbstractWrapper.createOf(dialogId);
                    dialogControllerMap.put(dialogId, c);
                }
                showDialog(c.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void onNewMessage(DialogChooserController c, Message m)
    {
        chooserBox.getChildren().remove(c.getPane());
        chooserBox.getChildren().add(1, c.getPane());
    }

    public Node getRoot()
    {
        return root;
    }
    public static MainWindowController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
        loader.load();
        return loader.getController();
    }
    @FunctionalInterface
    private interface Supplier<T>
    {
        T get() throws Throwable;
    }

}
