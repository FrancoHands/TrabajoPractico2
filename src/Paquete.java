public class Paquete {
    private int id;
    private String descripcion;
    private double peso;
    private String estado;

    public Paquete(int id, String descripcion, double peso) {
    	this.id = id;
        this.descripcion = descripcion;
        this.peso = peso;
        this.estado = "Recibido";
    }

    public Paquete(int id, double peso) {
        this(id, "Sin descripción", peso);
    }

    public void prepararParaEnvio() {
        if (!estado.equals("Recibido")) {
            throw new IllegalStateException("El paquete debe estar en estado 'Recibido' para prepararse.");
        }
        this.estado = "En preparación";
    }

    public void marcarEnDistribucion() {
        if (!estado.equals("En preparación")) {
            throw new IllegalStateException("El paquete debe estar en 'En preparación' para pasar a distribución.");
        }
        this.estado = "En distribución";
    }

    public void entregar() {
        if (!estado.equals("En distribución")) {
            throw new IllegalStateException("El paquete debe estar en distribución para ser entregado.");
        }
        this.estado = "Entregado";
    }

    public void devolver() {
        if (!estado.equals("Entregado")) {
            throw new IllegalStateException("Solo se puede devolver un paquete que fue entregado.");
        }
        this.estado = "Devuelto";
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public double getPeso() { return peso; }
    public String getEstado() { return estado; }
}
