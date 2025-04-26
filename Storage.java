import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Bike> bikes = new ArrayList<Bike>();
    public List<Booking> bookings = new ArrayList<Booking>();
    public int bikeCounter = 1;
    public int bookingCounter = 1;


    private static Storage INSTANCE;

    private static final String FILE = "data.bin";

    static {
        load();
    }

    private static void load() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(FILE));
            INSTANCE = (Storage) in.readObject();
            Bike.syncCounter(INSTANCE.bikeCounter);
            Booking.syncCounter(INSTANCE.bookingCounter);
        } catch (Exception e) {
            INSTANCE = new Storage();
            Bike.syncCounter(1);
            Booking.syncCounter(1);
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ignore) {
            }
        }

        // seed bikes if first run
        if (INSTANCE.bikes.isEmpty()) {
            INSTANCE.bikes.add(new Bike("Trek", "Ranger", "Mountain", 35));
            INSTANCE.bikes.add(new Bike("Special", "Swift", "Electric", 50));
            INSTANCE.bikes.add(new Bike("Giant", "Pioneer", "Gearless", 25));
            INSTANCE.bikes.add(new Bike("Cannondale", "Blaze", "Mountain", 40));
            INSTANCE.bikes.add(new Bike("Special", "Volt", "Electric", 55));
            INSTANCE.bikes.add(new Bike("Giant", "Sprint", "Road", 45));
            save();
        }
    }

    public static Storage get() {
        return INSTANCE;
    }

    public static void save() {
        INSTANCE.bikeCounter = Bike.counter();
        INSTANCE.bookingCounter = Booking.counter();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(FILE));
            out.writeObject(INSTANCE);
        } catch (Exception e) {
            System.err.println("Could not save data: " + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ignore) {
            }
        }
    }
}
