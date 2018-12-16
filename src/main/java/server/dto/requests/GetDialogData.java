package server.dto.requests;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GetDialogData {
    private Integer count;
    private String token;
}
