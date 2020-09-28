import java.io.IOException;
import com.sockets.*;


public class Main {
    public static void main(String[] args) throws Exception {
        SocketServer socketServer = new SocketServer("localhost", 7777);
        SocketClient socketClient = new SocketClient();

        Runnable server = () -> {
            try {
                socketServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable client = () -> {
            try {
                socketClient.startClient("2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Thread threadMain = new Thread(server);

        Thread threadFuncF = new Thread(client, "funcF");
        Thread threadFuncG = new Thread(client, "funcG");

        threadMain.start();
        Thread.sleep(3000); /* Waiting for starting server */

        threadFuncF.start();
        threadFuncG.start();

        threadFuncF.join();
        threadFuncG.join();
        threadMain.join();

        System.out.println("Result is: " + socketServer.getResult());
    }
}


