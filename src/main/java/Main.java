import java.io.IOException;
import com.sockets.*;


public class Main {
    public static void main(String[] args) throws Exception {
        SocketServer socketServer = new SocketServer("localhost", 7777);
        SocketClient socketClient = new SocketClient();

        Demonstration demo = new Demonstration();

        demo.showVariants();
        int variant = demo.inputVariant(); /* try-catch TODO ... */

        Runnable server = () -> {
            try {
                socketServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable client = () -> {
            try {
                socketClient.startClient(variant);
            } catch (IOException | InterruptedException e) {
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

        System.out.println("Multiplication is: " + socketServer.getMultiplication());
    }
}


