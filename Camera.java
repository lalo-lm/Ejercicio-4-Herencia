public class Camera extends Equipment {
    private int maxResolution; // Píxeles verticales

    public Camera(String inventoryCode, String brand, String model, double dailyRate, int maxResolution) {
        super(inventoryCode, brand, model, dailyRate);
        this.maxResolution = maxResolution;
    }

    public int getMaxResolution() {
        return maxResolution;
    }

    @Override
    public double calculateCost(int days) {
        double baseCost = dailyRate * days;
        double surcharge = (maxResolution > 1080) ? 75.0 : 0.0; // Q75.00 de cargo fijo si la resolución máxima es mayor a 1080p
        return baseCost + surcharge;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Resolución Máxima: %dp", maxResolution);
    }
}