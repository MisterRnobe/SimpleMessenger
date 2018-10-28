package server.database;

import common.objects.*;
import common.objects.requests.*;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseExtractor {
    boolean verifyUser(UserPasswordData userPasswordData) throws SQLException;
    void addUser(RegistrationData registrationData) throws SQLException;
    int createGroup(String creator, String title, List<String> partners) throws SQLException;
    int createChannel(String creator, String title, List<String> partners) throws SQLException;
    int createDialog(String creator, String partner) throws SQLException;
    List<User> getUsersInDialog(Integer dialogId) throws SQLException;
    List<User> findUsers(String mask) throws SQLException;
    List<DialogMarker> getDialogs(String login, Integer count) throws SQLException;
    UserProfile getUserProfile(String login) throws SQLException;
    User getUserStatus(String login) throws SQLException;
    List<Message> getMessagesByIds(List<Integer> ids) throws SQLException;
    User getUser(String login) throws SQLException;
    void readMessages(Integer dialogId, String login) throws SQLException;
    void addUsersToDialog(Integer dialogId, List<String> users) throws SQLException;
    Dialog getDialogById(Integer dialogId) throws SQLException;
    void setOnline(String login) throws SQLException;
    void setOffline(String login) throws SQLException;
    FullDialog getFullDialog(Integer dialogId, String login) throws SQLException;
    int addMessage(Integer dialogId, String sender, String text, Long time) throws SQLException;
    void setLastMessage(Integer dialogId, Integer messageId) throws SQLException;
}
