import java.util.ArrayList;
import java.util.List;

public abstract class Envio {
    private static final int CANTIDAD_MINIMA = 1;
    private static final int CANTIDAD_MAXIMA = 3;
    private static final double PESO_MAXIMO = 100;

    private final List<Paquete> paquetes = new ArrayList<>();
    // Conserva todos los acontecimientos del recorrido del envío.
    private final List<RegistroSeguimiento> historial = new ArrayList<>();
    // Identifica al cliente dueño de este envío.
    private final Cliente cliente;
    // Indica dónde se encuentra actualmente el envío.
    private Sucursales sucursalActual;
    // Indica la sucursal final a la que debe llegar el envío.
    private final Sucursales destino;
    private double costo;

    private Envio(Cliente cliente) {
        this(cliente, Sucursales.VIEDMA, Sucursales.VIEDMA);
    }

    private Envio(Cliente cliente, Sucursales origen, Sucursales destino) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser nulos.");
        }
        this.cliente = cliente;
        this.sucursalActual = origen;
        this.destino = destino;
        // Registra este envío también en la lista del cliente.
        cliente.agregarEnvio(this);
        // Registra el envío en la sucursal donde comienza el recorrido.
        origen.agregarEnvio(this);
        registrarMovimiento(origen, "Envío recibido");
    }

    public Envio(Cliente cliente, Paquete paquete) {
        this(cliente);
        agregarPaquete(paquete);
    }

    public Envio(Cliente cliente, Sucursales origen, Sucursales destino, Paquete paquete) {
        this(cliente, origen, destino);
        agregarPaquete(paquete);
    }

    public Envio(Cliente cliente, List<Paquete> paquetesIniciales) {
        this(cliente);
        if (paquetesIniciales == null || paquetesIniciales.isEmpty()) {
            throw new IllegalArgumentException("Un envío debe tener al menos un paquete.");
        }
        for (Paquete paquete : paquetesIniciales) {
            agregarPaquete(paquete);
        }
    }

    public void agregarPaquete(Paquete paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo.");
        }
        if (paquetes.size() >= CANTIDAD_MAXIMA) {
            throw new IllegalStateException("No se pueden agregar más de " + CANTIDAD_MAXIMA + " paquetes.");
        }
        paquetes.add(paquete);
    }

    public List<Paquete> getPaquetes() {
        return new ArrayList<>(paquetes);
    }

    // Permite consultar a qué cliente pertenece el envío.
    public Cliente getCliente() {
        return cliente;
    }

    // Devuelve la sucursal en la que se procesa actualmente el envío.
    public Sucursales getSucursalActual() {
        return sucursalActual;
    }

    // Devuelve la sucursal final del recorrido.
    public Sucursales getDestino() {
        return destino;
    }

    // Agrega un acontecimiento al historial interno del envío.
    void registrarMovimiento(Sucursales sucursal, String descripcion) {
        historial.add(new RegistroSeguimiento(this, sucursal, descripcion));
    }

    // Devuelve una copia del historial completo del envío.
    public List<RegistroSeguimiento> getHistorial() {
        return new ArrayList<>(historial);
    }

    // Muestra todos los acontecimientos registrados en el recorrido.
    public void mostrarHistorial() {
        for (RegistroSeguimiento movimiento : historial) {
            System.out.println(movimiento);
        }
    }

    // Devuelve el último acontecimiento o null si todavía no hay registros.
    public RegistroSeguimiento getUltimoMovimiento() {
        if (historial.isEmpty()) {
            return null;
        }
        return historial.get(historial.size() - 1);
    }

    // Devuelve las sucursales por las que pasó el envío, sin repetirlas.
    public List<Sucursales> getSucursalesRecorridas() {
        List<Sucursales> sucursales = new ArrayList<>();
        for (RegistroSeguimiento movimiento : historial) {
            if (!sucursales.contains(movimiento.getSucursal())) {
                sucursales.add(movimiento.getSucursal());
            }
        }
        return sucursales;
    }

    // Mueve el envío de la sucursal actual a otra sucursal del recorrido.
    public void moverASucursal(Sucursales nuevaSucursal) {
        if (nuevaSucursal == null) {
            throw new IllegalArgumentException("La nueva sucursal no puede ser nula.");
        }
        if (nuevaSucursal == sucursalActual) {
            throw new IllegalStateException("El envío ya se encuentra en esa sucursal.");
        }
        if (estaEntregado()) {
            throw new IllegalStateException("No se puede mover un envío ya entregado.");
        }
        sucursalActual.quitarEnvio(this);
        sucursalActual = nuevaSucursal;
        nuevaSucursal.agregarEnvio(this);
    }

    // Actualiza la sucursal actual cuando una sucursal recibe el envío.
    void recibirEnSucursal(Sucursales nuevaSucursal) {
        if (nuevaSucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula.");
        }
        if (sucursalActual != nuevaSucursal) {
            throw new IllegalStateException("El envío debe ser despachado antes de recibirse en otra sucursal.");
        }
        nuevaSucursal.agregarEnvio(this);
        registrarMovimiento(nuevaSucursal, "Envío recibido");
    }

    // Actualiza el recorrido cuando la sucursal actual despacha el envío.
    void despacharASucursal(Sucursales siguienteSucursal) {
        if (siguienteSucursal == null) {
            throw new IllegalArgumentException("La siguiente sucursal no puede ser nula.");
        }
        if (estaEntregado()) {
            throw new IllegalStateException("No se puede despachar un envío ya entregado.");
        }
        registrarMovimiento(sucursalActual, "Envío despachado");
        moverASucursal(siguienteSucursal);
        registrarMovimiento(siguienteSucursal, "Envío recibido");
    }

    // Determina si todos los paquetes del envío ya fueron entregados.
    private boolean estaEntregado() {
        if (paquetes.isEmpty()) {
            return false;
        }
        for (Paquete paquete : paquetes) {
            if (!"Entregado".equals(paquete.getEstado())) {
                return false;
            }
        }
        return true;
    }

    public double getCosto() {
        return costo;
    }

    public void iniciarEnvio() {
        chequearEnvioValido();
        for (Paquete paquete : paquetes) {
            paquete.prepararParaEnvio();
        }
    }

    public void iniciarDistribucion() {
        chequearEnvioValido();
        for (Paquete paquete : paquetes) {
            paquete.marcarEnDistribucion();
        }
    }

    public void finalizarEnvio() {
        chequearEnvioValido();
        if (estaEntregado()) {
            throw new IllegalStateException("El envío ya fue entregado.");
        }
        for (Paquete paquete : paquetes) {
            if ("Recibido".equals(paquete.getEstado())) paquete.prepararParaEnvio();
            if ("En preparación".equals(paquete.getEstado())) paquete.marcarEnDistribucion();
            if ("En distribución".equals(paquete.getEstado())) paquete.entregar();
        }
        registrarMovimiento(sucursalActual, "Envío entregado");
    }

    public void calcularCosto() {
        double pesoTotal = 0;
        for (Paquete paquete : paquetes) {
            pesoTotal += paquete.getPeso();
        }
        chequearPeso(pesoTotal);
        costo = pesoTotal * tarifa();
    }

    protected abstract double tarifa();

    private void chequearPeso(double peso) {
        if (peso <= 0 || peso >= PESO_MAXIMO) {
            throw new IllegalArgumentException("El peso total debe estar entre 0 y " + PESO_MAXIMO + "kg.");
        }
    }

    private void chequearEnvioValido() {
        if (paquetes.size() < CANTIDAD_MINIMA) {
            throw new IllegalStateException("El envío debe tener al menos " + CANTIDAD_MINIMA + " paquete.");
        }
    }
}
