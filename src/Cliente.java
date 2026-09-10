import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private final String nombre;
    private final String dni;
    private final String telefono;
    // Un cliente puede solicitar varios envíos a lo largo del tiempo.
    private final List<Envio> envios = new ArrayList<>();

    public Cliente(String nombre, String dni) {
        this(nombre, dni, "Sin teléfono");
    }

    public Cliente(String nombre, String dni, String telefono) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El cliente necesita un nombre.");
        }
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El cliente necesita un DNI.");
        }
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
    }

    void agregarEnvio(Envio envio) {
        envios.add(envio);
    }

    public List<Envio> getEnvios() { return new ArrayList<>(envios); }

    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }

    @Override
    public String toString() { return nombre + " (DNI " + dni + ")"; }
}
