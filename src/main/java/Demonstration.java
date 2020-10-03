import java.util.Scanner;

public class Demonstration {
    public void showVariants() {
        System.out.println("Possible variants: ");
        System.out.println("1. f finishes before g with non value");
        System.out.println("2. g finishes before f with non value");
        System.out.println("3. f finishes zero, g hangs");
        System.out.println("4. g finishes zero, f hangs");
        System.out.println("5. f finishes non zero value, f hangs");
        System.out.println("6. g finishes non zero value, f hangs");
    }

    public int inputVariant() {
        int variant;
        Scanner selector = new Scanner(System.in);

        variant = selector.nextInt();

        return variant;
    }
}
