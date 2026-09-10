public class EnvioExpress extends Envio {
    public EnvioExpress(Cliente cliente, Paquete paquete) {
        super(cliente, paquete);
    }

    public EnvioExpress(Cliente cliente, Sucursales origen, Sucursales destino, Paquete paquete) {
        super(cliente, origen, destino, paquete);
    }

    @Override
    protected double tarifa() {
        return 15;
    }
}

