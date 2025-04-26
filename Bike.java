import java.io.Serializable;

public class Bike implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int idCounter = 1;

    public static void syncCounter(int next) {
        idCounter = next;
    }

    public static int counter() {
        return idCounter;
    }

    private final int id;
    private String make;
    private String model;
    private String type;
    private double pricePerDay;
    private boolean available;

    public Bike(int id, String make, String model, String type,
            double pricePerDay, boolean available) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.type = type;
        this.pricePerDay = pricePerDay;
        this.available = available;
    }

    public Bike(String make, String model, String type, double pricePerDay) {
        this(idCounter++, make, model, type, pricePerDay, true);
    }

    public int getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double p) {
        this.pricePerDay = p;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean a) {
        this.available = a;
    }

    @Override
    public String toString() {
        return String.format("%-2d | %-10s | %-12s | %-9s | $%.2f | %s",
                id, make, model, type, pricePerDay, available ? "✓" : "✗");
    }
}
