public class EnvioInternacional extends Envio {
    public EnvioInternacional(Cliente cliente, Paquete paquete) {
        super(cliente, paquete);
    }

    public EnvioInternacional(Cliente cliente, Sucursales origen, Sucursales destino, Paquete paquete) {
        super(cliente, origen, destino, paquete);
    }

    @Override
    protected double tarifa() {
        return 20;
    }
}
