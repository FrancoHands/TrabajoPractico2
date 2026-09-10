import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RegistroSeguimiento {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final LocalDateTime fechaHora = LocalDateTime.now();
    private final Sucursal sucursal;
    private final String descripcion;

    RegistroSeguimiento(Sucursal sucursal, String descripcion) {
        if (sucursal == null) {
            throw new IllegalArgumentException("El movimiento necesita una sucursal.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("El movimiento necesita una descripción.");
        }
        this.sucursal = sucursal;
        this.descripcion = descripcion;
    }

    Sucursal getSucursal() { return sucursal; }

    @Override
    public String toString() {
        return fechaHora.format(FORMATO_FECHA) + " - " + descripcion + " en " + sucursal;
    }
}
