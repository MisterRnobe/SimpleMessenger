package server.core;

public class HandleError extends RuntimeException {
    private final int code;

    public HandleError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
