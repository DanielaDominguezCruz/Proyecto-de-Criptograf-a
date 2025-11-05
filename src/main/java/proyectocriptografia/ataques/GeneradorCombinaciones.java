package proyectocriptografia.ataques;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class GeneradorCombinaciones implements Iterable<String> {
    private final char[] alfabeto;
    private final int minLen, maxLen;

    public GeneradorCombinaciones(String alfabeto, int minLen, int maxLen) {
        if (alfabeto == null || alfabeto.isEmpty()) throw new IllegalArgumentException("alfabeto no puede ser vacio");
        if (minLen < 1 || maxLen < minLen) throw new IllegalArgumentException("longitudes invalidas");
        this.alfabeto = alfabeto.toCharArray();
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    @Override 
    public Iterator<String> iterator() { 
        return new Iter(); 
    }

    private final class Iter implements Iterator<String> {
        private int len;
        private int[] idx;
        private boolean agotado;
        private String proximo;

        public Iter() {
            this.len = minLen;
            if (minLen <= maxLen) {
                this.idx = new int[minLen];
                calcularProximo();
            } else {
                this.agotado = true;
            }
        }

        @Override 
        public boolean hasNext() { 
            return !agotado; 
        }

        @Override 
        public String next() {
            if (agotado) throw new NoSuchElementException();
            
            String actual = proximo;
            avanzarIndices();
            calcularProximo();
            
            return actual;
        }
        
        private void calcularProximo() {
            if (agotado) return;
            
            char[] out = new char[len];
            for (int i = 0; i < len; i++) {
                out[i] = alfabeto[idx[i]];
            }
            proximo = new String(out);
        }
        
        private void avanzarIndices() {
            // Avanzar como contador en base N (tamaño del alfabeto)
            for (int pos = len - 1; pos >= 0; pos--) {
                if (idx[pos] < alfabeto.length - 1) {
                    idx[pos]++;
                    return;
                } else {
                    idx[pos] = 0;
                }
            }
            
            // Si llegamos aquí, hemos agotado esta longitud
            len++;
            if (len > maxLen) {
                agotado = true;
            } else {
                idx = new int[len]; // Todos los índices en 0
            }
        }
    }
}