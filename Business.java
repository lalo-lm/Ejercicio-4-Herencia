import java.util.ArrayList;
import java.util.List;

public class Business {
    private List<Equipment> equipments;
    private double totalIncome;

    public Business(){
        this.equipments = new ArrayList<>();
        this.totalIncome = 0.0;
        initializeTestData();
    }

    private void initializeTestData() {
        equipments.add(new Proyector("P-001", "Epson", "PowerLite 1781w", 150.0, 3200, true));
        equipments.add(new Proyector("P-002", "Sony", "VPL-EX435", 120.0, 2800, false));
    
        equipments.add(new Camera("C-001", "Canon", "EOS R50", 250.0, 2160));
        equipments.add(new Camera("C-002", "Sony", "ZV-1F", 200.0, 1080));

        equipments.add(new SoundSystem("S-001", "JBL", "PartyBox 310", 200.0, 1.5));
        equipments.add(new SoundSystem("S-002", "Bose", "S1 Pro+", 180.0, 0.8));
    }

    public boolean registerEquipment(Equipment equipment) {
        if (searchEquipment(equipment.getInventoryCode()) != null) {
            return false; 
        }
        equipments.add(equipment);
        return true;
    }

    public Equipment searchEquipment(String inventoryCode) {
        for (Equipment e : equipments) {
            if (e.getInventoryCode().equalsIgnoreCase(inventoryCode)) {
                return e;
            }
        }
        return null; 
    }

    public boolean confirmRental(String inventoryCode, int days) {
        Equipment eq = searchEquipment(inventoryCode);
        if (eq == null || !eq.isAvailable()) {
            return false;
        }
        double totalCost = eq.calculateCost(days);
        eq.setAvailable(false);
        totalIncome += totalCost;
        return true;
    }

    public boolean registerReturn(String inventoryCode) {
        Equipment eq = searchEquipment(inventoryCode);
        if (eq == null || eq.isAvailable()) {
            return false;
        }
        eq.setAvailable(true);
        return true;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public List<Equipment> getEquipment() {
        return equipments;
    }
}