package client.application;

public interface Listener<T> {
    void onHandle(T t);
}
