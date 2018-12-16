import com.alibaba.fastjson.JSON;
import common.objects.GroupInfo;
import common.objects.Message;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ReflectionTest {

    @Test
    public void doTest() {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setUsersCount(9);
        groupInfo.setLastMessage(new Message());
        groupInfo.setGroupName("123");
        groupInfo.setAvatarPath("/gf");
        groupInfo.setDialogId(12);
        System.out.println(JSON.toJSONString(groupInfo));
    }
}