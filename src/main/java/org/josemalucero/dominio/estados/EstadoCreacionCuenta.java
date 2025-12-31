package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.Registrable;
import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.RepositorioMonedas;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EstadoCreacionCuenta implements EstadoUsuario, Registrable {
    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        System.out.println("Seleccione su tipo de cuenta");
        for(int i =0;i< RepositorioMonedas.getMonedasDB().size();i++) {
            System.out.println(""+(i+1)+". Cuenta en "+RepositorioMonedas.getMonedasDB().get(i).getNombre()
                    +" | "+RepositorioMonedas.getMonedasDB().get(i).getCodigo());
        }
        System.out.print("Seleccione una opción: ");

    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {
        if(opcion<=0 || opcion>RepositorioMonedas.getMonedasDB().size()){
            System.out.println("Opción inválida");
        } else {
            System.out.println("Usted ha seleccionado cuenta en "+RepositorioMonedas.getMonedasDB().get(opcion-1).getNombre());
            contexto.getUsuarioLogueado().crearCuentRegular();
            contexto.getUsuarioLogueado().getCuentaRegular().setMoneda(RepositorioMonedas.getMonedasDB().get(opcion-1));
            registrar(contexto.getUsuarioLogueado().getCuentaRegular());
            contexto.cambiarEstado(new EstadoOperaciones());
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
