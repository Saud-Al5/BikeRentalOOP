import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class BookingService {
    private final List<Booking> bookings = Storage.get().bookings;
    private static final BookingService INSTANCE = new BookingService();

    public static BookingService get() {
        return INSTANCE;
    }

    private BookingService() {
    }

    public List<Booking> all() {
        return new ArrayList<Booking>(bookings);
    }

    public Booking create(Bike bike, String renter, LocalDate s, LocalDate e) {
        Booking bk = new Booking(bike, renter, s, e);
        bookings.add(bk);
        Storage.save();
        return bk;
    }

    public Booking find(int id) {
        for (Booking b : bookings)
            if (b.getId() == id)
                return b;
        return null;
    }

    public List<Booking> byRenter(String renter) {
        List<Booking> list = new ArrayList<Booking>();
        for (Booking b : bookings) {
            if (b.getRenterName().equalsIgnoreCase(renter))
                list.add(b);
        }
        return list;
    }

    public List<Booking> pending() {
        List<Booking> list = new ArrayList<Booking>();
        for (Booking b : bookings) {
            if (b.getStatus() == Booking.Status.PENDING)
                list.add(b);
        }
        return list;
    }

    public void cancel(int id) {
        Booking b = find(id);
        if (b != null) {
            // if it was approved, put the bike back
            if (b.getStatus() == Booking.Status.APPROVED) {
                b.getBike().setAvailable(true);
            }
            b.setStatus(Booking.Status.CANCELLED);
            Storage.save();
        }
    }

    public void save() {
        Storage.save();
    }
}
