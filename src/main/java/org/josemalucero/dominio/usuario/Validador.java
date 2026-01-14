package org.josemalucero.dominio.usuario;

import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

public class Validador {

    InputProvider inputProvider;
    OutputProvider outputProvider;

    public Validador(InputProvider inputProvider, OutputProvider outputProvider) {
        this.inputProvider = inputProvider;
        this.outputProvider = outputProvider;
    }

    /**
     * Recibe lo que el usuario introduce como su clave y realiza una serie de validaciones mínimas,
     * como cantidad mínima y máxima de caracteres o prohibición de espacios intermedios.
     * @return {@code String} que cumple con las especificaciones para credenciales de usuario.
     */
    public String validarClavesDeUsuario() {
        String entrada;

        entrada = inputProvider.leerOpcionString().trim();
        if (entrada.length() == 0) {
            outputProvider.println(Messages.get("alerta.validacion.no.puede.estar.vacio"));
            return null;
        }

        if (entrada.length() <6) {
            outputProvider.println(Messages.get("alerta.validacion.tener.al.menos")+" "+6+" "+Messages.get("caracteres"));
            return null;
        }

        if (entrada.length() > 10) {
            outputProvider.println(Messages.get("alerta.validacion.tener.no.mas")+" "+10+" "+Messages.get("caracteres"));
            return null;
        }

        if (entrada.contains(" ")) {
            outputProvider.println(Messages.get("alerta.validacion.no.espacios.intermedios"));
            return null;
        }

        return entrada;
    }

    /**
     * Recibe lo que el usuario introduce como su nombre y apellido y realiza una serie de validaciones mínimas,
     * como cantidad mínima y máxima de caracteres, prohibición de espacios intermedios, letra inicial en mayúscula
     * obligatoria y solo caracteres alfabéticos.
     * @return {@code String} que cumple con las especificaciones para credenciales de usuario.
     */

    public String validaNombresOApellidosDeUsuario(){
        String entrada;
        entrada = inputProvider.leerOpcionString().trim();
        if (entrada.length() == 0) {
            outputProvider.println(Messages.get("alerta.validacion.no.puede.estar.vacio"));
            return null;
        }

        if (entrada.length() < 3) {
            outputProvider.println(Messages.get("alerta.validacion.tener.al.menos")+" "+3+" "+Messages.get("caracteres"));
            return null;
        }

        if (entrada.length() > 15) {
            outputProvider.println(Messages.get("alerta.validacion.tener.no.mas")+" "+15+" "+Messages.get("caracteres"));
            return null;
        }

        if (!entrada.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$")) {
            outputProvider.println(Messages.get("alerta.validacion.contrasena.solo.contener"));
            return null;
        }

        if (!Character.isUpperCase(entrada.charAt(0))) {
            outputProvider.println(Messages.get("alerta.validacion.debe.empezar.con.mayuscula"));
            return null;
        }

        if (entrada.contains(" ")) {
            outputProvider.println(Messages.get("validacion.no.espacios.intermedios"));
            return null;
        }

        return entrada;

    }

}
