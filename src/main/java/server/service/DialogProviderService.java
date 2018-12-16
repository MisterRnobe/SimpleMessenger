package server.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.db.dto.DialogDB;
import server.db.repositories.DialogRepository;
import server.db.repositories.MessageRepository;
import server.db.repositories.UnreadMessagesRepository;
import server.dto.responses.DialogCommonData;
import server.service.converters.DialogToDialogCommonDataConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DialogProviderService {
    private DialogRepository dialogRepository;
    private MessageRepository messageRepository;
    private DialogToDialogCommonDataConverter converter;
    private UnreadMessagesRepository unreadMessagesRepository;


    public List<DialogCommonData> findDialogsForUser(String login, Integer count, Integer offset){
        List<DialogDB> dialogs = dialogRepository.findAllByUserDBS_Login(login);
        List<DialogCommonData> dialogDataList = dialogs.stream()
                .map(d-> converter.convert(d, login))
                .peek(d->d.setLastMessage(messageRepository.findTopByDialogIdOrderByTimeDesc(d.getDialogId())))
                .collect(Collectors.toList());
        return dialogDataList;
    }
    private Integer loadUnreadMessagesCount(Integer dialogId, String forLogin){
        return null;
    }

}
