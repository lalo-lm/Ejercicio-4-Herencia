import java.util.List;
import java.util.Scanner;

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
        System.out.println("\n--- REGISTRAR NUEVO EQUIPO ---");
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

        double dailyRate = readPositiveDouble(scanner, "Ingrese la tarifa diaria (Q): ");

        if (dailyRate <= 0) {
            System.out.println("Tarifa diaria inválida. Registro cancelado.");
            return;
        }

        Equipment newEquipment = null;

        if (cat == 1) {
            int lum = readPositiveInt(scanner, "Ingrese luminosidad en lúmenes (entero): ");
            System.out.print("¿Tiene conectividad inalámbrica? (s/n): ");
            String resp = scanner.nextLine().trim().toLowerCase();
            boolean inalambrica = resp.equals("s") || resp.equals("si");
            newEquipment = new Proyector(inventoryCode, brand, model, dailyRate, lum, inalambrica);
        } else if (cat == 2) {
            int res = readPositiveInt(scanner, "Ingrese resolución máxima en píxeles verticales (ej. 1080, 2160): ");
            newEquipment = new Camera(inventoryCode, brand, model, dailyRate, res);
        } else if (cat == 3) {
            double pot = readPositiveDouble(scanner, "Ingrese potencia nominal en kilovatios kW (admite decimales): ");
            newEquipment = new SoundSystem(inventoryCode, brand, model, dailyRate, pot);
        }

        if (newEquipment != null || business.registerEquipment(newEquipment)) {
            System.out.println("Equipo registrado y disponible para alquiler.");
        } else {
            System.out.println("Error al registrar el equipo. Intente nuevamente.");
        }
    }

    private static void consultInventoryMenu(Business business) {
        System.out.println("\n--- INVENTARIO GENERAL ---");
        List<Equipment> equipments = business.getEquipment();
        if (equipments.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        for (Equipment e : equipments) {
            System.out.println(e);
        }
    }

    private static void quoteRentalMenu(Scanner scanner, Business business) {
        System.out.println("\n--- Cotizar Alquiler ---");
        System.out.print("Ingrese el código de inventario del equipo: ");
        String inventoryCode = scanner.nextLine().trim();

        Equipment eq = business.searchEquipment(inventoryCode);
        if (eq == null) {
            System.out.println("Equipo no encontrado.");
            return;
        }

        int days = readPositiveInt(scanner, "Ingrese la cantidad de días para el alquiler: ");
        double totalCost = eq.calculateCost(days);

        System.out.printf("\n--- RESULTADO DE LA COTIZACIÓN ---");
        System.out.println(eq);
        System.out.printf("Días cotizados: %d\n", days);
        System.out.printf("Costo total estimado: Q%.2f\n", totalCost);
        
        if (!eq.isAvailable()) {
            System.out.println("Nota: Este equipo actualmente está alquilado y no disponible.");
        }
    }

    private static void confirmRentalMenu(Scanner scanner, Business business) {
        System.out.println("\n--- CONFIRMAR ALQUILER ---");
        System.out.print("Ingrese el código de inventario: ");
        String codigo = scanner.nextLine().trim();

        Equipment eq = business.searchEquipment(codigo);
        if (eq == null) {
            System.out.println("Error: Código de inventario inexistente.");
            return;
        }

        if (!eq.isAvailable()) {
            System.out.println("Error: El equipo se encuentra actualmente ocupado y no puede alquilarse.");
            return;
        }

        int dias = readPositiveInt(scanner, "Ingrese la cantidad de días de alquiler: ");
        double costoTotal = eq.calculateCost(dias);

        System.out.println("\n--- RESUMEN DE OPERACIÓN ---");
        System.out.println(eq);
        System.out.printf("Días: %d | Total a pagar: Q%.2f\n", dias, costoTotal);
        System.out.print("¿Desea confirmar el alquiler y efectuar el cobro? (s/n): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();

        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            boolean exito = business.confirmRental(codigo, dias);
            if (exito) {
                System.out.printf("¡Alquiler confirmado con éxito! Cobro realizado: Q%.2f. El equipo ahora está ocupado.\n", costoTotal);
            } else {
                System.out.println("Error: No se pudo procesar el alquiler.");
            }
        } else {
            System.out.println("Operación cancelada. El estado y los ingresos permanecen intactos.");
        }
    }

    private static void registerReturnMenu(Scanner scanner, Business business) {
        System.out.println("\n--- REGISTRAR DEVOLUCIÓN ---");
        System.out.print("Ingrese el código de inventario del equipo a devolver: ");
        String codigo = scanner.nextLine().trim();

        Equipment eq = business.searchEquipment(codigo);
        if (eq == null) {
            System.out.println("Error: Código de inventario inexistente.");
            return;
        }

        if (eq.isAvailable()) {
            System.out.println("Error: El equipo ya se encuentra disponible. No se puede devolver.");
            return;
        }

        boolean exito = business.registerReturn(codigo);
        if (exito) {
            System.out.println("¡Devolución registrada con éxito! El equipo vuelve a estar disponible.");
        } else {
            System.out.println("Error al registrar la devolución.");
        }
    }

    private static void reportIncomeMenu(Business business) {
        System.out.println("\n--- REPORTE GENERAL DE INGRESOS ---");
        List<Equipment> lista = business.getEquipment();

        int regProy = 0, dispProy = 0, alqProy = 0;
        int regCam = 0, dispCam = 0, alqCam = 0;
        int regSou = 0, dispSou = 0, alqSou = 0;

        for (Equipment e : lista) {
            if (e instanceof Proyector) {
                regProy++;
                if (e.isAvailable()) dispProy++; else alqProy++;
            } else if (e instanceof Camera) {
                regCam++;
                if (e.isAvailable()) dispCam++; else alqCam++;
            } else if (e instanceof SoundSystem) {
                regSou++;
                if (e.isAvailable()) dispSou++; else alqSou++;
            }
        }

        System.out.println("1. PROYECTORES:");
        System.out.printf("   - Registrados: %d | Disponibles: %d | Alquilados: %d\n", regProy, dispProy, alqProy);
        System.out.println("2. CÁMARAS DE VIDEO:");
        System.out.printf("   - Registrados: %d | Disponibles: %d | Alquilados: %d\n", regCam, dispCam, alqCam);
        System.out.println("3. EQUIPOS DE SONIDO:");
        System.out.printf("   - Registrados: %d | Disponibles: %d | Alquilados: %d\n", regSou, dispSou, alqSou);
        System.out.println("--------------------------------------------------");
        System.out.printf("TOTAL EQUIPOS REGISTRADOS EN LA EMPRESA: %d\n", lista.size());
        System.out.printf("DINERO ACUMULADO POR ALQUILERES CONFIRMADOS: Q%.2f\n", business.getTotalIncome());
    }

    private static int readPositiveInt(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                if (valor > 0) return valor;
                System.out.println("Error: El valor debe ser mayor a cero.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número entero válido.");
            }
        }
    }

    private static double readPositiveDouble(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(scanner.nextLine());
                if (valor > 0) return valor;
                System.out.println("Error: El valor debe ser mayor a cero.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un valor numérico válido.");
            }
        }
    }
}