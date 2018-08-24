package client.window.main;

import client.Supplier;
import client.application.DialogBean;
import client.window.main.dialog.controllers.AbstractWrapper;
import client.window.main.menu.AbstractWindow;
import client.window.main.menu.newchannel.ChannelWindowController;
import client.window.main.menu.newdialog.UserSearchController;
import client.window.main.menu.newgroup.UserSelectingListController;
import common.objects.Message;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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

    private AnchorPane locker;
    private Timeline fadeAnimation, appearAnimation;

    private AbstractWindow window;
    private MenuWindow menuWindow;


    private final Map<Integer, AbstractWrapper> dialogControllerMap = new TreeMap<>();
    public static final double ANIMATION_TIME = 1/5d;

    @FXML
    private void initialize()
    {
        locker = new AnchorPane();

        AnchorPane.setBottomAnchor(locker, 0d);
        AnchorPane.setTopAnchor(locker, 0d);
        AnchorPane.setLeftAnchor(locker, 0d);
        AnchorPane.setRightAnchor(locker, 0d);

        locker.setOpacity(0.0);
        locker.setStyle("-fx-background-color: black;");
        locker.setOnMouseClicked(e->{
            closeWindow();
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
        displayWindow(()->menuWindow);
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
    public void closeWindow()
    {
        Timeline close = window.getOnClose();
        if (close == null) {
            root.getChildren().removeAll(locker, window.getRoot());
        }
        else {
            close.setOnFinished(e->root.getChildren().remove((Node)e.getSource()));
            ParallelTransition t = new ParallelTransition(close, fadeAnimation);
            t.setCycleCount(1);
            t.play();
        }
        window = null;
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
    @FXML
    private void prepareNewChannel()
    {
        displayWindow(ChannelWindowController::create);
    }
    public void displayWindow(Supplier<AbstractWindow> supplier)
    {
//        locker.setOnMouseClicked(e-> closeWindow());
//        menu.setTranslateX(-menu.getPrefWidth());
        if (window != null)
        {
            root.getChildren().remove(window.getRoot());
        }
        try {
            AbstractWindow prepared = supplier.get();
            Pane p = prepared.getRoot();

            if (window == null)
            {
                locker.setOpacity(0.5d);
                root.getChildren().add(locker);
            }
            root.getChildren().add(p);

            Timeline open = prepared.getOnOpen();
            if (open == null) {
                prepared.attach();
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
            window = prepared;
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
    static class MenuWindow extends AbstractWindow
    {
        public MenuWindow(AnchorPane root)
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

}
