public class EnvioInternacional extends Envio {
    public EnvioInternacional(Cliente cliente, Sucursal origen, Sucursal destino, Paquete... paquetes) {
        super(cliente, origen, destino, paquetes);
    }

    @Override
    protected double tarifa() { return 20; }
}
