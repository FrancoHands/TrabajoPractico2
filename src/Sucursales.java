import java.util.ArrayList;
import java.util.List;

public class Sucursales {
    // Sucursales disponibles para procesar los envíos.
    public static final Sucursales VIEDMA = new Sucursales("Viedma");
    public static final Sucursales BAHIA_BLANCA = new Sucursales("Bahía Blanca");
    public static final Sucursales BUENOS_AIRES = new Sucursales("Buenos Aires");
    public static final Sucursales BARILOCHE = new Sucursales("Bariloche");

    private final String nombre;
    private final List<Envio> envios = new ArrayList<>();

    private Sucursales(String nombre) {
        this.nombre = nombre;
    }

    // Registra un envío que está siendo procesado en esta sucursal.
    void agregarEnvio(Envio envio) {
        if (!envios.contains(envio)) {
            envios.add(envio);
        }
    }

    // Deja de registrar un envío cuando sale de esta sucursal.
    void quitarEnvio(Envio envio) {
        envios.remove(envio);
    }

    // Recibe un envío y le pide que actualice su sucursal actual.
    public void recibirEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        if (envio.getSucursalActual() != this) {
            throw new IllegalStateException("El envío se encuentra en otra sucursal.");
        }
        if (envios.contains(envio)) {
            throw new IllegalStateException("El envío ya fue recibido en esta sucursal.");
        }
        envio.recibirEnSucursal(this);
    }

    // Despacha un envío hacia otra sucursal indicada por quien realiza la operación.
    public void despacharEnvio(Envio envio, Sucursales siguienteSucursal) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        if (siguienteSucursal == null) {
            throw new IllegalArgumentException("La siguiente sucursal no puede ser nula.");
        }
        if (!envios.contains(envio)) {
            throw new IllegalStateException("La sucursal no está procesando este envío.");
        }
        if (siguienteSucursal == this) {
            throw new IllegalStateException("El envío ya se encuentra en esta sucursal.");
        }
        envio.despacharASucursal(siguienteSucursal);
    }

    // Devuelve el nombre de la sucursal.
    public String getNombre() {
        return nombre;
    }

    // Devuelve una copia de los envíos que están en esta sucursal.
    public List<Envio> getEnvios() {
        return new ArrayList<>(envios);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
