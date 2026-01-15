package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.Registrable;
import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;

import java.math.BigDecimal;

/** Permite la creación de una cuenta que será asociada al usuario logueado.
 * Implementa la interfaz {@link  Registrable} ya que de todos los estados es el único considerado un punto singular en el historial
 * de operaciones de la propia cuenta.
 * El resto de los eventos subsiguientes en la existencia de una cuenta, son registrados por cada {@code Operacion}.
 *  @author José Maria Lucero
 */

public class EstadoCreacionCuenta extends EstadoUsuario implements Registrable {
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoCreacionCuenta(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Muestra todas las monedas disponibles entre las cuales el usuario puede seleccionar para asociar a la cuenta que se crea.
     * @param contextoUsuario
     */

    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        outputProvider.println(Messages.get("seleccione.tipo.de.cuenta"));
        for(int i =0;i< RepositorioMonedas.getMonedasDB().size();i++) {
            outputProvider.println(""+(i+1)+". "+Messages.get("cuenta.en")+" "+RepositorioMonedas.getMonedasDB()
                    .get(i).getNombre() +" | "+RepositorioMonedas.getMonedasDB().get(i).getCodigo());
        }
        outputProvider.print(Messages.get("seleccione.opcion")+": ");

    }

    /**
     * Recibe la opción elegida; verifica su validez, esto es, que se encuentre dentro del rango comprendido entre 1 y el número
     * máximo de monedas existentes en el sistema; crea la cuenta, que por defecto es una cuenta regular; le asigna una moneda, la que
     * resulta de la consulta mediante la búsqueda del index en el {@link RepositorioMonedas}; registra la operación de creación de la cuenta en
     * el historial y finalmente cambia al estado {@link EstadoOperaciones}.
     * @param opcionStr
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcionStr, ContextoUsuario contextoUsuario) {


        try{
            int opcion=Integer.parseInt(opcionStr);
            if(opcion<=0 || opcion>RepositorioMonedas.getMonedasDB().size()){
                outputProvider.printlnAlert(Messages.get("alerta.opcion.invalida"));
            } else {
                outputProvider.println(Messages.get("usted.ha.seleccionad.cuenta.en")+" "
                        +RepositorioMonedas.getMonedasDB().get(opcion-1).getNombre());
                contextoUsuario.getUsuarioLogueado().crearCuentRegular();
                contextoUsuario.getUsuarioLogueado().getCuentaRegular().setMoneda(RepositorioMonedas
                        .getMonedasDB().get(opcion-1));
                registrar(contextoUsuario.getUsuarioLogueado().getCuentaRegular());
                contextoUsuario.confirmaContinuar();
                contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(),
                        contextoUsuario.getOuputProvider()));
            }
        } catch (NumberFormatException e){
            outputProvider.printlnAlert(Messages.get("alerta.introduzca.opcion.valida"));
        }
    }

    /**
     * Genera el primer registro del ciclo de vida de la cuenta, especificando el tipo de moneda y el monto incial de 0.00.
     * @param cuentaRegular
     */
    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        String monedaCuenta = cuentaRegular.getMonedaConvertible().getNombre();
        cuentaRegular.registrarOperacion(new RegistroOperacion(Messages.get("apertura.cuenta.regular.en")
                +" "+monedaCuenta, BigDecimal.ZERO,BigDecimal.ZERO));
    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.creacion.cuenta");
    }


}
