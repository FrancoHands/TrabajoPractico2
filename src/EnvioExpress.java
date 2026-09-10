public class EnvioExpress extends Envio {
    public EnvioExpress(Cliente cliente, Sucursal origen, Sucursal destino, Paquete... paquetes) {
        super(cliente, origen, destino, paquetes);
    }

    @Override
    protected double tarifa() { return 15; }
}
