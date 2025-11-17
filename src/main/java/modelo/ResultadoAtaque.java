package modelo;

public class ResultadoAtaque {
    private boolean exito;
    private String palabra;
    private String mensaje;

    public ResultadoAtaque(boolean exito, String palabra, String mensaje) {
        this.exito = exito;
        this.palabra = palabra;
        this.mensaje = mensaje;
    }

    public boolean isExito() { return exito; }
    public String getPalabra() { return palabra; }
    public String getMensaje() { return mensaje; }

    @Override
    public String toString() {
        return "Éxito: " + exito + "\n" +
               "Palabra encontrada: " + palabra + "\n" +
               "Mensaje: " + mensaje;
    }
}
