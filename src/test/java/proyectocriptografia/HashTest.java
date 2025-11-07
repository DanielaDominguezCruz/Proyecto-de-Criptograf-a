package proyectocriptografia;

import org.junit.jupiter.api.Test;
import proyectocriptografia.util.ComparadorHash;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ComparadorHash}.
 * Verifica el comportamiento deterministico del metodo de hashing SHA-256.
 */
public class HashTest {

    /**
     * Comprueba que el metodo {@link ComparadorHash#sha256(String)} produzca
     * siempre el mismo resultado para el mismo texto de entrada.
     */
    @Test
    void sha256Deterministico() {
        String h1 = ComparadorHash.sha256("abc123");
        String h2 = ComparadorHash.sha256("abc123");
        assertEquals(h1, h2);
    }
}
