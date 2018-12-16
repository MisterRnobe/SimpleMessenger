package server.dto.requests;

import lombok.Data;

@Data
public class UserPasswordData {
    private String login;
    private String password;
}
