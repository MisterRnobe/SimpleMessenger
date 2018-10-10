package client.app.main;

import client.Supplier;
import client.app.main.menu.UserSearch;
import client.utils.DialogBean;
import client.app.main.dialog.AbstractDialogWrapper;
import client.app.main.menu.CreateChannel;
import client.app.main.menu.CreateGroup;
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


public class MainWindowController extends AbstractMainWindow {
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

    private AbstractWindow window, replaced;
    private MenuWindow menuWindow;


    private final Map<Integer, AbstractDialogWrapper> dialogControllerMap = new TreeMap<>();
    private AbstractDialogWrapper currentDialog = null;
    private static final double ANIMATION_TIME = 1/5d;

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
    public void replaceWindow(AbstractWindow w)
    {
        if (window == null)
            displayWindow(()->w);
        else
        {
            replaced = window;
            window = w;
            root.getChildren().remove(replaced.getRoot());
            root.getChildren().add(window.getRoot());
            window.attach();
        }
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
        displayWindow(()->new UserSearch(this));
    }
    @FXML
    private void prepareNewGroup()
    {
        displayWindow(CreateGroup::new);
    }
    @FXML
    private void prepareNewChannel()
    {
        displayWindow(CreateChannel::new);
    }
    public void displayWindow(Supplier<AbstractWindow> supplier)
    {
//        locker.setOnMouseClicked(e-> closeTopWindow());
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

    public AbstractDialogWrapper getCurrentDialog() {
        return currentDialog;
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
                AbstractDialogWrapper c = dialogControllerMap.get(dialogId);
                if (c == null) {
                    c = AbstractDialogWrapper.createOf(dialogId);
                    dialogControllerMap.put(dialogId, c);
                }
                showDialog(c.getRoot());
                currentDialog = c;
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

}
