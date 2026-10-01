public class SoundSystem extends Equipment {
    private double nominalPower; // En kilovatios (kW)

    public SoundSystem(String inventoryCode, String brand, String model, double dailyRate, double nominalPower) {
        super(inventoryCode, brand, model, dailyRate);
        this.nominalPower = nominalPower;
    }

    public double getNominalPower() {
        return nominalPower;
    }

    @Override
    public double calculateCost(int days) {
        double baseCost = dailyRate * days;
        double surcharge = 100.0 * nominalPower * days; // Q100.00 por kW por día
        return baseCost + surcharge;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Potencia: %.2f kW", nominalPower);
    }
    
}