package proyectocriptografia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import proyectocriptografia.ataques.GeneradorCombinaciones;

/**
 * Pruebas unitarias para la clase {@link GeneradorCombinaciones}.
 * Verifica la correcta generacion de combinaciones segun el alfabeto y longitudes indicadas.
 */
public class GeneradorCombinacionesTest {

    /**
     * Verifica que el generador produzca correctamente todas las combinaciones esperadas
     * para el alfabeto "ab" con longitudes entre 1 y 2.
     */
    @Test
    void generaCorrectamente() {
        var gen = new GeneradorCombinaciones("ab", 1, 2);
        var it = gen.iterator();

        Assertions.assertTrue(it.hasNext());
        Assertions.assertEquals("a", it.next());  // Primera combinacion: "a"
        Assertions.assertEquals("b", it.next());  // Segunda combinacion: "b"
        Assertions.assertEquals("aa", it.next()); // Tercera combinacion: "aa"
        Assertions.assertEquals("ab", it.next()); // Cuarta combinacion: "ab"
        Assertions.assertEquals("ba", it.next()); // Quinta combinacion: "ba"
        Assertions.assertEquals("bb", it.next()); // Sexta combinacion: "bb"
        Assertions.assertFalse(it.hasNext());
    }
}
