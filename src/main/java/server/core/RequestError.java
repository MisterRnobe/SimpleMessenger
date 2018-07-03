package server.core;

public class RequestError extends RuntimeException {
    private final int code;

    public RequestError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
