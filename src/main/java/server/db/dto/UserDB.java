package server.db.dto;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user")
@ToString
public class UserDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    private String login;
    @Column(name = "password", columnDefinition = "char")
    private String password;
    private String name;
    private String email;
    private String info;
    @Column(name = "has_avatar")
    private Boolean hasAvatar;
}
