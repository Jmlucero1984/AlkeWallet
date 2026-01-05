package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.Registrable;
import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioMonedas;

import java.math.BigDecimal;


public class EstadoCreacionCuenta extends EstadoUsuario implements Registrable {
    public EstadoCreacionCuenta(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        outputProvider.println("Seleccione su tipo de cuenta");
        for(int i =0;i< RepositorioMonedas.getMonedasDB().size();i++) {
            outputProvider.println(""+(i+1)+". Cuenta en "+RepositorioMonedas.getMonedasDB().get(i).getNombre()
                    +" | "+RepositorioMonedas.getMonedasDB().get(i).getCodigo());
        }
        outputProvider.print("Seleccione una opción: ");

    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {
        if(opcion<=0 || opcion>RepositorioMonedas.getMonedasDB().size()){
            outputProvider.println("Opción inválida");
        } else {
            outputProvider.println("Usted ha seleccionado cuenta en "+RepositorioMonedas.getMonedasDB().get(opcion-1).getNombre());
            contextoUsuario.getUsuarioLogueado().crearCuentRegular();
            contextoUsuario.getUsuarioLogueado().getCuentaRegular().setMoneda(RepositorioMonedas.getMonedasDB().get(opcion-1));
            registrar(contextoUsuario.getUsuarioLogueado().getCuentaRegular());
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        }
    }

    @Override
    public String getNombreEstado() {
        return "SELECCIÓN TIPO DE CUENTA";
    }

    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        String monedaCuenta = cuentaRegular.getMonedaConvertible().getNombre();
        cuentaRegular.registrarOperacion(new RegistroOperacion("Apertura Cuenta Regular en "+monedaCuenta, BigDecimal.ZERO,BigDecimal.ZERO));
    }
}
