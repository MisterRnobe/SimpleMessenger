package server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    private String status;
    private String message;
    private Object body;
}
