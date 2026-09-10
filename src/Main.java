public class Main {
    public static void main(String[] args) {
        // Un mismo cliente puede solicitar varios envíos.
        Cliente ana = new Cliente("Ana Pérez", "30111222", "2920-555111");
        Cliente luis = new Cliente("Luis Gómez", "28999444");

        Envio e1 = new EnvioEstandar(ana, Sucursal.VIEDMA, Sucursal.BUENOS_AIRES,
                new Paquete(1, "Ropa", 5), new Paquete(4, "Libros", 8));
        Envio e2 = new EnvioExpress(ana, Sucursal.BAHIA_BLANCA, Sucursal.BUENOS_AIRES,
                new Paquete(2, "Electrónicos", 17));
        Envio e3 = new EnvioInternacional(luis, Sucursal.VIEDMA, Sucursal.BARILOCHE,
                new Paquete(3, "Perfume", 6), new Paquete(5, "Zapatos", 8), new Paquete(6, "Juguetes", 7));

        GestorEnvios gestor = new GestorEnvios();
        gestor.registrar(e1);
        gestor.registrar(e2);
        gestor.registrar(e3);

        // El recorrido: la sucursal pide, el envío coordina y sus paquetes cambian de estado.
        Sucursal.VIEDMA.prepararEnvio(e1);
        Sucursal.VIEDMA.despacharEnvio(e1, Sucursal.BAHIA_BLANCA);
        Sucursal.BAHIA_BLANCA.recibirEnvio(e1);
        Sucursal.BAHIA_BLANCA.despacharEnvio(e1, Sucursal.BUENOS_AIRES);
        Sucursal.BUENOS_AIRES.recibirEnvio(e1);
        Sucursal.BUENOS_AIRES.entregarEnvio(e1);

        Sucursal.BAHIA_BLANCA.prepararEnvio(e2);
        Sucursal.BAHIA_BLANCA.despacharEnvio(e2, Sucursal.BUENOS_AIRES);
        Sucursal.BUENOS_AIRES.recibirEnvio(e2);

        // Este queda viajando: todavía no llegó a Bariloche.
        Sucursal.VIEDMA.prepararEnvio(e3);
        Sucursal.VIEDMA.despacharEnvio(e3, Sucursal.BAHIA_BLANCA);
        Sucursal.BAHIA_BLANCA.recibirEnvio(e3);
        Sucursal.BAHIA_BLANCA.despacharEnvio(e3, Sucursal.BARILOCHE);

        System.out.println("===== ENVÍOS REGISTRADOS =====");
        for (Envio envio : gestor.getEnvios()) {
            System.out.println(envio);
            for (Paquete paquete : envio.getPaquetes()) {
                System.out.println(" - " + paquete);
            }
            System.out.println("   Destino: " + envio.getDestino() + " | Costo: $" + envio.getCosto());
            System.out.println("   Sucursales recorridas: " + envio.getSucursalesRecorridas());
            System.out.println("   Último movimiento: " + envio.getUltimoMovimiento());
            System.out.println("   Historial:");
            envio.mostrarHistorial();
        }

        System.out.println("\n===== INFORMACIÓN GENERAL =====");
        System.out.println("Envíos registrados: " + gestor.cantidad());
        System.out.println("Envíos entregados: " + gestor.cantidadEntregados());
        System.out.println("Facturación total: $" + gestor.facturacionTotal());
        System.out.println("Costo promedio: $" + gestor.costoPromedio());
        System.out.println("Envíos de " + ana + ": " + ana.getEnvios().size());
        System.out.println("Envíos en Buenos Aires: " + Sucursal.BUENOS_AIRES.getEnvios());

        System.out.println("\n===== OPERACIONES QUE EL SISTEMA IMPIDE =====");
        rechazar("Entregar un envío que todavía viaja", () -> Sucursal.BARILOCHE.entregarEnvio(e3));
        rechazar("Despachar un envío ya entregado", () -> Sucursal.BUENOS_AIRES.despacharEnvio(e1, Sucursal.VIEDMA));
        rechazar("Recibir un envío en una sucursal que no lo espera", () -> Sucursal.VIEDMA.recibirEnvio(e3));
        rechazar("Despachar desde una sucursal que no tiene el envío", () -> Sucursal.VIEDMA.despacharEnvio(e2, Sucursal.BARILOCHE));
        rechazar("Operar sobre un envío inexistente", () -> gestor.buscarPorId(99));
        rechazar("Crear un envío sin paquetes", () -> new EnvioEstandar(ana, Sucursal.VIEDMA, Sucursal.BARILOCHE));
        rechazar("Llevar el mismo paquete en dos envíos", () -> new EnvioExpress(ana, Sucursal.VIEDMA, Sucursal.BARILOCHE, e2.getPaquetes().get(0)));
        rechazar("Entregar un paquete sin despacharlo", () -> new Paquete(7, "Suelto", 1).entregar());
    }

    // Muestra que la operación inválida fue rechazada.
    private static void rechazar(String operacion, Runnable intento) {
        try {
            intento.run();
            System.out.println("[NO SE BLOQUEÓ] " + operacion);
        } catch (RuntimeException error) {
            System.out.println("[bloqueado] " + operacion + " -> " + error.getMessage());
        }
    }
}
