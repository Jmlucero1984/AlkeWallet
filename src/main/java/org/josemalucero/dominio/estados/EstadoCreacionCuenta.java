package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.Registrable;
import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioMonedas;

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
    public void mostrarMenu(ContextoUsuario contextoUsuario) {
        outputProvider.println("Seleccione su tipo de cuenta");
        for(int i =0;i< RepositorioMonedas.getMonedasDB().size();i++) {
            outputProvider.println(""+(i+1)+". Cuenta en "+RepositorioMonedas.getMonedasDB().get(i).getNombre()
                    +" | "+RepositorioMonedas.getMonedasDB().get(i).getCodigo());
        }
        outputProvider.print("Seleccione una opción: ");

    }

    /**
     * Recibe la opción elegida; verifica su validez, esto es, que se encuentre dentro del rango comprendido entre 1 y el número
     * máximo de monedas existentes en el sistema; crea la cuenta, que por defecto es una cuenta regular; le asigna una moneda, la que
     * resulta de la consulta mediante la búsqueda del index en el {@link RepositorioMonedas}; registra la operación de creación de la cuenta en
     * el historial y finalmente cambia al estado {@link EstadoOperaciones}.
     * @param opcion
     * @param contextoUsuario
     */
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

    /**
     * Genera el primer registro del ciclo de vida de la cuenta, especificando el tipo de moneda y el monto incial de 0.00.
     * @param cuentaRegular
     */
    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        String monedaCuenta = cuentaRegular.getMonedaConvertible().getNombre();
        cuentaRegular.registrarOperacion(new RegistroOperacion("Apertura Cuenta Regular en "+monedaCuenta, BigDecimal.ZERO,BigDecimal.ZERO));
    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return "SELECCIÓN TIPO DE CUENTA";
    }


}
