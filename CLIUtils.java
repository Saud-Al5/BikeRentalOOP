import java.time.LocalDate;
import java.util.Scanner;

public final class CLIUtils {
    private static final Scanner sc = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException nfe) {
                System.out.println("  ! enter a valid number");
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException nfe) {
                System.out.println("  ! enter a valid decimal number");
            }
        }
    }

    public static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = sc.nextLine().trim();
            if (!s.isEmpty())
                return s;
            System.out.println("  ! value cannot be empty");
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD | today | tomorrow): ");
            String in = sc.nextLine().trim().toLowerCase();

            if ("today".equals(in))
                return LocalDate.now();
            if ("tomorrow".equals(in))
                return LocalDate.now().plusDays(1);

            in = in.replace('/', '-');
            String[] p = in.split("-");
            if (p.length == 3) {
                try {
                    int y = Integer.parseInt(p[0]);
                    int m = Integer.parseInt(p[1]);
                    int d = Integer.parseInt(p[2]);
                    return LocalDate.of(y, m, d);
                } catch (Exception ignore) {
                }
            }
            System.out.println("  ! invalid date – try 2025-04-23, today, or tomorrow");
        }
    }

    public static void pressEnter() {
        System.out.println("\nPress ENTER to continue…");
        sc.nextLine();
    }

    // Read a full line (may be empty).
    public static String readLine(String prompt) {
        System.out.print(prompt + ": ");
        return sc.nextLine().trim();
    }

    // Read a yes/no answer
    public static boolean readBoolean(String prompt, boolean current) {
        while (true) {
            System.out.print(prompt + " [" + (current ? "yes" : "no") + "] (yes/no): ");
            String in = sc.nextLine().trim().toLowerCase();
            if (in.isEmpty())
                return current;
            if (in.equals("yes") || in.equals("y") || in.equals("true"))
                return true;
            if (in.equals("no") || in.equals("n") || in.equals("false"))
                return false;
            System.out.println("  ! enter yes or no");
        }
    }

    private CLIUtils() {
    }
}
