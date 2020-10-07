import com.constants.Constants;
import com.sockets.SocketClient;
import com.sockets.SocketServer;

import java.io.IOException;

public class MainManager {
    private final SocketServer socketServer = new SocketServer("localhost", 7777);
    private final SocketClient socketClient = new SocketClient();
    private final Demonstration demo = new Demonstration();
    private Integer variant = null;


    public void interactiveMenu() {
        while (true) {
            this.demo.showVariants();
            this.variant = this.demo.inputVariant();

            if (this.variant != null) {
                break;
            }

            System.out.println("Provided incorrect number of variant, try again!\n");
        }
    }

    public void start() throws InterruptedException {
        Runnable server = () -> {
            try {
                this.socketServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable client = () -> {
            try {
                this.socketClient.startClient(variant);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread threadMain = new Thread(server);

        Thread threadFuncF = new Thread(client, Constants.FUNC_F);
        Thread threadFuncG = new Thread(client, Constants.FUNC_G);

        threadMain.start();
        Thread.sleep(3000); /* Waiting for starting server */

        threadFuncF.start();
        threadFuncG.start();

        threadFuncF.join();
        threadFuncG.join();
        threadMain.join();

        System.out.println("Result of computations is: " + this.socketServer.getMultiplication());
    }
}
