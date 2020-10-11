import spos.lab1.demo.IntOps;

public class Main {
    public static void main(String[] args) throws Exception {
        MainManager mainManager = new MainManager();

        while (true) {
            mainManager.interactiveMenu();
            mainManager.start();
        }

//        System.out.println(IntOps.funcF(1));
//        System.out.println(IntOps.funcF(2));
//        System.out.println(IntOps.funcF(3));
//        System.out.println(IntOps.funcF(4));
//        System.out.println(IntOps.funcF(5));
//        System.out.println(IntOps.funcF(6));

    }
}


