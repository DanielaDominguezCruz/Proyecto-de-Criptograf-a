package proyectocriptografia.util;

import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Utilidad de hashing y verificación.
 */
public final class ComparadorHash {

    private ComparadorHash(){}

    /**
     * Calcula SHA-256 en hexadecimal minúsculas.
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
     * Compara un candidato en texto plano contra un hash SHA-256 esperado.
     */
    public static boolean coincideSha256(String candidato, String hashEsperado) {
        return sha256(candidato).equalsIgnoreCase(hashEsperado);
    }
}
