import com.alibaba.fastjson.JSONObject;
import com.ly.lrpc.netty.annotation.RemoteInvoke;
import com.ly.lrpc.netty.client.Response;
import com.ly.user.bean.User;
import com.ly.user.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokeTest.class)
@ComponentScan("com.ly")
public class RemoteInvokeTest {




    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void test2(){

        User user = new User();
        user.setId(1);
        user.setName("zhangsan");
        Response response = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(response));
    }

}
