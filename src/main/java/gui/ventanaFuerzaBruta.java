package gui;

import javax.swing.*;
import modelo.AtaqueFuerzaBruta;
import proyectocriptografia.ataques.ParametrosAtaque;

/**
 * Ventana gráfica para ejecutar un ataque de fuerza bruta sobre un hash objetivo.
 */
public class ventanaFuerzaBruta extends JFrame {

    private JTextField txtHash;
    private JTextArea txtResultado;

    /**
     * Constructor que configura la interfaz del ataque de fuerza bruta.
     */
    public ventanaFuerzaBruta() {
        setTitle("Ataque de Fuerza Bruta"); // Título de la ventana
        setSize(500, 400); // Tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana

        txtHash = new JTextField(40); // Campo para escribir el hash objetivo
        txtResultado = new JTextArea(10, 50); // Área donde se muestran los resultados

        JButton btnIniciar = new JButton("Iniciar"); // Botón para ejecutar el ataque

        JPanel panel = new JPanel(); // Panel contenedor
        panel.add(new JLabel("Hash objetivo:")); // Etiqueta del texto del hash
        panel.add(txtHash);
        panel.add(btnIniciar);
        panel.add(new JScrollPane(txtResultado)); // Área de salida con scroll

        add(panel);

        // Acción del botón para iniciar el ataque
        btnIniciar.addActionListener(e -> iniciar());
    }

    /**
     * Ejecuta el ataque de fuerza bruta configurando los parámetros
     * y mostrando el resultado en el área de texto.
     */
    private void iniciar() {
        try {
            txtResultado.setText("Iniciando ataque...\n");

            ParametrosAtaque p = new ParametrosAtaque(); // Parámetros del ataque
            p.hashObjetivo = txtHash.getText();
            p.alfabeto = "abcdefghijklmnopqrstuvwxyz"; // Conjunto de caracteres usados
            p.minLongitud = 1; // Longitud mínima de la clave
            p.maxLongitud = 5; // Longitud máxima de la clave
            p.limiteTiempoMs = 0; // Sin límite de tiempo

            AtaqueFuerzaBruta fb = new AtaqueFuerzaBruta(); // Instancia del ataque
            fb.configurar(p);

            fb.run(); // Ejecuta el ataque de manera bloqueante

            txtResultado.append("Clave encontrada: " + fb.getResultadoClave() + "\n");
            txtResultado.append("Intentos: " + fb.getIntentosRealizados() + "\n");

        } catch (Exception ex) {
            txtResultado.append("ERROR: " + ex.getMessage());
        }
    }
}
