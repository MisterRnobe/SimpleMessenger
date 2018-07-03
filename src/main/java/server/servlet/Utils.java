package server.servlet;

import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static Map<String, String> getFirst(Map<String, String[]> map)
    {
        return map
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v-> v.getValue()[0]));
    }
}
