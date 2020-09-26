import java.io.IOException;
import com.sockets.*;


public class Main {
    public static void main(String[] args) throws Exception {
        Runnable server = () -> {
            try {
                new SocketServer("localhost", 7777).startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable client = () -> {
            try {
                new SocketClient().startClient("7777");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(server).start();
        Thread.sleep(3000);
        new Thread(client, "client-funcF").start();
        new Thread(client, "client-funcG").start();
    }
}


