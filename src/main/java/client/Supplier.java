package client;

@FunctionalInterface
public interface Supplier<T>
{
    T get() throws Throwable;
}
