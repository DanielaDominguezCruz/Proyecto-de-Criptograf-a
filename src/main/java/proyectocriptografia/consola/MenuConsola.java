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
 * Clase que implementa el menu de consola para ejecutar y consultar simulaciones
 * de ataques de fuerza bruta y diccionario.
 */
public class MenuConsola {
    private final ServicioSimulacion servicio = new ServicioSimulacion();
    private final Scanner in = new Scanner(System.in);

    /**
     * Muestra el menu principal en consola y gestiona las opciones seleccionadas por el usuario.
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
                case "0" -> { System.out.println("Adios."); return; }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    /**
     * Ejecuta el ataque de fuerza bruta solicitando los parametros al usuario.
     * Incluye validaciones de entrada y valores por defecto cuando el usuario presiona Enter.
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
     * Ejecuta el ataque de diccionario solicitando los parametros al usuario.
     * Valida la ruta del archivo, verifica permisos y evita valores vacios para parametros numericos.
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
                break;
            }

            List<String> dic = Files.readAllLines(Path.of(ruta));

            long muestral = leerLong("Guardar intento muestral cada N (0=desactiva): ", 0L);
            long limite = leerLong("Limite de tiempo en ms (0=sin limite): ", 0L);

            ParametrosAtaque p = new ParametrosAtaque();
            p.hashObjetivo = hash;
            p.diccionario = dic;
            p.maxIntentosMuestralesCada = muestral;
            p.limiteTiempoMs = limite;

            Simulacion s = servicio.simularDiccionario(p);
            imprimirResultado(s);
        } catch (Exception e) {
            System.out.println("Error cargando diccionario: " + e.getMessage());
        }
    }

    /**
     * Lista las simulaciones registradas mostrando su informacion basica.
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
     * Muestra los intentos muestrales de una simulacion segun su ID.
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
     * Calcula el hash SHA-256 de un texto ingresado por el usuario y lo muestra por consola.
     */
    private void hashDeTexto(){
        System.out.print("Texto a hashear: ");
        String t = in.nextLine();
        System.out.println("SHA-256 = " + ComparadorHash.sha256(t));
    }

    /**
     * Imprime el resultado completo de una simulacion.
     * @param s objeto Simulacion con los datos del resultado
     */
    private void imprimirResultado(Simulacion s){
        long durMs = (s.getFin().toEpochMilli()-s.getInicio().toEpochMilli());
        System.out.println("\n--- RESULTADO ---");
        System.out.println("ID: " + s.getId());
        System.out.println("Tipo: " + s.getTipo());
        System.out.println("Objetivo: " + s.getObjetivoHash());
        System.out.println("Parametros: " + s.getParametros());
        System.out.println("Exito: " + s.isExito());
        System.out.println("Clave hallada: " + s.getClaveHallada());
        System.out.println("Intentos totales: " + s.getIntentosTotales());
        System.out.println("Intentos/seg: " + Formato.double2(s.getIntentosPorSegundo()));
        System.out.println("Duracion: " + Formato.msAHumano(durMs));
    }

    /**
     * Lee un numero entero desde la entrada estandar.
     * Si el usuario presiona Enter, devuelve el valor por defecto.
     * @param prompt mensaje mostrado al usuario
     * @param defecto valor por defecto si no se ingresa nada
     * @return entero ingresado o el valor por defecto
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
     * Lee un numero long desde la entrada estandar.
     * Si el usuario presiona Enter, devuelve el valor por defecto.
     * @param prompt mensaje mostrado al usuario
     * @param defecto valor por defecto si no se ingresa nada
     * @return numero long ingresado o el valor por defecto
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
