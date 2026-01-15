package org.josemalucero.dominio.estado;

import org.josemalucero.servicio.formatters.FormateadorDeRegistroAImprimir;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.cuenta.Transferible;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.operacion.*;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;

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
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {

        outputProvider.println(Messages.get("bienvenido")+", " + contextoUsuario.getUsuarioLogueado().getNombreCompleto());
        outputProvider.print("\n");
        outputProvider.printMenu(
            "1. "+Messages.get("opcion.consultar.datos.cuenta")+"\n"+
            "2. "+Messages.get("opcion.consultar.saldo")+"\n"+
            "3. "+Messages.get("opcion.depositar.dinero")+"\n"+
            "4. "+Messages.get("opcion.retirar.dinero")+"\n"+
            "5. "+Messages.get("opcion.transferir.dinero")+"\n"+
            "6. "+Messages.get("opcion.consultar.conversion.monedas")+"\n"+
            "7. "+Messages.get("opcion.convertir.cuenta")+"\n"+
            "8. "+Messages.get("opcion.ver.historial")+"\n"+
            "9. "+Messages.get("opcion.cerrar.sesion")+"\n"+
                    Messages.get("seleccione.opcion")+": "
        );
    }

    /**
     * Maneja mediante un {@code switch} con el número de opción elegida como entrada, las diferentes
     * operaciones que se pueden realizar con una cuenta ya creada para un usuario existente y logueado.
     * @param opcionStr
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcionStr, ContextoUsuario contextoUsuario) {
        try {
            int opcion = Integer.parseInt(opcionStr);
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
                    outputProvider.println(Messages.get("cerrando.sesion"));
                    contextoUsuario.cerrarSesion();
                    break;

                default:
                    outputProvider.printlnAlert(Messages.get("alerta.opcion.invalida"));
            }
        } catch (NumberFormatException e) {
            outputProvider.printlnAlert(Messages.get("alerta.introduzca.opcion.valida"));
        }
    }

    /**
     * Muestra por medio del correspondiente {@link OutputProvider} el nombre completo del usuario logueado, el detalle
     * del tipo de moneda asociado a la cuenta del usuario y el N° Cuenta, necesario para la {@link OperacionTransferencia}.
     * @param contextoUsuario
     */
    private void consultarDatosCuenta(ContextoUsuario contextoUsuario){
        outputProvider.println("\n"+contextoUsuario.getUsuarioLogueado().getNombreCompleto());
        outputProvider.println(Messages.get("cuenta.en")+" "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible().getNombre());
        outputProvider.println(Messages.get("n.cuenta")+": "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta());
        contextoUsuario.confirmaContinuar();

    }

    /**
     * Crea una {@link OperacionConsulta} y la ejecuta, mostrando como salida mediante un {@link OutputProvider}el saldo correpondiente
     * a la cuenta asociada al usuario logueado.
     * @param contextoUsuario
     */
    private void consultarSaldo(ContextoUsuario contextoUsuario) {
        OperacionConsulta operacionConsulta = new OperacionConsulta(contextoUsuario.getUsuarioLogueado().getCuentaRegular(),outputProvider);
        operacionConsulta.ejecutar();
        contextoUsuario.confirmaContinuar();
    }

    /**
     * Crea una {@link OperacionDeposito}. La misma requiere de una cifra verificada. Se {@code prevalida} la operación, luego se {@code ejecuta} y
     * finalmente se {@code postvalida}. De ser exitoso el resultado final, se procede a registrar la operacion mediante {@link RegistroOperacion}.
     * @param contextoUsuario
     */
    private void depositarDinero(ContextoUsuario contextoUsuario) {
        if(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getCantidad_depositos_historicos()>=ConstantesFiscalesBancarias.LIMITE_DEPOSITOS_POR_CUENTA){
            outputProvider.println("\n"+Messages.get("alerta.limite.depositos.por.cuenta"));

            contextoUsuario.confirmaContinuar();
        } else if(contextoUsuario.getDepositos_por_sesion()>=ConstantesFiscalesBancarias.LIMITE_DEPOSITOS_POR_SESION){
            outputProvider.println("\n"+Messages.get("alerta.limite.depositos.por.sesion"));
            contextoUsuario.confirmaContinuar();
        } else {
            contextoUsuario.cambiarEstado(new EstadoDeposito(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        }
    }

    /**
     * Crea una {@link OperacionRetiro}. La misma requiere de una cifra verificada. Se {@code prevalida} la operación, luego se {@code ejecuta} y
     * finalmente se {@code postvalida}. De ser exitoso el resultado final, se procede a registrar la operacion mediante {@link RegistroOperacion}.
     * @param contextoUsuario
     */

    private void retirarDinero(ContextoUsuario contextoUsuario) {
        if(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getCantidad_retiros_historicos()>=ConstantesFiscalesBancarias.LIMITE_RETIROS_POR_CUENTA){
            outputProvider.println("\n"+Messages.get("alerta.limite.retiros.por.cuenta"));

            contextoUsuario.confirmaContinuar();
        } else if(contextoUsuario.getRetiros_por_sesion()>=ConstantesFiscalesBancarias.LIMITE_RETIROS_POR_SESION){
            outputProvider.println("\n"+Messages.get("alerta.limite.retiros.por.sesion"));
            contextoUsuario.confirmaContinuar();
        } else {
            contextoUsuario.cambiarEstado(new EstadoRetiro(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        }
    }

    /**
     * Busca un {@link Usuario} por su número de cuenta mediante {@link #manejarEntradaDeNumeroCuenta(InputProvider)} y luego de verificar que
     * efectivamente corresponde a un usuario existente con cuenta continua el proceso de transferencia con {@link #operarSobreCuentaParaTransferir(ContextoUsuario, Usuario)}
     * @param contextoUsuario
     */

    private void transferirDinero(ContextoUsuario contextoUsuario) {
        if(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getCantidad_transferencias_historicas()>=ConstantesFiscalesBancarias.LIMITE_TRANSFERENCIAS_POR_CUENTA){
            outputProvider.println("\n"+Messages.get("alerta.limite.transferencias.por.cuenta"));

            contextoUsuario.confirmaContinuar();
        } else if(contextoUsuario.getTransferencias_por_sesion()>=ConstantesFiscalesBancarias.LIMITE_TRANSFERENCIAS_POR_SESION){
            outputProvider.println("\n"+Messages.get("alerta.limite.transferencias.por.sesion"));
            contextoUsuario.confirmaContinuar();
        } else {
            contextoUsuario.cambiarEstado(new EstadoTransferencias(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        }

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
     * Cambia al estado {@link OperacionDeConversionDeCuenta} para convertir la cuenta actual, esto es, cambiar la moneda asociada a otra designada por
     * el usuario y la correspondiente conversión del saldo actual.
     * @param contextoUsuario
     */
    private void convertirCuentaAOtraMoneda(ContextoUsuario contextoUsuario){
        contextoUsuario.cambiarEstado(new EstadoConversionCuenta(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
    }

    /**
     * Obtiene el registro histórico de operaciones de la cuenta actual del usuario. Presenta todos los registros mediante el {@link OutputProvider},
     * generando en primera instancia las cabeceras con los titulos de cada columna, según un {@link  FormateadorDeRegistroAImprimir.Alineado}
     * específico y a continuación cada operación histórica en orden temporal descendente.
     * @param contextoUsuario
     */

    private void verHistorial(ContextoUsuario contextoUsuario) {
        outputProvider.println("\n"+Messages.get("mostrando.historial")+"\n");
        ArrayList<RegistroOperacion> operacionesHistoricas = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getHistorialOperaciones();
        outputProvider.println(FormateadorDeRegistroAImprimir.generarCabeceras(FormateadorDeRegistroAImprimir.Alineado.CENTRO));
        operacionesHistoricas.forEach(t->outputProvider.println(FormateadorDeRegistroAImprimir.formatearRegistro(t, FormateadorDeRegistroAImprimir.Alineado.CENTRO)));
        contextoUsuario.confirmaContinuar();
    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.operaciones");
    }





    /*
    ------------------------->  FUNCIONES OBSOLETAS DESDE LA CREACION DEL ESTADO <EstadoTransferencias>    <--------------------

     */


    /**

     * Muestra a quien pertenece la cuenta. Si la cuenta obtenida por el N° Cuenta pertenece al mismo usuario, muestra un mensaje y se termina
     * la operación. Si no es el caso, pero aun así la cuenta no implementa la interfaz {@link Transferible}, se informa con un mensaje y se
     * termina la operación. Finalmente, de darse las condiciones necesarias, se elige el tipo de transferencia, se ingresa el monto de la misma.
     * Se {@code prevalida} la operación, luego se {@code ejecuta} y finalmente se {@code postvalida}. De ser exitoso el resultado final,
     * se procede a registrar la operacion mediante {@link RegistroOperacion}
     * @param contextoUsuario
     * @param usuarioDestino
     *
     * @deprecated Desde que se trasladó toda la funcionalidad de transferencias a su propio EstadoTransferencias
     */
    @Deprecated
    private void operarSobreCuentaParaTransferir(ContextoUsuario contextoUsuario,Usuario usuarioDestino){
        outputProvider.println("La cuenta destino pertenece a: " + usuarioDestino.getNombreCompleto());
        CuentaRegular cuentaRegular =contextoUsuario.getUsuarioLogueado().getCuentaRegular();

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

        BigDecimal cifraVerificada= manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider());
        if(cifraVerificada==null) return;

        DatosTransferencia datosTransferencia = new DatosTransferencia(cuentaRegular,usuarioDestino.getCuentaRegular(),cifraVerificada, new ConversorMoneda());
        OperacionTransferencia operacionTransferencia = obtenerOperacionTransferenciaEspecífica(tipoTransferencia,datosTransferencia,outputProvider);

        if (operacionTransferencia.preValidar() ){
            operacionTransferencia.ejecutar();
            if(operacionTransferencia.postValidar()) {
                operacionTransferencia.registrar(cuentaRegular);
                outputProvider.println("TRANSFERENCIA REALIZADA");
                contextoUsuario.confirmaContinuar();
            } else {
                operacionTransferencia.restaurarEstadoAnterior();
                contextoUsuario.confirmaContinuar();
            }
        } else {
            contextoUsuario.confirmaContinuar();
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
     * @deprecated Desde que se trasladó toda la funcionalidad de transferencias a su propio EstadoTransferencias
     */
    @Deprecated
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
        } else {
            return  TipoTransferencia.IGUAL_MONEDA;
        }
        return null;

    }

    /**
     * Devuelve una determinada instancia de {@link OperacionTransferencia} o subclases {@link OperacionTransferenciaMonedaOrigen} y
     * {@link OperacionTransferenciaMonedaDestino}  creadas con los datos propios de la operación requerida.
     * @param tipoTransferencia
     * @param datosTransferencia
     * @param outputProvider
     * @return {@link OperacionTransferencia} que representa la operación de transferencia determinada por el {@link TipoTransferencia}
     * @deprecated Desde que se trasladó toda la funcionalidad de transferencias a su propio EstadoTransferencias
     */
    @Deprecated
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
     * Provee una forma sencilla de presentar un título, una serie de opciones con un indice asociado sobre las cuales se puede
     * elegir, o retornar la negativa a la posibilidad mencionada.
     * @param consoleInputProvider
     * @param titulo
     * @param opciones
     * @return {@code int} que representa el índice de una determinada elección o un valor -1 para una operación particular no especificada en las opciones.
     @deprecated Desde que se trasladó toda la funcionalidad de transferencias a su propio EstadoTransferencias
     */
    @Deprecated
    private int seleccionMultipleGenerica(InputProvider consoleInputProvider,String titulo,String[] opciones){
        while(true) {
            outputProvider.println(titulo+" | ESC para salir.");
            for(int i=0;i<opciones.length;i++){
                outputProvider.println((i+1)+". "+opciones[i]);
            }
            String textoIntroducido = consoleInputProvider.leerOpcionString();
            if (textoIntroducido.equalsIgnoreCase("esc")) return -1;
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
     * @deprecated Desde que se trasladó toda la funcionalidad de transferencias a su propio EstadoTransferencias
     */

    @Deprecated
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
     * @deprecated Desde que se trasladó toda la funcionalidad de transferencias a su propio EstadoTransferencias
     */
    @Deprecated
    private BigDecimal manejarEntradaDeCifraMonetaria(InputProvider consoleInputProvider) {
        boolean cantidadVálida = false;
        while(!cantidadVálida){
            outputProvider.println("Introducir cantidad con enteros y centavos $$$.$$ | ESC para salir.");
            String cantidadIntroducida = consoleInputProvider.leerOpcionString();
            if(cantidadIntroducida.equalsIgnoreCase("esc")) return null;
            cantidadVálida=cantidadIntroducida.matches("^\\d+\\.\\d{2}$");
            if (cantidadVálida) {
                return new BigDecimal(cantidadIntroducida);
            }
            outputProvider.println("Cantidad inválida, intente nuevamente...");
        }
        return  null;
    }




}