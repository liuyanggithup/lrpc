import com.ly.lrpc.netty.client.ClientRequest;
import com.ly.lrpc.netty.client.Response;
import com.ly.lrpc.netty.client.TcpClient;
import org.junit.Test;

public class TestTcp {

    @Test
    public void testGetResponse(){
        ClientRequest request = new ClientRequest();
        request.setContent("ceshiyixia");
        Response send = TcpClient.send(request);
        System.out.println(send.getResult());
    }
}
