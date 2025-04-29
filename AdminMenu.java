import java.util.List;
import java.time.LocalDate;
import java.util.Iterator;

public final class AdminMenu {
    private final String adminName;

    public AdminMenu(String adminName) {
        this.adminName = adminName;
    }

    public void start() {
        while (true) {
            BookingService.get().cleanupExpired();

            CLIUtils.clearScreen();
            System.out.println("");
            System.out.println("=== Admin Menu (" + adminName + ") ===");
            System.out.println(" 1. List bikes");
            System.out.println(" 2. Add bike");
            System.out.println(" 3. Edit bike");
            System.out.println(" 4. Delete bike");
            System.out.println(" 5. View rental requests");
            System.out.println(" 6. Approve request");
            System.out.println(" 7. Reject request");
            System.out.println(" 0. Logout");
            int c = CLIUtils.readInt("Choose");
            switch (c) {
                case 1:
                    listBikes();
                    break;
                case 2:
                    addBike();
                    break;
                case 3:
                    editBike();
                    break;
                case 4:
                    deleteBike();
                    break;
                case 5:
                    listRequests();
                    break;
                case 6:
                    changeRequest(true);
                    break;
                case 7:
                    changeRequest(false);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void listBikes() {
        System.out.println("");
        System.out.println("ID | Make       | Model        | Type      | Price  | Avail");
        for (Bike b : BikeService.get().all()) {
            System.out.println(b);
        }
        CLIUtils.pressEnter();
    }

    private void addBike() {
        String make = CLIUtils.readNonEmpty("Make");
        String model = CLIUtils.readNonEmpty("Model");
        String type = CLIUtils.readNonEmpty("Type");
        double price = CLIUtils.readDouble("Price per day");
        boolean avail = CLIUtils.readBoolean("Available", true);

        Bike b = new Bike(make, model, type, price);
        b.setAvailable(avail);
        BikeService.get().addBike(b);

        System.out.println("Bike added.");
        CLIUtils.pressEnter();
    }

    private void editBike() {
        int id = CLIUtils.readInt("Enter bike ID to edit");
        Bike b = BikeService.get().findById(id);
        if (b == null) {
            System.out.println("  ! bike not found");
            CLIUtils.pressEnter();
            return;
        }

        String makeInput = CLIUtils.readLine("Make [" + b.getMake() + "]");
        if (!makeInput.isEmpty())
            b.setMake(makeInput);

        String modelInput = CLIUtils.readLine("Model [" + b.getModel() + "]");
        if (!modelInput.isEmpty())
            b.setModel(modelInput);

        String typeInput = CLIUtils.readLine("Type [" + b.getType() + "]");
        if (!typeInput.isEmpty())
            b.setType(typeInput);

        String priceInput = CLIUtils.readLine("Price per day [" + b.getPricePerDay() + "]");
        if (!priceInput.isEmpty()) {
            try {
                b.setPricePerDay(Double.parseDouble(priceInput));
            } catch (NumberFormatException e) {
                System.out.println("  ! invalid price, keeping existing value");
            }
        }

        boolean avail = CLIUtils.readBoolean("Available", b.isAvailable());
        b.setAvailable(avail);

        BikeService.get().save();
        System.out.println("Bike updated.");
        CLIUtils.pressEnter();
    }

    private void deleteBike() {
        int id = CLIUtils.readInt("Enter bike ID to delete");
        Bike b = BikeService.get().findById(id);
        if (b == null) {
            System.out.println("  ! bike not found");
        } else {
            boolean hasActive = false;
            for (Booking bk : BookingService.get().all()) {
                if (bk.getBike().getId() == id &&
                        (bk.getStatus() == Booking.Status.PENDING ||
                                bk.getStatus() == Booking.Status.APPROVED)) {
                    hasActive = true;
                    break;
                }
            }
            if (hasActive) {
                System.out.println("  ! cannot delete bike with pending or approved bookings");
            } else {
                BikeService.get().deleteBike(id);
                System.out.println("Bike removed.");
            }
        }
        CLIUtils.pressEnter();
    }

    private void listRequests() {
        List<Booking> req = BookingService.get().pending();
        if (req.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            System.out.println("");
            System.out.println("ID | Renter | Bike | Start → End");
            for (Booking b : req) {
                System.out.println(String.format("%-3d | %-6s | %-25s | %s → %s",
                        b.getId(), b.getRenterName(), b.getBike(), b.getStart(), b.getEnd()));
            }
        }
        CLIUtils.pressEnter();
    }

    private void changeRequest(boolean approve) {
        // Fetch pending requests to make it easier to see what's available
        List<Booking> req = BookingService.get().pending();

        if (req.isEmpty()) {
            System.out.println("No pending requests.");
            CLIUtils.pressEnter();
            return;
        }

        System.out.println("\nPending requests:");
        System.out.println("ID  | Renter  | Bike                     | Start → End");
        System.out.println("-------------------------------------------------------");
        for (Booking b : req) {
            System.out.println(String.format(
                    "%-3d | %-7s | %-25s | %s → %s",
                    b.getId(),
                    b.getRenterName(),
                    b.getBike(),
                    b.getStart(),
                    b.getEnd()));
        }

        int id = CLIUtils.readInt("\nEnter booking ID to " + (approve ? "approve" : "reject") + " (0 to cancel)");
        Booking b = BookingService.get().find(id);

        if (b == null) {
            System.out.println("  ! booking not found backing out");
        } else if (b.getStatus() != Booking.Status.PENDING) {
            System.out.println("  ! booking already " +
                    b.getStatus().name().toLowerCase() +
                    ". Check rental requests to see current pending requests.");
        } else {
            b.setStatus(approve ? Booking.Status.APPROVED : Booking.Status.REJECTED);
            if (approve) {
                b.getBike().setAvailable(false);
            }
            Storage.save();
            System.out.println("Request " + (approve ? "approved." : "rejected."));
        }
        CLIUtils.pressEnter();
    }
}
