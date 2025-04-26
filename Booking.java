import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int idCounter = 1;

    public static void syncCounter(int next) {
        idCounter = next;
    }

    public static int counter() {
        return idCounter;
    }

    public enum Status {
        PENDING, APPROVED, REJECTED, CANCELLED
    }

    private final int id;
    private final Bike bike;
    private final String renterName;
    private LocalDate start;
    private LocalDate end;
    private Status status;

    public Booking(int id, Bike bike, String renterName,
            LocalDate start, LocalDate end, Status status) {
        this.id = id;
        this.bike = bike;
        this.renterName = renterName;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public Booking(Bike bike, String renterName,
            LocalDate start, LocalDate end) {
        this(idCounter++, bike, renterName, start, end, Status.PENDING);
    }

    public int getId() {
        return id;
    }

    public Bike getBike() {
        return bike;
    }

    public String getRenterName() {
        return renterName;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate s) {
        this.start = s;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate e) {
        this.end = e;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status st) {
        this.status = st;
    }

    @Override
    public String toString() {
        return String.format("%-3d | %-8s | %s → %s | %s",
                id, bike.getModel(), start, end, status);
    }
}
