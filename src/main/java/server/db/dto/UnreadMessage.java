package server.db.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unread_message")
public class UnreadMessage {
    @Id
    @Column(name = "message_id")
    private Integer messageId;
    private String login;
}
