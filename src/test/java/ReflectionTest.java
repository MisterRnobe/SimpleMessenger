import com.alibaba.fastjson.JSON;
import common.objects.User;
import org.junit.Test;

public class ReflectionTest {

    @Test
    public void doTest() {
        User user = new User();
        user.setIsOnline(false);
        user.setLastOnline(123L);
        System.out.println(JSON.toJSONString(user.toJSONObject()));
    }
}