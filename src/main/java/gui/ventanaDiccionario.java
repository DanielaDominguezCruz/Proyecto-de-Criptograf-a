package gui;

import javax.swing.*;
import modelo.AtaqueDiccionario;
import proyectocriptografia.ataques.ParametrosAtaque;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Ventana gráfica para ejecutar un ataque de diccionario contra un hash objetivo.
 */
public class ventanaDiccionario extends JFrame {

    private JTextField txtHash, txtDiccionario;
    private JTextArea txtSalida;

    /**
     * Constructor de la ventana que configura el entorno gráfico del ataque de diccionario.
     */
    public ventanaDiccionario() {
        setTitle("Ataque de Diccionario");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtHash = new JTextField(40); // Campo para ingresar el hash objetivo
        txtDiccionario = new JTextField(30); // Campo para mostrar la ruta del diccionario
        txtSalida = new JTextArea(15, 50); // Área para mostrar resultados del ataque

        JButton btnExaminar = new JButton("Examinar..."); // Botón para seleccionar archivo de diccionario
        JButton btnIniciar = new JButton("Iniciar Ataque"); // Botón para ejecutar el ataque

        JPanel panel = new JPanel(); // Panel principal que contiene todos los elementos
        panel.add(new JLabel("Hash objetivo:")); // Etiqueta para el hash
        panel.add(txtHash);
        panel.add(new JLabel("Diccionario:")); // Etiqueta para el archivo diccionario
        panel.add(txtDiccionario);
        panel.add(btnExaminar);
        panel.add(btnIniciar);
        panel.add(new JScrollPane(txtSalida)); // Área de salida con scroll

        add(panel);

        // Acción para seleccionar archivo de diccionario
        btnExaminar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtDiccionario.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        // Acción para iniciar el ataque
        btnIniciar.addActionListener(e -> ejecutarAtaque());
    }

    /**
     * Ejecuta el ataque de diccionario usando los parámetros ingresados por el usuario.
     */
    private void ejecutarAtaque() {
        try {
            txtSalida.setText("Cargando diccionario...\n");

            List<String> dic = Files.readAllLines(Paths.get(txtDiccionario.getText())); // Lee el diccionario línea por línea

            ParametrosAtaque p = new ParametrosAtaque(); // Configuración de ataque
            p.diccionario = dic;
            p.hashObjetivo = txtHash.getText();
            p.limiteTiempoMs = 0;

            AtaqueDiccionario ataque = new AtaqueDiccionario(); // Instancia del ataque
            ataque.configurar(p);

            txtSalida.append("Ejecutando...\n");
            ataque.run(); // Realiza el ataque

            txtSalida.append("\nRESULTADO:\n");
            txtSalida.append("Palabra encontrada: " + ataque.getResultadoClave() + "\n");
            txtSalida.append("Intentos: " + ataque.getIntentosRealizados() + "\n");

        } catch (Exception ex) {
            txtSalida.append("ERROR: " + ex.getMessage());
        }
    }
}

