import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Bike Rental System ===");

        while (true) {
            System.out.print("\nEnter your name (blank to quit): ");
            String name = sc.nextLine().trim();
            if (name.isEmpty())
                break;

            System.out.print("Login as [renter/admin]: ");
            String role = sc.nextLine().trim().toLowerCase();

            if ("admin".equals(role))
                new AdminMenu(name).start();
            else if ("renter".equals(role))
                new RenterMenu(name).start();
            else
                System.out.println("Unknown role");
        }
        System.out.println("Bye!");
        sc.close();
    }
}
