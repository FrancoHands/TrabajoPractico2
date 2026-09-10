import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroSeguimiento {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Guarda el momento exacto en que ocurrió el acontecimiento.
    private final LocalDateTime fechaHora;
    // Indica en qué sucursal ocurrió el acontecimiento.
    private final Sucursales sucursal;
    // Describe qué ocurrió durante el seguimiento.
    private final String descripcion;
    // Relaciona este registro con el envío correspondiente.
    private final Envio envio;

    public RegistroSeguimiento(Envio envio, Sucursales sucursal, String descripcion) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        this.fechaHora = LocalDateTime.now();
        this.sucursal = sucursal;
        this.descripcion = descripcion;
        this.envio = envio;
    }

    // Devuelve cuándo ocurrió el acontecimiento.
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    // Devuelve dónde ocurrió el acontecimiento.
    public Sucursales getSucursal() {
        return sucursal;
    }

    // Devuelve qué ocurrió durante el acontecimiento.
    public String getDescripcion() {
        return descripcion;
    }

    // Devuelve el envío relacionado con este registro.
    public Envio getEnvio() {
        return envio;
    }

    @Override
    public String toString() {
        return fechaHora.format(FORMATO_FECHA) + " - " + descripcion + " en " + sucursal;
    }
}
