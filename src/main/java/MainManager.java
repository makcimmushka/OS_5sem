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
                this.socketServer.setVariant(this.variant);
                break;
            }

            System.out.println("Provided incorrect number of variant, try again!\n");
        }
    }

    public void start() throws InterruptedException, IOException {
        this.socketServer.startServer();
        System.out.println("Result of computations is: " + this.socketServer.getMultiplication());
    }
}
