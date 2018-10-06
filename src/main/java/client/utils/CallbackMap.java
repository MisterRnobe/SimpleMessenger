package client.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class CallbackMap<E> extends TreeMap<String, List<Consumer<E>>> {
    public void put(String key, Consumer<E> callback)
    {
        List<Consumer<E>> list = this.get(key);
        if (list == null)
        {
            list = new LinkedList<>();
            list.add(callback);
            this.put(key, list);
        }
        else
        {
            list.add(callback);
        }
    }
}
