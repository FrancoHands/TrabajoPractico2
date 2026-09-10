import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private String apellido;
    private String direccion;
    private String email;
    private int DNI;
    private int telefono;

    // Guarda todos los envíos que pertenecen a este cliente.
    private final List<Envio> envios = new ArrayList<>();

    // Registra un envío y evita agregar dos veces el mismo objeto.
    public void agregarEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        if (!envios.contains(envio)) {
            envios.add(envio);
        }
    }

    // Devuelve una copia de los envíos para proteger la lista interna.
    public List<Envio> getEnvios() {
        return new ArrayList<>(envios);
    }
}

