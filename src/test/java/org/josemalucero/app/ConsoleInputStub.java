package org.josemalucero.app;

public class ConsoleInputStub implements InputProvider{
    int proximaRespuestaInt;
    String procimaRespuestaString;

    public void setProximaRespuestaInt(int proximaRespuestaInt) {
        this.proximaRespuestaInt = proximaRespuestaInt;
    }

    public void setProcimaRespuestaString(String procimaRespuestaString) {
        this.procimaRespuestaString = procimaRespuestaString;
    }

    @Override
    public int leerOpcionInt() {
        return proximaRespuestaInt;
    }

    @Override
    public String leerOpcionString() {
        return procimaRespuestaString;
    }
}
