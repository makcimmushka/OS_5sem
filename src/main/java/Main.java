import java.io.IOException;

import com.constants.Constants;
import com.sockets.*;


/* Move this logic to MainManager class */
public class Main {
    public static void main(String[] args) throws Exception {
        MainManager mainManager = new MainManager();
        mainManager.interactiveMenu();
        mainManager.start();
    }
}


