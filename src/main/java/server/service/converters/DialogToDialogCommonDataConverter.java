package server.service.converters;

import org.springframework.stereotype.Component;
import server.db.dto.DialogDB;
import server.db.dto.UserDB;
import server.dto.responses.DialogCommonData;
import server.dto.responses.DialogData;
import server.dto.responses.GroupData;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;

@Component
public class DialogToDialogCommonDataConverter {
    private Map<Integer, BiFunction<DialogDB, String, DialogCommonData>> concreteConverters =
            new TreeMap<>();

    {
        concreteConverters.put(0, (d, login) -> {
            DialogData dialogData = new DialogData();
            dialogData.setType(d.getType());
            dialogData.setDialogId(d.getDialogId());

            UserDB partner = d.getUserDBS().stream()
                    .filter(user -> !user.getLogin().equals(login))
                    .findFirst()
                    .get();
            dialogData.setPartner(partner);
            return dialogData;
        });
        BiFunction<DialogDB, String, DialogCommonData> dialogToGroup = (d, login) -> {
            GroupData groupData = new GroupData();
            groupData.setDialogId(d.getDialogId());
            groupData.setType(d.getType());
            groupData.setCreator(d.getCreator());
            groupData.setGroupName(d.getDialogName());
            groupData.setUsersCount(d.getUserDBS().size());
            return groupData;
        };
        concreteConverters.put(1, dialogToGroup);
        concreteConverters.put(2, dialogToGroup);
    }

    public DialogCommonData convert(DialogDB dialog, String forLogin) {
        return concreteConverters.get(dialog.getType()).apply(dialog, forLogin);
    }
}
