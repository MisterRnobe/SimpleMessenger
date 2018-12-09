package client.app.main;

import client.app.main.dialog.AbstractDialogWrapper;
import client.app.main.dialog.NewDialogWrapper;
import client.controllers.DialogChooserController;
import client.controllers.MainWindowController;
import client.network.queries.GetDialogQuery;
import client.network.queries.GetDialogsQuery;
import client.suppliers.AbstractDialogBean;
import client.suppliers.DialogManager;
import client.utils.Supplier;
import common.objects.User;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class MainWindowManager {
    private static MainWindowManager instance;
    private static MainWindowController mainWindow;
    public static MainWindowManager getInstance() {
        return instance;
    }

    private final Map<Integer, AbstractDialogWrapper> dialogControllerMap = new TreeMap<>();
    private AbstractDialogWrapper currentDialog = null;

    private MainWindowManager(Stage stage) throws IOException{
        stage.setTitle("Simple messenger");
        initializeListeners();
        stage.setResizable(true);
        mainWindow = MainWindowController.create();
        Scene scene = new Scene(mainWindow.getRoot());
        stage.setScene(scene);
        stage.show();
        try {
            GetDialogsQuery.sendQuery(50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(Stage stage) throws IOException{
        instance = new MainWindowManager(stage);
    }

    public <E extends AbstractWindow> E replaceWindow(Supplier<E> windowSupplier)
    {
        E window = null;
        try {
            window = windowSupplier.get();
            mainWindow.replaceWindow(window);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return window;
    }
    public void closeWindow()
    {
        mainWindow.closeTopWindow();
    }
    public <E extends AbstractWindow> E displayWindow(Supplier<E> windowSupplier)
    {
        E window = null;
        try {
            window = windowSupplier.get();
            mainWindow.displayWindow(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return window;
    }
    public void createEmptyDialog(User u)
    {
        try {
            NewDialogWrapper wrapper = new NewDialogWrapper(u);
            mainWindow.showDialog(wrapper.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public int getOpenedDialogId()
    {
        return currentDialog == null? -1:currentDialog.getDialogId();
    }
    public void setDialog(int dialogId)
    {
        AbstractDialogBean bean = DialogManager.getInstance().getDialogById(dialogId);
        if (bean.messages().size() == 0) {
            try {
                GetDialogQuery.sendQuery(dialogId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                AbstractDialogWrapper wrapper = dialogControllerMap.getOrDefault(dialogId, AbstractDialogWrapper.createOf(bean));
                this.dialogControllerMap.put(bean.dialogId, wrapper);
                Platform.runLater(()->mainWindow.showDialog(wrapper.getRoot()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void initializeListeners()
    {
        DialogManager.getInstance().addDialogListener(change -> {
            if (change.wasAdded())
            {
                AbstractDialogBean dialogBean = change.getValueAdded();
                try {
                    DialogChooserController chooserController = DialogChooserController.create(dialogBean);
                    mainWindow.addDialogInfo(chooserController);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

}
