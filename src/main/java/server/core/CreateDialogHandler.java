package server.core;

import common.objects.*;
import common.objects.requests.CreateDialogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import server.db.dto.DialogDB;
import server.db.dto.MessageDB;
import server.db.dto.UserDB;
import server.db.repositories.DialogRepository;
import server.db.repositories.MessageRepository;
import server.db.repositories.UserRepository;

import java.sql.SQLException;
import java.util.*;

@Component
public class CreateDialogHandler extends AbstractHandler<CreateDialogRequest> {
    private User lastPartner;
    private Message lastMessage;
    private DialogRepository dialogRepository;
    private UserRepository userRepository;
    private MessageRepository messageRepository;

    @Autowired
    public CreateDialogHandler(DialogRepository dialogRepository, UserRepository userRepository, MessageRepository messageRepository) {
        super(CreateDialogRequest.class, new String[]{"partner", "initialMessage"}, null);
        this.dialogRepository = dialogRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    protected Body onHandle(CreateDialogRequest body) throws HandleError, SQLException {

        Iterable<UserDB> iterable = userRepository.findAllByLoginIn(Arrays.asList(login, body.getPartner()));
        Set<UserDB> users = new HashSet<>();
        iterable.forEach(users::add);
        DialogDB dialog = DialogDB.builder()
                .creator(login)
                .type(0)
                .userDBS(users)
                .hasPhoto(false)
                .build();
        DialogDB savedDialog = dialogRepository.save(dialog);

        MessageDB initialMessage = MessageDB.builder()
                .sender(login)
                .text(body.getInitialMessage())
                .time(System.currentTimeMillis())
                .isSystem(false)
                .dialogId(savedDialog.getDialogId())
                .build();
        initialMessage = messageRepository.save(initialMessage);
        messageRepository.save(initialMessage);

        FullDialog fullDialog = new FullDialog();
        Dialog d = new Dialog();
        d.setDialogId(savedDialog.getDialogId());
        fullDialog.setDialog(d);
        return fullDialog;



//        DatabaseExtractor extractor = DatabaseManager.getExtractor();
//        int dialogId = extractor.createDialog(login, body.getPartner());
//        if (dialogId == -1) {
//            throw new HandleError(Errors.INTERNAL_ERROR);
//        }
//        int messageId = extractor.addMessage(dialogId, login, body.getInitialMessage(), System.currentTimeMillis());
//        extractor.setLastMessage(dialogId, messageId);
//        FullDialog fullDialog = extractor.getFullDialog(dialogId, login);
//        List<MessageDB> messageList = fullDialog.getDialog().getMessages();
//        lastMessage = messageList.get(messageList.size() - 1);
//        lastPartner = ((DialogInfo) fullDialog.getInfo()).getPartner();
//
//        return fullDialog;

    }

    @Override
    protected void beforeSend() {
        AbstractHandler.sendAllMessage(lastMessage, Arrays.asList(lastPartner));

    }
}
