package common.objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class Body {
    public static final Body NULL_BODY = new Body() {
        @Override
        public JSONObject toJSONObject() {
            return new JSONObject();
        }
    };
    public abstract JSONObject toJSONObject();
}
