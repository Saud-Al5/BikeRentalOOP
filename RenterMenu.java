import java.util.List;
import java.time.LocalDate;

public final class RenterMenu {
    private final String renterName;

    public RenterMenu(String renterName) {
        this.renterName = renterName;
    }

    public void start() {
        while (true) {
            BookingService.get().cleanupExpired();

            CLIUtils.clearScreen();
            System.out.println("");
            System.out.println("=== Renter Menu (" + renterName + ") ===");
            System.out.println(" 1. Browse available bikes");
            System.out.println(" 2. Search bikes");
            System.out.println(" 3. Book a bike");
            System.out.println(" 4. My bookings");
            System.out.println(" 5. Cancel a booking");
            System.out.println(" 0. Logout");
            int c = CLIUtils.readInt("Choose");
            switch (c) {
                case 1:
                    listBikes(BikeService.get().all());
                    break;
                case 2:
                    String term = CLIUtils.readNonEmpty("Search term");
                    listBikes(BikeService.get().search(term));
                    break;
                case 3:
                    bookBike();
                    break;
                case 4:
                    listBookings(BookingService.get().byRenter(renterName));
                    break;
                case 5:
                    cancelBooking();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void listBikes(List<Bike> bikes) {
        System.out.println("\nID | Make       | Model        | Type      | Price per day | Available");
        System.out.println("---------------------------------------------------------------");
        for (Bike b : bikes) {
            if (b.isAvailable())
                System.out.println(b);
        }
        CLIUtils.pressEnter();
    }

    private void listBookings(List<Booking> list) {
        System.out.println("\nYour bookings:");
        System.out.println("ID  | Bike                     | Dates         | Status");
        System.out.println("--------------------------------------------------------");
        for (Booking b : list) {
            Bike bk = b.getBike();
            String desc = String.format("(#%d) %s %s",
                    bk.getId(), bk.getMake(), bk.getModel());
            System.out.println(String.format("%-3d | %-25s | %s → %s | %s",
                    b.getId(), desc, b.getStart(), b.getEnd(), b.getStatus()));
        }
        CLIUtils.pressEnter();
    }

    private void bookBike() {
        // 1) show available bikes
        System.out.println("\nAvailable Bikes:");
        System.out.println("ID | Make       | Model        | Type      | Price    | Available");
        System.out.println("---------------------------------------------------------------");
        for (Bike b : BikeService.get().all()) {
            if (b.isAvailable())
                System.out.println(b);
        }

        // 2) prompt for bike ID
        int id = CLIUtils.readInt("Enter bike ID to book");
        Bike bike = BikeService.get().findById(id);
        if (bike == null) {
            System.out.println("  ! bike not found");
            CLIUtils.pressEnter();
            return;
        }
        if (!bike.isAvailable()) {
            System.out.println("  ! bike not available");
            CLIUtils.pressEnter();
            return;
        }

        // 3) reject if the same user already has a PENDING request for this bike
        for (Booking b : BookingService.get().byRenter(renterName)) {
            if (b.getBike().getId() == id && b.getStatus() == Booking.Status.PENDING) {
                System.out.println("  ! you already have a pending booking for this bike");
                CLIUtils.pressEnter();
                return;
            }
        }

        // 4) read dates
        LocalDate today = LocalDate.now();
        LocalDate s = CLIUtils.readDate("Start date");
        if (s.isBefore(today)) {
            System.out.println("  ! start date cannot be in the past");
            CLIUtils.pressEnter();
            return;
        }
        LocalDate e = CLIUtils.readDate("End date");
        if (e.isBefore(s)) {
            System.out.println("  ! end date must be on/after start date");
            CLIUtils.pressEnter();
            return;
        }

        // 5) create the booking
        BookingService.get().create(bike, renterName, s, e);
        System.out.println("Request submitted (pending admin approval).");
        CLIUtils.pressEnter();
    }

    private void cancelBooking() {
        int id = CLIUtils.readInt("Enter booking ID to cancel");
        Booking b = BookingService.get().find(id);
        if (b == null) {
            System.out.println("  ! booking not found");
        } else if (!b.getRenterName().equalsIgnoreCase(renterName)) {
            System.out.println("  ! that's not your booking");
        } else if (b.getStatus() != Booking.Status.PENDING) {
            System.out.println("  ! only pending bookings can be cancelled");
        } else {
            BookingService.get().cancel(id);
            System.out.println("Booking cancelled.");
        }
        CLIUtils.pressEnter();
    }
}
