package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.RepositorioMonedas;

import java.util.ArrayList;

public class EstadoCreacionCuenta implements EstadoUsuario{
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
            contexto.cambiarEstado(new EstadoOperaciones());
        }

    }

    @Override
    public String getNombreEstado() {
        return "SELECCIÖN TIPO DE CUENTA";
    }
}
