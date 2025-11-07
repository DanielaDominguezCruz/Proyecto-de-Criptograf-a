package proyectocriptografia.util;

import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Clase de utilidad para operaciones de hashing y comparacion de valores usando SHA-256.
 */
public final class ComparadorHash {

    /** Constructor privado para evitar instanciacion. */
    private ComparadorHash(){}

    /**
     * Calcula el hash SHA-256 de un texto y devuelve su representacion en hexadecimal minusculas.
     * @param texto texto de entrada a hashear
     * @return hash SHA-256 en formato hexadecimal minusculas
     * @throws IllegalStateException si ocurre un error al calcular el hash
     */
    public static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(dig);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo calcular SHA-256", e);
        }
    }

    /**
     * Compara un texto plano con un hash SHA-256 esperado.
     * @param candidato texto plano que se desea verificar
     * @param hashEsperado hash SHA-256 esperado en formato hexadecimal
     * @return true si el hash del texto coincide con el hash esperado, false en caso contrario
     */
    public static boolean coincideSha256(String candidato, String hashEsperado) {
        return sha256(candidato).equalsIgnoreCase(hashEsperado);
    }
}
