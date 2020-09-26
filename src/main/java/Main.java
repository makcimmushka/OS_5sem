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
                new SocketClient().startClient("2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(server).start();
        Thread.sleep(3000); /* Waiting for starting server */

        Thread threadFuncF = new Thread(client, "funcF");
        Thread threadFuncG = new Thread(client, "funcG");

        threadFuncF.start();
        threadFuncG.start();
        threadFuncF.join();
        threadFuncG.join();
        System.out.println();
    }
}


