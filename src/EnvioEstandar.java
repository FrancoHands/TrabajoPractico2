public class EnvioEstandar extends Envio {
    public EnvioEstandar(Cliente cliente, Paquete paquete) {
        super(cliente, paquete);
    }

    public EnvioEstandar(Cliente cliente, Sucursales origen, Sucursales destino, Paquete paquete) {
        super(cliente, origen, destino, paquete);
    }

    @Override
    protected double tarifa() {
        return 10;
    }
}
