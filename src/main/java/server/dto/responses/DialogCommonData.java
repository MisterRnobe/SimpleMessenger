package server.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.db.dto.MessageDB;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogCommonData {
    protected Integer dialogId;
    protected Integer type;
    protected MessageDB lastMessage;
    protected Integer unread;

}
