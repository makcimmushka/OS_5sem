import com.sockets.SocketServer;

public class Main {
    public static void main(String[] args) throws Exception {
        MainManager mainManager = new MainManager();

        while (true) {
            mainManager.interactiveMenu();
            mainManager.start();
        }

//        new SocketServer("localhost", 7777).startServer();
    }
}


