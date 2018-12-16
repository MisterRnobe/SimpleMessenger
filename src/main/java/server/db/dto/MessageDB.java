package server.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class MessageDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;
    @Column(name = "dialog_id")
    private Integer dialogId;
    private String sender;
    private String text;
    private Long time;
    @Column(name = "is_system")
    private Boolean isSystem;
}
