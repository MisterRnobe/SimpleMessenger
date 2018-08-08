package common.objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class Body {
    public static final Body NULL_BODY = new Body() {
        @Override
        public JSONObject toJSONObject() {
            return new JSONObject();
        }
    };
    public JSONObject toJSONObject()
    {
        Method[] methods = getClass().getDeclaredMethods();
        Map<String, Method> map = Arrays.stream(methods).filter(m->m.getName().contains("get"))
                .collect(Collectors.toMap(s -> {
                    String str = s.getName().replace("get","");
                    return str.substring(0,1).toLowerCase() + str.substring(1);
                }, method -> method));
        JSONObject o = new JSONObject();
        map.forEach((k,v)->{
            try {
                o.put(k, v.invoke(this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return o;
    }
}
