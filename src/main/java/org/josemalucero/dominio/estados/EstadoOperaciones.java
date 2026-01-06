package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.cuenta.Transferible;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.operacion.*;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.FormateadorDeRegistroAImprimir;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Gestiona en gran medida las principales acciones sobre la cuenta. Dispone de métodos propios para
 * gestionar cada implementacion concreta de la clase abstracta {@link Operacion} o derivar en otros estados dependiendo
 * de la complejidad de la operación.
 *
 @author José Maria Lucero
 */

public class EstadoOperaciones extends EstadoUsuario {


    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoOperaciones(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**Muestra las operaciones disponibles sobre la cuenta. Siendo un punto central de la interacción durante el ciclo
     * de vida de la actividad del usuario, el mismo representa un punto de rigidez de la aplicación.
     *
     * @param contextoUsuario
     */

    @Override
    public void mostrarMenu(ContextoUsuario contextoUsuario) {
        outputProvider.println("Bienvenido, " + contextoUsuario.getUsuarioLogueado().getNombreCompleto());
        outputProvider.println("1. Consultar datos cuenta");
        outputProvider.println("2. Consultar saldo");
        outputProvider.println("3. Depositar dinero");
        outputProvider.println("4. Retirar dinero");
        outputProvider.println("5. Transferir dinero");
        outputProvider.println("6. Consultar conversión entre monedas");
        outputProvider.println("7. Convertir cuenta a otra moneda");
        outputProvider.println("8. Ver historial de transacciones");
        outputProvider.println("9. Cerrar sesión (Sign Out)");
        outputProvider.print("Seleccione una opción: ");
    }

    /**
     * Maneja mediante un {@code switch} con el número de opción elegida como entrada, las diferentes
     * operaciones que se pueden realizar con una cuenta ya creada para un usuario existente y logueado.
     * @param opcion
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {
        switch (opcion) {
            case 1:
                consultarDatosCuenta(contextoUsuario);
                break;
            case 2:
                consultarSaldo(contextoUsuario);
                break;

            case 3:
                depositarDinero(contextoUsuario);
                break;

            case 4:
                retirarDinero(contextoUsuario);
                break;

            case 5:
                transferirDinero(contextoUsuario);
                break;

            case 6:
                consultarConversionMoneda(contextoUsuario);
                break;
            case 7:
                convertirCuentaAOtraMoneda(contextoUsuario);
                break;

            case 8:
                verHistorial(contextoUsuario);
                break;

            case 9:
                outputProvider.println("Cerrando sesión...");
                contextoUsuario.cerrarSesion();
                break;

            default:
                outputProvider.println("Opción inválida");
        }
    }


    /**
     * Crea una {@link OperacionConsulta} y la ejecuta, mostrando como salida mediante un {@link OutputProvider}el saldo correpondiente
     * a la cuenta asociada al usuario logueado.
     * @param contextoUsuario
     */
    private void consultarSaldo(ContextoUsuario contextoUsuario) {
        OperacionConsulta operacionConsulta = new OperacionConsulta(contextoUsuario.getUsuarioLogueado().getCuentaRegular(),outputProvider);
        operacionConsulta.ejecutar();
    }

    /**
     * Crea una {@link OperacionDeposito}. La misma requiere de una cifra verificada. Se {@code prevalida} la operación, luego se {@code ejecuta} y
     * finalmente se {@code postvalida}. De ser exitoso el resultado final, se procede a registrar la operacion mediante {@link RegistroOperacion}.
     * @param contextoUsuario
     */
    private void depositarDinero(ContextoUsuario contextoUsuario) {
        outputProvider.println("DEPOSITAR EN CUENTA");
        Optional<BigDecimal> cifraVerificada;
        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
            if(cifraVerificada.isEmpty()){
                return;
            } else {
                OperacionDeposito operacionDeposito = new OperacionDeposito(cuentaRegular,cifraVerificada.get(),outputProvider);
                if (operacionDeposito.preValidar()) {
                    operacionDeposito.ejecutar();
                    if(operacionDeposito.postValidar()){
                        operacionExitosa = true;
                        operacionDeposito.registrar(cuentaRegular);
                        outputProvider.println("DEPOSITO REALIZADO");
                    }

                }
            }
        }
    }

    /**
     * Muestra por medio del correspondiente {@link OutputProvider} el nombre completo del usuario logueado, el detalle
     * del tipo de moneda asociado a la cuenta del usuario y el N° Cuenta, necesario para la {@link OperacionTransferencia}.
     * @param contextoUsuario
     */
    private void consultarDatosCuenta(ContextoUsuario contextoUsuario){
        outputProvider.println(contextoUsuario.getUsuarioLogueado().getNombreCompleto());
        outputProvider.println("Cuenta en "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible().getNombre());
        outputProvider.println("N° Cuenta: "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta());
    }

    /**
     * Cambia al estado {@link OperacionDeConversionDeCuenta} para convertir la cuenta actual, esto es, cambiar la moneda asociada a otra designada por
     * el usuario y la correspondiente conversión del saldo actual.
     * @param contextoUsuario
     */
    private void convertirCuentaAOtraMoneda(ContextoUsuario contextoUsuario){
        contextoUsuario.cambiarEstado(new EstadoConversionCuenta(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
    }

    /**
     * Cambia al estado {@link EstadoConversionMonedas} para hacer consultas de equivalencias de importes entre monedas diferentes, sin afectar la
     * cuenta actual, tanto en su moneda como en su saldo.
     * @param contextoUsuario
     */
    private void consultarConversionMoneda(ContextoUsuario contextoUsuario){
        contextoUsuario.cambiarEstado(new EstadoConversionMonedas(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
    }

    /**
     * Crea una {@link OperacionRetiro}. La misma requiere de una cifra verificada. Se {@code prevalida} la operación, luego se {@code ejecuta} y
     * finalmente se {@code postvalida}. De ser exitoso el resultado final, se procede a registrar la operacion mediante {@link RegistroOperacion}.
     * @param contextoUsuario
     */

    private void retirarDinero(ContextoUsuario contextoUsuario) {
        outputProvider.println("RETIRAR DE CUENTA");
        Optional<BigDecimal> cifraVerificada;
        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
            if(cifraVerificada.isEmpty()){
                return;
            } else  {
                OperacionRetiro operacionRetiro = new OperacionRetiro(cuentaRegular,cifraVerificada.get(),outputProvider);
                if (operacionRetiro.preValidar()){
                    operacionRetiro.ejecutar();
                    if(operacionRetiro.postValidar()) {
                        operacionExitosa = true;
                        operacionRetiro.registrar(cuentaRegular);
                        outputProvider.println("RETIRO REALIZADO");
                    }
                }
            }
        }
    }

    /**
     * Busca un {@link Usuario} por su número de cuenta mediante {@link #manejarEntradaDeNumeroCuenta(InputProvider)} y luego de verificar que
     * efectivamente corresponde a un usuario existente con cuenta continua el proceso de transferencia con {@link #operarSobreCuentaParaTransferir(ContextoUsuario, Usuario)}
     * @param contextoUsuario
     */

    private void transferirDinero(ContextoUsuario contextoUsuario) {
        outputProvider.println("TRANSFERIR A CUENTA");
        Optional<Usuario> usuarioDestino = manejarEntradaDeNumeroCuenta(contextoUsuario.getConsoleInputProvider());
        if(usuarioDestino!=null && !usuarioDestino.isEmpty()){
            operarSobreCuentaParaTransferir(contextoUsuario,usuarioDestino.get());
        }
    }

    /**
     * Muestra a quien pertenece la cuenta. Si la cuenta obtenida por el N° Cuenta pertenece al mismo usuario, muestra un mensaje y se termina
     * la operación. Si no es el caso, pero aun así la cuenta no implementa la interfaz {@link Transferible}, se informa con un mensaje y se
     * termina la operación. Finalmente, de darse las condiciones necesarias, se elige el tipo de transferencia, se ingresa el monto de la misma.
     * Se {@code prevalida} la operación, luego se {@code ejecuta} y finalmente se {@code postvalida}. De ser exitoso el resultado final,
     * se procede a registrar la operacion mediante {@link RegistroOperacion}
     *
     * @param contextoUsuario
     * @param usuarioDestino
     */
    private void operarSobreCuentaParaTransferir(ContextoUsuario contextoUsuario,Usuario usuarioDestino){
        outputProvider.println("La cuenta destino pertenece a: " + usuarioDestino.getNombreCompleto());
        CuentaRegular cuentaRegular =contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        outputProvider.println("");
        if(usuarioDestino.equals(contextoUsuario.getUsuarioLogueado())){
            outputProvider.println("|||| No le parece sin sentido transferirse a usted mismo? ||||");
            return;
        }

        if (!(usuarioDestino.getCuentaRegular() instanceof Transferible)) {
            outputProvider.println("La cuenta destino no puede recibir transferencias");
            return;
        }
        TipoTransferencia tipoTransferencia =obtenerTipoTransferencia(contextoUsuario,usuarioDestino.getCuentaRegular());
        if (tipoTransferencia==null) return;

        Optional<BigDecimal> cifraVerificada= Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
        if(cifraVerificada==null) {return;}

        DatosTransferencia datosTransferencia = new DatosTransferencia(cuentaRegular,usuarioDestino.getCuentaRegular(),cifraVerificada.get(), new ConversorMoneda());
        OperacionTransferencia operacionTransferencia = obtenerOperacionTransferenciaEspecífica(tipoTransferencia,datosTransferencia,outputProvider);

        if (operacionTransferencia.preValidar() ){
            operacionTransferencia.ejecutar();
            if(operacionTransferencia.postValidar()) {
                operacionTransferencia.registrar(cuentaRegular);
                outputProvider.println("TRANSFERENCIA REALIZADA");
            } else {
                operacionTransferencia.restaurarEstadoAnterior();
            }
        }
    }

    /**
     * Verifica si los tipos de monedas de la cuenta de origen y de destino coinciden. Si así lo es, retorna {@code TipoTransferencia.IGUAL_MONEDA},
     * en caso contrario, se da la posibilidad al usuario de elegir si prefiere hacer una transferencia por el monto ingresado sobre la base de
     * la moneda actual de la cuenta, o en la moneda de la cuenta destino. En ese caso se retorna {@code TipoTransferencia.MONEDA_ORIGEN} o
     * {@code TipoTransferencia.MONEDA_DESTINO} según el caso.
     * @param contextoUsuario
     * @param cuentaDestino
     * @return {@link TipoTransferencia} Enum que identifica el tipo de transferencia a realizar.
     */
    private TipoTransferencia obtenerTipoTransferencia(ContextoUsuario contextoUsuario,CuentaRegular cuentaDestino){
        CuentaRegular cuentaOrigen = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        if(!cuentaDestino.getMonedaConvertible().getCodigo().equals(cuentaOrigen.getMonedaConvertible().getCodigo())){
            outputProvider.println("La cuenta destino está en una moneda diferente ("+cuentaOrigen.getMonedaConvertible().getCodigo()+")");
            String[] opciones = new String[]{"Monto en moneda de su propia cuenta","Monto en moneda de la cuenta destino"};
            String titulo = "Seleccione opción para transferencia entre cuentas";
            int eleccion = seleccionMultipleGenerica(contextoUsuario.getConsoleInputProvider(),titulo, opciones);
            switch (eleccion){
                case 1: return TipoTransferencia.MONEDA_ORIGEN;
                case 2: return  TipoTransferencia.MONEDA_DESTINO;
            }
        }
        return TipoTransferencia.IGUAL_MONEDA;

    }

    /**
     * Devuelve una determinada instancia de {@link OperacionTransferencia} o subclases {@link OperacionTransferenciaMonedaOrigen} y
     * {@link OperacionTransferenciaMonedaDestino}  creadas con los datos propios de la operación requerida.
     * @param tipoTransferencia
     * @param datosTransferencia
     * @param outputProvider
     * @return {@link OperacionTransferencia} que representa la operación de transferencia determinada por el {@link TipoTransferencia}
     */

    private OperacionTransferencia obtenerOperacionTransferenciaEspecífica(TipoTransferencia tipoTransferencia,DatosTransferencia datosTransferencia, OutputProvider outputProvider){
        switch (tipoTransferencia) {
            case TipoTransferencia.IGUAL_MONEDA -> {
                return new OperacionTransferencia(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),outputProvider);
            }
            case TipoTransferencia.MONEDA_ORIGEN -> {
                return new OperacionTransferenciaMonedaOrigen(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),new ConversorMoneda(),outputProvider);
            }
            case TipoTransferencia.MONEDA_DESTINO -> {
                return new OperacionTransferenciaMonedaDestino(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),new ConversorMoneda(),outputProvider);
            }
        };
        return null;
    }

    /**
     * Obtiene el registro histórico de operaciones de la cuenta actual del usuario. Presenta todos los registros mediante el {@link OutputProvider},
     * generando en primera instancia las cabeceras con los titulos de cada columna, según un {@link  org.josemalucero.servicio.FormateadorDeRegistroAImprimir.Alineado}
     * específico y a continuación cada operación histórica en orden temporal descendente.
     * @param contextoUsuario
     */

    private void verHistorial(ContextoUsuario contextoUsuario) {
        outputProvider.println("Mostrando historial...");
        ArrayList<RegistroOperacion> operacionesHistoricas = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getHistorialOperaciones();
        outputProvider.println(FormateadorDeRegistroAImprimir.generarCabeceras(FormateadorDeRegistroAImprimir.Alineado.CENTRO));
        operacionesHistoricas.forEach(t->outputProvider.println(FormateadorDeRegistroAImprimir.formatearRegistro(t, FormateadorDeRegistroAImprimir.Alineado.CENTRO)));

    }

    /**
     * Provee una forma sencilla de presentar un título, una serie de opciones con un indice asociado sobre las cuales se puede
     * elegir, o retornar la negativa a la posibilidad mencionada.
     * @param consoleInputProvider
     * @param titulo
     * @param opciones
     * @return {@code int} que representa el índice de una determinada elección o un valor -1 para una operación particular no especificada en las opciones.
     */
    private int seleccionMultipleGenerica(InputProvider consoleInputProvider,String titulo,String[] opciones){
        while(true) {
            outputProvider.println(titulo+" | ESC para salir.");
            for(int i=0;i<opciones.length;i++){
                outputProvider.println((i+1)+". "+opciones[i]);
            }
            String textoIntroducido = consoleInputProvider.leerOpcionString();
            if (textoIntroducido.equalsIgnoreCase("ESC")) return -1;
            int opcion;
            try {
                opcion =  Integer.parseInt(textoIntroducido);
                if(opcion>0 && opcion<opciones.length+1){
                    return opcion;
                } else {
                    outputProvider.println("Opción invalida");
                }
            } catch (NumberFormatException e) {
                outputProvider.println("Opción invalida");
            }
        }
    }

    /**
     * Obtiene un {@link Usuario} por el N° de Cuenta ingresado mediante {@link InputProvider}.
     * @param consoleInputProvider
     * @return {@link Optional<Usuario>} si logra encontrar un usuario o {@code null} en caso contrario o si se
     * cancela la operación.
     */

    private Optional<Usuario> manejarEntradaDeNumeroCuenta(InputProvider consoleInputProvider){
        boolean cuentaValida = false;
        while(!cuentaValida){
            outputProvider.println("Introducir numero de cuenta destino | ESC para salir.");
            String textoIntroducido = consoleInputProvider.leerOpcionString();
            if(textoIntroducido.equalsIgnoreCase("ESC")) return null;
            Optional<Usuario> usuario = RepositorioUsuarios.consultarUsuarioPorCuenta(textoIntroducido);
            if (usuario.isPresent()) {
                outputProvider.println("USUARIO ENCONTRADO");
                outputProvider.println(usuario.get().getNombreCompleto());
                return Optional.of(usuario.get());
            }
            outputProvider.println("No existe el numero de cuenta, intente nuevamente...");
        }
        return  null;
    }

    /**
     * Permite al usurio introducir una cantidad monetaria, validada mediante REGEX, o la negativa de continuar
     * con la operación.
     * @param consoleInputProvider
     * @return {@link BigDecimal} que representa el monto ingresado por medio del {@link InputProvider}
     */

    private BigDecimal manejarEntradaDeCifraMonetaria(InputProvider consoleInputProvider) {
        boolean cantidadVálida = false;
        while(!cantidadVálida){
            outputProvider.println("Introducir cantidad con enteros y centavos $$$.$$ | ESC para salir.");
            String cantidadIntroducida = consoleInputProvider.leerOpcionString();
            if(cantidadIntroducida.equalsIgnoreCase("ESC")) return null;
            cantidadVálida=cantidadIntroducida.matches("^\\d+\\.\\d{2}$");
            if (cantidadVálida) {
                return new BigDecimal(cantidadIntroducida);
            }
            outputProvider.println("Cantidad inválida, intente nuevamente...");
        }
        return  null;
    }


    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return "OPERACIONES";
    }

}