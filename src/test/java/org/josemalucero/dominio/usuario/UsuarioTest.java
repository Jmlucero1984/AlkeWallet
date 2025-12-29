package org.josemalucero.dominio.usuario;

import Helpers.RandomStringGenerators;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    String nombre = RandomStringGenerators.getRandomString(8);
    String apellido = RandomStringGenerators.getRandomString(8);
    String clave = RandomStringGenerators.getRandomString(8);


    Usuario usuario = new Usuario(nombre, apellido,clave);

    @Test
    void getNombreCompleto() {
        Assertions.assertEquals(usuario.getNombreCompleto(),nombre+" "+apellido);

    }

    @Test
    void getClave() {
        Assertions.assertEquals(usuario.getClave(),clave);
    }


}