package gui;

import javax.swing.*;
import java.security.MessageDigest;

/**
 * Ventana para calcular el hash SHA-256 de un texto ingresado por el usuario.
 */
public class ventanaSha256 extends JFrame {

    private JTextField txtEntrada;
    private JTextArea txtSalida;

    /**
     * Constructor que configura la interfaz para ingresar texto y mostrar el hash SHA-256.
     */
    public ventanaSha256() {
        setTitle("Obtener SHA-256"); // Título de la ventana
        setSize(500, 400); // Tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        txtEntrada = new JTextField(30); // Campo de texto para ingresar el texto
        txtSalida = new JTextArea(10, 40); // Área de texto para mostrar el hash resultante

        JButton btnCalcular = new JButton("Calcular"); // Botón para calcular el hash

        JPanel panel = new JPanel(); // Panel contenedor
        panel.add(new JLabel("Texto:")); // Etiqueta para el campo de entrada
        panel.add(txtEntrada);
        panel.add(btnCalcular);
        panel.add(new JScrollPane(txtSalida)); // Área de salida con scroll

        add(panel);

        // Acción del botón para calcular el hash SHA-256
        btnCalcular.addActionListener(e -> calcular());
    }

    /**
     * Calcula el hash SHA-256 del texto ingresado y lo muestra en el área de salida.
     */
    private void calcular() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Instancia del algoritmo SHA-256
            byte[] hash = md.digest(txtEntrada.getText().getBytes()); // Obtiene el hash del texto
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b)); // Convierte cada byte a hexadecimal
            }
            txtSalida.setText(hex.toString()); // Muestra el hash en el área de texto
        } catch (Exception ex) {
            txtSalida.setText("Error: " + ex.getMessage()); // Muestra el error si ocurre
        }
    }
}

