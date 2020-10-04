import java.util.Scanner;

public class Demonstration {
    public void showVariants() {
        System.out.println("Possible variants: ");
        System.out.println("1. f finishes before g with non value");
        System.out.println("2. g finishes before f with non value");
        System.out.println("3. f finishes with zero, g hangs");
        System.out.println("4. g finishes with zero, f hangs");
        System.out.println("5. f finishes with non zero value, f hangs");
        System.out.println("6. g finishes with non zero value, f hangs");
    }

    public int inputVariant() {
        int variant;
        Scanner selector = new Scanner(System.in);

        variant = selector.nextInt();

        return variant;
    }
}
