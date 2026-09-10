import java.util.ArrayList;
import java.util.List;

public enum Sucursal {
    VIEDMA("Viedma"),
    BAHIA_BLANCA("Bahía Blanca"),
    BUENOS_AIRES("Buenos Aires"),
    BARILOCHE("Bariloche");

    private final String nombre;
    // Envíos que esta sucursal está procesando en este momento.
    private final List<Envio> envios = new ArrayList<>();

    Sucursal(String nombre) { this.nombre = nombre; }

    // El envío entra bajo responsabilidad de esta sucursal.
    void tomar(Envio envio) { envios.add(envio); }

    // El envío sale y deja de ser responsabilidad de esta sucursal.
    void soltar(Envio envio) { envios.remove(envio); }

    // La sucursal pide la operación; el envío decide cómo llevarla a cabo.
    public void prepararEnvio(Envio envio) {
        exigirQueEsteAca(envio);
        envio.preparar();
    }

    public void despacharEnvio(Envio envio, Sucursal siguiente) {
        exigirQueEsteAca(envio);
        if (siguiente == null || siguiente == this) {
            throw new IllegalArgumentException("La sucursal siguiente debe ser distinta de " + nombre + ".");
        }
        envio.despachar(siguiente);
    }

    // Recibe un envío que está viajando: acá todavía no es responsabilidad de esta sucursal.
    public void recibirEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        envio.recibir(this);
    }

    public void entregarEnvio(Envio envio) {
        exigirQueEsteAca(envio);
        envio.entregar();
    }

    private void exigirQueEsteAca(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        if (!envios.contains(envio)) {
            throw new IllegalStateException("La sucursal " + nombre + " no está procesando ese envío.");
        }
    }

    public List<Envio> getEnvios() { return new ArrayList<>(envios); }

    @Override
    public String toString() { return nombre; }
}
