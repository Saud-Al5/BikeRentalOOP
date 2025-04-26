import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BikeService {
    private final List<Bike> bikes = Storage.get().bikes;
    private static final BikeService INSTANCE = new BikeService();

    public static BikeService get() {
        return INSTANCE;
    }

    private BikeService() {
    }

    public List<Bike> all() {
        return new ArrayList<Bike>(bikes);
    }

    public void addBike(Bike b) {
        bikes.add(b);
        Storage.save();
    }

    public void deleteBike(int id) {
        Iterator<Bike> it = bikes.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                it.remove();
            }
        }
        Storage.save();
    }

    public Bike findById(int id) {
        for (Bike b : bikes)
            if (b.getId() == id)
                return b;
        return null;
    }

    public List<Bike> search(String term) {
        List<Bike> res = new ArrayList<Bike>();
        if (term == null)
            term = "";
        term = term.toLowerCase();
        for (Bike b : bikes) {
            String combined = (b.getMake() + " " + b.getModel() + " " + b.getType()).toLowerCase();
            if (combined.contains(term))
                res.add(b);
        }
        return res;
    }

    public void save() {
        Storage.save();
    }
}
