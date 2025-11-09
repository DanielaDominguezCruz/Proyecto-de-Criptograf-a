package proyectocriptografia.ataques;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Esta clase genera todas las combinaciones posibles de letras
 * usando un alfabeto dado, con una longitud mínima y máxima.
 * <p>
 * Por ejemplo, si el alfabeto es "abc" y los límites son 1 y 2,
 * se generarán: a, b, c, aa, ab, ac, ba, bb, bc, ca, cb, cc.
 * </p>
 */
public final class GeneradorCombinaciones implements Iterable<String> {

    /** Arreglo con los caracteres que se usarán para formar las combinaciones. */
    private final char[] alfabeto;

    /** Longitud mínima de las combinaciones. */
    private final int minLen;

    /** Longitud máxima de las combinaciones. */
    private final int maxLen;

    /**
     * Constructor del generador de combinaciones.
     *
     * @param alfabeto Cadena con los caracteres permitidos.
     * @param minLen Longitud mínima de las combinaciones.
     * @param maxLen Longitud máxima de las combinaciones.
     * @throws IllegalArgumentException Si el alfabeto está vacío o las longitudes no son válidas.
     */
    public GeneradorCombinaciones(String alfabeto, int minLen, int maxLen) {
        if (alfabeto == null || alfabeto.isEmpty()) throw new IllegalArgumentException("alfabeto no puede ser vacio");
        if (minLen < 1 || maxLen < minLen) throw new IllegalArgumentException("longitudes invalidas");
        this.alfabeto = alfabeto.toCharArray();
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    /**
     * Devuelve un iterador que recorre todas las combinaciones posibles.
     *
     * @return Iterador de cadenas generadas.
     */
    @Override
    public Iterator<String> iterator() {
        return new Iter();
    }

    /**
     * Clase interna que se encarga de generar las combinaciones una por una.
     */
    private final class Iter implements Iterator<String> {

        /** Longitud actual de la combinación. */
        private int len;

        /** Arreglo que guarda los índices actuales de los caracteres. */
        private int[] idx;

        /** Indica si ya no hay más combinaciones por generar. */
        private boolean agotado;

        /** Guarda la próxima combinación que se devolverá. */
        private String proximo;

        /**
         * Constructor del iterador.
         * Inicia la generación desde la longitud mínima.
         */
        public Iter() {
            this.len = minLen;
            if (minLen <= maxLen) {
                this.idx = new int[minLen];
                calcularProximo();
            } else {
                this.agotado = true;
            }
        }

        /**
         * Indica si todavía hay combinaciones por generar.
         *
         * @return true si hay más combinaciones, false si ya se terminaron.
         */
        @Override
        public boolean hasNext() {
            return !agotado;
        }

        /**
         * Devuelve la siguiente combinación generada.
         *
         * @return La siguiente cadena del generador.
         * @throws NoSuchElementException Si ya no hay más combinaciones.
         */
        @Override
        public String next() {
            if (agotado) throw new NoSuchElementException();

            String actual = proximo;
            avanzarIndices();
            calcularProximo();

            return actual;
        }

        /**
         * Calcula la próxima combinación a partir de los índices actuales.
         */
        private void calcularProximo() {
            if (agotado) return;

            char[] out = new char[len];
            for (int i = 0; i < len; i++) {
                out[i] = alfabeto[idx[i]];
            }
            proximo = new String(out);
        }

        /**
         * Avanza los índices para generar la siguiente combinación.
         * Funciona como un contador en base al tamaño del alfabeto.
         */
        private void avanzarIndices() {
            for (int pos = len - 1; pos >= 0; pos--) {
                if (idx[pos] < alfabeto.length - 1) {
                    idx[pos]++;
                    return;
                } else {
                    idx[pos] = 0;
                }
            }

            // Si se terminaron todas las combinaciones de esta longitud
            len++;
            if (len > maxLen) {
                agotado = true;
            } else {
                idx = new int[len]; // Reinicia los índices a 0
            }
        }
    }
}
