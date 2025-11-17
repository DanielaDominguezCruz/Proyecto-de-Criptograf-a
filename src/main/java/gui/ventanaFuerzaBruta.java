package gui;

import javax.swing.*;
import modelo.AtaqueFuerzaBruta;
import proyectocriptografia.ataques.ParametrosAtaque;
import java.util.Arrays;

public class ventanaFuerzaBruta extends JFrame {

    private JTextField txtHash;
    private JTextArea txtResultado;

    public ventanaFuerzaBruta() {
        setTitle("Ataque de Fuerza Bruta");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtHash = new JTextField(40);
        txtResultado = new JTextArea(10, 50);

        JButton btnIniciar = new JButton("Iniciar");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Hash objetivo:"));
        panel.add(txtHash);
        panel.add(btnIniciar);
        panel.add(new JScrollPane(txtResultado));

        add(panel);

        btnIniciar.addActionListener(e -> iniciar());
    }

    private void iniciar() {
        try {
            txtResultado.setText("Iniciando ataque...\n");

            // Crear parámetros
            ParametrosAtaque p = new ParametrosAtaque();
            p.hashObjetivo = txtHash.getText();
            p.alfabeto = "abcdefghijklmnopqrstuvwxyz";
            p.minLongitud = 1;
            p.maxLongitud = 5;
            p.limiteTiempoMs = 0;

            // Crear ataque
            AtaqueFuerzaBruta fb = new AtaqueFuerzaBruta();
            fb.configurar(p);

            // Ejecutar directamente (bloqueante)
            fb.run();

            txtResultado.append("Clave encontrada: " + fb.getResultadoClave() + "\n");
            txtResultado.append("Intentos: " + fb.getIntentosRealizados() + "\n");

        } catch (Exception ex) {
            txtResultado.append("ERROR: " + ex.getMessage());
        }
    }
}