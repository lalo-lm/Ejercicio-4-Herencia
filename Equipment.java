public abstract class Equipment {
    protected String inventoryCode;
    protected String brand;
    protected String model;
    protected double dailyRate;
    protected boolean available;

    public Equipment(String inventoryCode, String brand, String model, double dailyRate) {
        this.inventoryCode = inventoryCode;
        this.brand = brand;
        this.model = model;
        this.dailyRate = dailyRate;
        this.available = true; // El equipo recien adquirido esta disponible por defecto
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract double calculateCost(int days); 

    @Override
    public String toString() {
        String state = available ? "Disponible" : "Alquilado (No disponible)";
        return String.format("Código: %s, Marca: %s, Modelo: %s, Tarifa Diaria: %.2f, Estado: %s",
                inventoryCode, brand, model, dailyRate, state);
    }
}