package server.dto.requests;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateDialogData {
    private String partner;
    private String initialMessage;
    private String token;
}
