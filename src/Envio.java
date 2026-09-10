import java.util.ArrayList;
import java.util.List;

public abstract class Envio {
    private static final int CANTIDAD_MAXIMA = 3;
    private static final double PESO_MAXIMO = 100;
    private static int ultimoId = 0;

    private final int id = ++ultimoId;
    private final List<Paquete> paquetes = new ArrayList<>();
    // Todos los acontecimientos del recorrido, en orden.
    private final List<RegistroSeguimiento> historial = new ArrayList<>();
    private final Cliente cliente;
    private final Sucursal destino;
    // Dónde está el envío ahora; null mientras viaja entre dos sucursales.
    private Sucursal sucursalActual;
    // Hacia dónde viaja; null cuando está detenido en una sucursal.
    private Sucursal enCaminoA;

    // Cliente y Sucursal sólo guardan la referencia al envío, no le piden nada todavía.
    @SuppressWarnings("this-escape")
    protected Envio(Cliente cliente, Sucursal origen, Sucursal destino, Paquete... paquetes) {
        if (cliente == null) {
            throw new IllegalArgumentException("El envío necesita un cliente.");
        }
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("El envío necesita origen y destino.");
        }
        if (paquetes.length == 0) {
            throw new IllegalArgumentException("Un envío debe transportar al menos un paquete.");
        }
        this.cliente = cliente;
        this.destino = destino;
        this.sucursalActual = origen;
        for (Paquete paquete : paquetes) {
            agregarPaquete(paquete);
        }
        cliente.agregarEnvio(this);
        origen.tomar(this);
        registrarMovimiento(origen, "Envío recibido");
    }

    private void agregarPaquete(Paquete paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo.");
        }
        if (paquetes.size() == CANTIDAD_MAXIMA) {
            throw new IllegalStateException("Un envío no puede llevar más de " + CANTIDAD_MAXIMA + " paquetes.");
        }
        if (pesoTotal() + paquete.getPeso() > PESO_MAXIMO) {
            throw new IllegalStateException("El envío no puede superar los " + PESO_MAXIMO + "kg.");
        }
        paquete.asignarAEnvio();
        paquetes.add(paquete);
    }

    // Las cuatro operaciones del recorrido: sólo la sucursal que tiene el envío puede pedirlas.
    void preparar() {
        for (Paquete paquete : paquetes) {
            paquete.prepararParaEnvio();
        }
        registrarMovimiento(sucursalActual, "Envío preparado");
    }

    void despachar(Sucursal siguiente) {
        if (estaEntregado()) {
            throw new IllegalStateException("No se puede despachar un envío ya entregado.");
        }
        for (Paquete paquete : paquetes) {
            paquete.iniciarViaje();
        }
        registrarMovimiento(sucursalActual, "Envío despachado");
        sucursalActual.soltar(this);
        sucursalActual = null;
        enCaminoA = siguiente;
    }

    void recibir(Sucursal sucursal) {
        if (enCaminoA != sucursal) {
            throw new IllegalStateException("El envío " + id + " no fue despachado hacia " + sucursal + ".");
        }
        sucursalActual = sucursal;
        enCaminoA = null;
        sucursal.tomar(this);
        registrarMovimiento(sucursal, "Envío recibido");
    }

    void entregar() {
        if (sucursalActual != destino) {
            throw new IllegalStateException("El envío " + id + " sólo puede entregarse en su destino (" + destino + ").");
        }
        for (Paquete paquete : paquetes) {
            paquete.entregar();
        }
        registrarMovimiento(destino, "Envío entregado");
    }

    // Un envío está entregado cuando lo están todos sus paquetes.
    public boolean estaEntregado() {
        for (Paquete paquete : paquetes) {
            if (!paquete.estaEntregado()) {
                return false;
            }
        }
        return true;
    }

    // Consultas sobre el recorrido: se le preguntan al envío, dueño de su historial.
    public void mostrarHistorial() {
        for (RegistroSeguimiento movimiento : historial) {
            System.out.println("   " + movimiento);
        }
    }

    public RegistroSeguimiento getUltimoMovimiento() {
        return historial.get(historial.size() - 1);
    }

    public List<Sucursal> getSucursalesRecorridas() {
        List<Sucursal> recorridas = new ArrayList<>();
        for (RegistroSeguimiento movimiento : historial) {
            if (!recorridas.contains(movimiento.getSucursal())) {
                recorridas.add(movimiento.getSucursal());
            }
        }
        return recorridas;
    }

    private void registrarMovimiento(Sucursal sucursal, String descripcion) {
        historial.add(new RegistroSeguimiento(sucursal, descripcion));
    }

    // Se calcula al momento de preguntarlo: nunca queda desactualizado.
    public double getCosto() { return pesoTotal() * tarifa(); }

    private double pesoTotal() {
        double total = 0;
        for (Paquete paquete : paquetes) {
            total += paquete.getPeso();
        }
        return total;
    }

    // Cada tipo de envío cobra distinto; el cálculo del costo es uno solo.
    protected abstract double tarifa();

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Sucursal getDestino() { return destino; }
    public Sucursal getSucursalActual() { return sucursalActual; }
    public List<Paquete> getPaquetes() { return new ArrayList<>(paquetes); }

    @Override
    public String toString() {
        String ubicacion = sucursalActual != null ? "en " + sucursalActual : "viajando hacia " + enCaminoA;
        return "Envío " + id + " - " + getClass().getSimpleName() + " - " + cliente + " - " + ubicacion;
    }
}
