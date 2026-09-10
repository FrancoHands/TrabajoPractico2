import java.util.ArrayList;
import java.util.List;

// Registra los envíos de la jornada y responde las consultas generales.
public class GestorEnvios {
    private final List<Envio> envios = new ArrayList<>();

    public void registrar(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        envios.add(envio);
    }

    // Impide operar sobre un envío que no existe.
    public Envio buscarPorId(int id) {
        for (Envio envio : envios) {
            if (envio.getId() == id) {
                return envio;
            }
        }
        throw new IllegalArgumentException("No existe ningún envío con id " + id + ".");
    }

    public int cantidad() { return envios.size(); }

    public int cantidadEntregados() {
        int entregados = 0;
        for (Envio envio : envios) {
            if (envio.estaEntregado()) {
                entregados++;
            }
        }
        return entregados;
    }

    public double facturacionTotal() {
        double total = 0;
        for (Envio envio : envios) {
            total += envio.getCosto();
        }
        return total;
    }

    public double costoPromedio() {
        return envios.isEmpty() ? 0 : facturacionTotal() / envios.size();
    }

    public List<Envio> getEnvios() { return new ArrayList<>(envios); }
}
