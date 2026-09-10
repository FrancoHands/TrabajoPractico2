public class EnvioEstandar extends Envio {
    public EnvioEstandar(Cliente cliente, Sucursal origen, Sucursal destino, Paquete... paquetes) {
        super(cliente, origen, destino, paquetes);
    }

    @Override
    protected double tarifa() { return 10; }
}
