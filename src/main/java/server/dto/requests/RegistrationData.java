package server.dto.requests;

import lombok.Data;

@Data
public class RegistrationData {
    private String login;
    private String password;
    private String name;
    private String email;

    private String info;

    private byte[] avatar;
}
