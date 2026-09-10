public class Paquete {
    public enum Estado {
        RECIBIDO("Recibido"), EN_PREPARACION("En preparación"), EN_VIAJE("En viaje"),
        ENTREGADO("Entregado"), DEVUELTO("Devuelto");

        private final String etiqueta;

        Estado(String etiqueta) { this.etiqueta = etiqueta; }

        @Override
        public String toString() { return etiqueta; }
    }

    private final int id;
    private final String descripcion;
    private final double peso;
    private Estado estado = Estado.RECIBIDO;
    private boolean asignado;

    public Paquete(int id, String descripcion, double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso del paquete debe ser mayor a 0.");
        }
        this.id = id;
        this.descripcion = descripcion;
        this.peso = peso;
    }

    public Paquete(int id, double peso) {
        this(id, "Sin descripción", peso);
    }

    void asignarAEnvio() {
        if (asignado) {
            throw new IllegalStateException("El paquete " + id + " ya pertenece a otro envío.");
        }
        asignado = true;
    }

    public void prepararParaEnvio() { cambiarA(Estado.EN_PREPARACION); }

    public void iniciarViaje() {
        if (estado != Estado.EN_VIAJE) {
            cambiarA(Estado.EN_VIAJE);
        }
    }

    public void entregar() { cambiarA(Estado.ENTREGADO); }

    public void devolver() { cambiarA(Estado.DEVUELTO); }

    public boolean estaEntregado() { return estado == Estado.ENTREGADO; }

    private void cambiarA(Estado nuevo) {
        if (!transicionValida(nuevo)) {
            throw new IllegalStateException("El paquete " + id + " no puede pasar de " + estado + " a " + nuevo + ".");
        }
        estado = nuevo;
    }

    private boolean transicionValida(Estado nuevo) {
        return switch (estado) {
            case RECIBIDO -> nuevo == Estado.EN_PREPARACION;
            case EN_PREPARACION -> nuevo == Estado.EN_VIAJE;
            case EN_VIAJE -> nuevo == Estado.ENTREGADO;
            case ENTREGADO -> nuevo == Estado.DEVUELTO;
            case DEVUELTO -> false;
        };
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public double getPeso() { return peso; }
    public Estado getEstado() { return estado; }

    @Override
    public String toString() {
        return "Paquete " + id + " (" + descripcion + ") - " + estado + " - " + peso + "kg";
    }
}
