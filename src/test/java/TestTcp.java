import com.ly.lrpc.netty.client.ClientRequest;
import com.ly.lrpc.netty.client.Response;
import com.ly.lrpc.netty.client.TcpClient;
import com.ly.user.bean.User;
import com.ly.user.controller.UserController;
import org.junit.Test;

public class TestTcp {

    @Test
    public void testGetResponse(){
        ClientRequest request = new ClientRequest();
        request.setContent("ceshiyixia");
        Response send = TcpClient.send(request);
        System.out.println(send.getResult());
    }

    @Test
    public void test2(){
        ClientRequest request = new ClientRequest();
        User user = new User();
        user.setId(1);
        user.setName("zhangsan");

        request.setCommand("com.ly.user.controller.UserController.saveUser");
        request.setContent(user);
        Response send = TcpClient.send(request);
        System.out.println(send.getResult());
    }


}
