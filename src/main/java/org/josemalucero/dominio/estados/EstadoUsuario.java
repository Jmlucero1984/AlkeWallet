package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;




/**
 La clase abstracta {@code EstadoUsuario} extiende la clase {@code Cuenta} con la propiedades
 y métodos elementales para registrar el activo digital, en este caso,
 la {@code MonedaConvertible}.
 Implementa las interfaces {@code Depositable},{@code Retirable},{@code Consultable},
 {@code Transferible} y {@code Convertible},
 <blockquote><b>Propiedades</b></blockquote>
 <li> {@code InputProvider inputProvider} inyectado mediante constructor, provee un medio para leer las opciones elegidas.</li>
 <li> {@code OutputProvider outputProvider} inyectado mediante constructor, provee un medio para dar salida a los mensajes.</li>
 <blockquote><b>Métodos</b></blockquote>
 <li> {@code abstract void mostrarMenu(ContextoUsuario contextoUsuario)} permitirá mostrar las opciones a elegir.</li>
 <li> {@code abstract void procesarOpcion(int opcion, ContextoUsuario contextoUsuario)} permitirá ejecutar acciones acorde a las elecciones del usuario.</li>
 <li> {@code abstract String getNombreEstado()} devolverá una cadena de texto descriptiva de cada estado que de esta extienda.</li>
 @author José Maria Lucero
 */
public abstract class EstadoUsuario {

    protected InputProvider inputProvider;
    protected OutputProvider outputProvider;

    public EstadoUsuario(InputProvider inputProvider, OutputProvider outputProvider) {
        this.inputProvider = inputProvider;
        this.outputProvider = outputProvider;
    }

    public abstract void mostrarMenu(ContextoUsuario contextoUsuario);

    public abstract void procesarOpcion(int opcion, ContextoUsuario contextoUsuario);

    public abstract String getNombreEstado();

}
