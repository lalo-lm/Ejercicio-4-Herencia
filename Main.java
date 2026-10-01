import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Business business = new Business();
        int option = 0;

        do {
            System.out.println("\n========================================");
            System.out.println("    SISTEMA DE GESTIÓN - ENESCENA"    );
            System.out.println("========================================");
            System.out.println("1. Registrar nuevo equipo");
            System.out.println("2. Consultar inventario");
            System.out.println("3. Cotizar alquiler del equipo");
            System.out.println("4. Confirmar alquiler");
            System.out.println("5. Registrar devolución");
            System.out.println("6. Reporte general de ingresos");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                continue;
            }

            switch (option) {
                case 1:
                    registerEquipmentMenu(scanner, business);
                    break;
                case 2:
                    consultInventoryMenu(business);
                    break;
                case 3:
                    quoteRentalMenu(scanner, business);
                    break;
                case 4:
                    confirmRentalMenu(scanner, business);
                    break;
                case 5:
                    registerReturnMenu(scanner, business);
                    break;
                case 6:
                    reportIncomeMenu(business);
                    break;
                case 7:
                    System.out.println("¡Gracias por usar el sistema de EnEscena!");
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (option != 7);

        scanner.close();
    } 

    private static void registerEquipmentMenu(Scanner scanner, Business business) {
        System.out.println("\n--- Registrar Nuevo Equipo ---");
        System.out.print("Seleccione la categoría:");
        System.out.print("1. Proyector");
        System.out.print("2. Cámara");
        System.out.print("3. Equipo de Sonido");
        System.out.print("Opción: ");
        
        int cat;
        try {
            cat = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Categoría inválida. Registro cancelado.");
            return;
        }

        if (cat < 1 || cat > 3) {
            System.out.println("Categoría inválida. Registro cancelado.");
            return;
        }

        System.out.print("Ingrese el código de inventario: ");
        String inventoryCode = scanner.nextLine().trim();

        if (inventoryCode.isEmpty()) {
            System.out.println("Código de inventario no puede estar vacío. Registro cancelado.");
            return;
        }

        if (business.searchEquipment(inventoryCode) != null) {
            System.out.println("El código de inventario ya existe. Registro cancelado.");
            return;
        }

        System.out.print("Ingrese la marca: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Ingrese el modelo: ");
        String model = scanner.nextLine().trim();

        double dailyRate = readDoublePositive(scanner, "Ingrese la tarifa diaria (Q): ");

        if (dailyRate <= 0) {
            System.out.println("Tarifa diaria inválida. Registro cancelado.");
            return;
        }

        Equipment newEquipment = null;

        if (cat == 1) {
            int lum = readIntPositive(scanner, "Ingrese luminosidad en lúmenes (entero): ");
            System.out.print("¿Tiene conectividad inalámbrica? (s/n): ");
            String resp = scanner.nextLine().trim().toLowerCase();
            boolean inalambrica = resp.equals("s") || resp.equals("si");
            newEquipment = new Proyector(inventoryCode, brand, model, dailyRate, lum, inalambrica);
        } else if (cat == 2) {
            int res = readIntPositive(scanner, "Ingrese resolución máxima en píxeles verticales (ej. 1080, 2160): ");
            newEquipment = new Camera(inventoryCode, brand, model, dailyRate, res);
        } else if (cat == 3) {
            double pot = readDoublePositive(scanner, "Ingrese potencia nominal en kilovatios kW (admite decimales): ");
            newEquipment = new SoundSystem(inventoryCode, brand, model, dailyRate, pot);
        }

        if (newEquipment != null || business.regiterEquipment(newEquipment)) {
            System.out.println("Equipo registrado y disponible para alquiler.");
        } else {
            System.out.println("Error al registrar el equipo. Intente nuevamente.");
        }
    }

    private static void consultInventoryMenu(Business business) {
        System.out.println("\n--- INVENTARIO GENERAL ---");
        List<Equipment> equipments = business.getEquipments();
        if (equipments.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        for (Equipment e : equipments) {
            System.out.println(e);
        }
    }
}