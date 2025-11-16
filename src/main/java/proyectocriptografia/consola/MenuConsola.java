package proyectocriptografia.consola;

import proyectocriptografia.ataques.ParametrosAtaque;
import proyectocriptografia.dominio.Simulacion;
import proyectocriptografia.servicios.ServicioSimulacion;
import proyectocriptografia.util.ComparadorHash;
import proyectocriptografia.util.Formato;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Menú de consola para ejecutar/consultar simulaciones de ataques.
 * Comentarios mínimos: cada método documentado con su propósito esencial.
 */
public class MenuConsola {
    private final ServicioSimulacion servicio = new ServicioSimulacion();
    private final Scanner in = new Scanner(System.in);

    /**
     * Muestra el menú principal y procesa las opciones hasta que se sale.
     */
    public void mostrar() {
        while (true) {
            System.out.println("\n=== SIMULADOR DE ATAQUES ===");
            System.out.println("1) Ataque de Fuerza Bruta");
            System.out.println("2) Ataque de Diccionario");
            System.out.println("3) Listar simulaciones");
            System.out.println("4) Ver detalle de simulacion");
            System.out.println("5) Obtener SHA-256 de un texto");
            System.out.println("0) Salir");
            System.out.print("Opcion: ");
            String op = in.nextLine().trim();
            switch (op) {
                case "1" -> opcionFuerzaBruta();
                case "2" -> opcionDiccionario();
                case "3" -> listar();
                case "4" -> detalle();
                case "5" -> hashDeTexto();
                case "0" -> {
                    System.out.println("Adios.");
                    return;
                }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    /**
     * Pide parámetros y ejecuta la simulación de fuerza bruta.
     */
    private void opcionFuerzaBruta() {
        try {
            System.out.print("Hash objetivo SHA-256 (hex): ");
            String hash = in.nextLine().trim();
            if (hash.isEmpty()) {
                System.out.println("Hash vacio: operacion cancelada.");
                return;
            }

            System.out.print("Alfabeto (ej. abcdef0123456789): ");
            String alfabeto = in.nextLine().trim();
            if (alfabeto.isEmpty()) {
                System.out.println("Alfabeto vacio: operacion cancelada.");
                return;
            }

            int min = leerInt("Longitud minima: ", 1);
            int max = leerInt("Longitud maxima: ", 3);
            if (min < 1) min = 1;
            if (max < min) {
                System.out.println("Longitud maxima menor que minima. Se ajusta max = min.");
                max = min;
            }

            long muestral = leerLong("Guardar intento muestral cada N (0=desactiva) [0]: ", 0L);
            long limite = leerLong("Limite de tiempo en ms (0=sin limite) [0]: ", 0L);

            ParametrosAtaque p = new ParametrosAtaque();
            p.hashObjetivo = hash;
            p.alfabeto = alfabeto;
            p.minLongitud = min;
            p.maxLongitud = max;
            p.maxIntentosMuestralesCada = muestral;
            p.limiteTiempoMs = limite;

            Simulacion s = servicio.simularFuerzaBruta(p);
            imprimirResultado(s);
        } catch (Exception e) {
            System.out.println("Error en parametros: " + e.getMessage());
        }
    }

    /**
     * Pide ruta de diccionario, la valida y ejecuta la simulación por diccionario.
     */
    private void opcionDiccionario() {
        try {
            System.out.print("Hash objetivo SHA-256 (hex): ");
            String hash = in.nextLine().trim();
            if (hash.isEmpty()) {
                System.out.println("Hash vacio: operacion cancelada.");
                return;
            }

            String ruta;
            while (true) {
                System.out.print("Ruta del diccionario (txt, una palabra por linea): ");
                ruta = in.nextLine().trim();
                if (ruta.isEmpty()) {
                    System.out.println("Ruta vacia: por favor ingrese la ruta completa al archivo.");
                    continue;
                }
                Path p = Path.of(ruta);
                if (!Files.exists(p)) {
                    System.out.println("Archivo no existe: " + ruta);
                    continue;
                }
                if (!Files.isReadable(p)) {
                    System.out.println("Archivo no es legible (permisos): " + ruta);
                    continue;
                }
                if (Files.isDirectory(p)) {
                    System.out.println("La ruta indicada es un directorio, debe ser un archivo .txt: " + ruta);
                    continue;
                }
                // ruta válida en p
                List<String> dic = Files.readAllLines(p);

                long muestral = leerLong("Guardar intento muestral cada N (0=desactiva): ", 0L);
                long limite = leerLong("Limite de tiempo en ms (0=sin limite): ", 0L);

                ParametrosAtaque parametros = new ParametrosAtaque();
                parametros.hashObjetivo = hash;
                parametros.diccionario = dic;
                parametros.maxIntentosMuestralesCada = muestral;
                parametros.limiteTiempoMs = limite;

                Simulacion s = servicio.simularDiccionario(parametros);
                imprimirResultado(s);
                return;
            }

        } catch (Exception e) {
            System.out.println("Error cargando diccionario: " + e.getMessage());
        }
    }

    /**
     * Muestra una tabla con las simulaciones registradas.
     */
    private void listar() {
        var sims = servicio.consultarSimulaciones();
        System.out.printf("%-6s %-14s %-8s %-12s %-10s %-12s\n",
                "ID","TIPO","EXITO","INTENTOS","IPS","DURACION");
        for (var s : sims) {
            long durMs = (s.getFin()==null? 0 : (s.getFin().toEpochMilli()-s.getInicio().toEpochMilli()));
            System.out.printf("%-6d %-14s %-8b %-12d %-10s %-12s\n",
                    s.getId(), s.getTipo(), s.isExito(), s.getIntentosTotales(),
                    Formato.double2(s.getIntentosPorSegundo()), Formato.msAHumano(durMs));
        }
    }

    /**
     * Muestra los intentos muestrales de una simulación por su ID.
     */
    private void detalle() {
        try {
            System.out.print("ID de simulacion: ");
            String linea = in.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("ID vacio: operacion cancelada.");
                return;
            }
            long id = Long.parseLong(linea);
            var intentos = servicio.consultarDetalle(id);
            System.out.println("Mostrando hasta 10,000 intentos muestrales:");
            for (var i : intentos) {
                System.out.printf("#%d [%s] -> %s\n",
                        i.getIndice(), i.getTimestamp(), i.getValorPropuesto());
            }
        } catch (Exception e) {
            System.out.println("Error consultando detalle: " + e.getMessage());
        }
    }

    /**
     * Calcula y muestra el SHA-256 del texto ingresado.
     */
    private void hashDeTexto(){
        System.out.print("Texto a hashear: ");
        String t = in.nextLine();
        System.out.println("SHA-256 = " + ComparadorHash.sha256(t));
    }

    /**
     * Imprime el resultado de una simulación; evita NPE si inicio/fin son null.
     * @param s simulación cuyo resultado se imprimirá
     */
    private void imprimirResultado(Simulacion s){
        long durMs = 0;
        if (s != null && s.getInicio() != null && s.getFin() != null) {
            durMs = s.getFin().toEpochMilli() - s.getInicio().toEpochMilli();
        }
        System.out.println("\n--- RESULTADO ---");
        System.out.println("ID: " + (s==null ? "n/a" : s.getId()));
        System.out.println("Tipo: " + (s==null ? "n/a" : s.getTipo()));
        System.out.println("Objetivo: " + (s==null ? "n/a" : s.getObjetivoHash()));
        System.out.println("Parametros: " + (s==null ? "n/a" : s.getParametros()));
        System.out.println("Exito: " + (s==null ? false : s.isExito()));
        System.out.println("Clave hallada: " + (s==null ? "n/a" : s.getClaveHallada()));
        System.out.println("Intentos totales: " + (s==null ? 0 : s.getIntentosTotales()));
        System.out.println("Intentos/seg: " + (s==null ? "0.00" : Formato.double2(s.getIntentosPorSegundo())));
        System.out.println("Duracion: " + Formato.msAHumano(durMs));
    }

    /**
     * Lee un entero; si se presiona Enter devuelve el valor por defecto.
     */
    private int leerInt(String prompt, int defecto) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) return defecto;
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) {
            System.out.println("Valor invalido. Usando " + defecto + ".");
            return defecto;
        }
    }

    /**
     * Lee un long; si se presiona Enter devuelve el valor por defecto.
     */
    private long leerLong(String prompt, long defecto) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) return defecto;
        try { return Long.parseLong(s); }
        catch (NumberFormatException e) {
            System.out.println("Valor invalido. Usando " + defecto + ".");
            return defecto;
        }
    }
}
