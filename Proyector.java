public class Proyector extends Equipment {
    private int brightness; // En lúmenes
    private boolean wirelessConnectivity;

    public Proyector(String inventoryCode, String brand, String model, double dailyRate, int brightness, boolean wirelessConnectivity) {
        super(inventoryCode, brand, model, dailyRate);
        this.brightness = brightness;
        this.wirelessConnectivity = wirelessConnectivity;
    }

    public int getBrightness() {
        return brightness;
    }

    public boolean hasWirelessConnectivity() {
        return wirelessConnectivity;
    }

    @Override
    public double calculateCost(int days) {
        double baseCost = dailyRate * days;
        double surcharge = wirelessConnectivity ? (50.0 * days) : 0.0;
        return baseCost + surcharge;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Luminosidad: %d lúmenes | Inalámbrico: %s", brightness, wirelessConnectivity ? "Sí" : "No");
    }
}