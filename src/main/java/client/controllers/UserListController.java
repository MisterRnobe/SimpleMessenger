package client.controllers;

import client.app.main.AbstractWindow;
import client.network.queries.FindUsersQuery;
import common.objects.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserListController extends AbstractWindow {
    @FXML
    private TextField textField;
    @FXML
    private VBox userBox;

    private ChangeListener<? super String> textListener;
    private List<UserElementController> elementControllerList = new LinkedList<>();
    private List<String> selected = new LinkedList<>();
    private Consumer<User> onClick = u->{};
    private boolean selectable = false;

    public final void search(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {
        if (newValue.length() != 0)
        {
            try{
                FindUsersQuery.sendQuery(newValue, ul->this.setUserList(ul.getUsers()));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public final void filter(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {
        userBox.getChildren().clear();
        Predicate<UserElementController> predicate = newValue.length() == 0? c->true: u-> u.getBindUser().getName().contains(newValue) || u.getBindUser().getLogin().contains(newValue);
        List<AnchorPane> filtered = elementControllerList.stream().filter(predicate).sorted(Comparator.comparing(u->u.getBindUser().getName())).map(UserElementController::getRoot).collect(Collectors.toList());
        Platform.runLater(()-> userBox.getChildren().addAll(filtered));
    }
    @FXML
    private void initialize()
    {
        textListener = ((observable, oldValue, newValue) -> {});
    }

    public UserListController setTextListener(ChangeListener<? super String> textListener) {
        textField.textProperty().removeListener(this.textListener);
        this.textListener = textListener;
        textField.textProperty().addListener(textListener);
        return this;
    }

    public void setUserList(List<User> userList)
    {
        this.elementControllerList = userList.stream().sorted(Comparator.comparing(User::getName)).map(u->
        {
            UserElementController c = null;
            try {
                c = UserElementController.create(u);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return c;
        }).filter(Objects::nonNull).peek(uc->{
            if (this.selectable)
                uc.setSelectable(this::onSelect, this::onDeselect);
            else
                uc.setOnClick(onClick);
        }).collect(Collectors.toList());

        Platform.runLater(()->{
            userBox.getChildren().clear();
            userBox.getChildren().addAll(elementControllerList.stream().map(UserElementController::getRoot).collect(Collectors.toList()));
        });
    }
    public UserListController setSize(double width, double height)
    {
        root.setPrefSize(width, height);
        return this;
    }
    public UserListController setSelectable(boolean selectable)
    {
        if (selectable)
            elementControllerList.forEach(e->e.setSelectable(this::onSelect, this::onDeselect));
        else
            elementControllerList.forEach(UserElementController::setUnselectable);
        this.selectable = selectable;
        return this;
    }

    public List<String> getSelected()
    {
        return new ArrayList<>(selected);
    }

    private void onSelect(String login)
    {
        selected.add(login);
    }
    private void onDeselect(String login)
    {
        selected.remove(login);
    }
    public void setOnClickElement(Consumer<User> onClick)
    {
        this.onClick = onClick;
        elementControllerList.forEach(e->e.setOnClick(onClick));
    }



    public static UserListController create() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(UserListController.class.getResource("UserList.fxml"));
        loader.load();
        return loader.getController();
    }

}
