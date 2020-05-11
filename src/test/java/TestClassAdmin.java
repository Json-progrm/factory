import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.getheart.MySpringBootApplication;
import com.getheart.mapper.UserMapper;
import com.getheart.pojo.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Json
 * @date 2020-29-8:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class TestClassAdmin {

    @Resource
    UserMapper userMapper;

    @Test
    public void md5() {
        String s = "123456";
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println("uuid:"+uuid);
        Md5Hash hash = new Md5Hash(s,uuid,2);
        System.out.println("加盐+散列2次后:"+hash.toString());
    }
}
