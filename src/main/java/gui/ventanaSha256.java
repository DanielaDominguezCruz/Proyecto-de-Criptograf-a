package gui;

import javax.swing.*;
import java.security.MessageDigest;

public class ventanaSha256 extends JFrame {

    private JTextField txtEntrada;
    private JTextArea txtSalida;

    public ventanaSha256() {
        setTitle("Obtener SHA-256");
        setSize(500, 400);
        setLocationRelativeTo(null);

        txtEntrada = new JTextField(30);
        txtSalida = new JTextArea(10, 40);

        JButton btnCalcular = new JButton("Calcular");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Texto:"));
        panel.add(txtEntrada);
        panel.add(btnCalcular);
        panel.add(new JScrollPane(txtSalida));

        add(panel);

        btnCalcular.addActionListener(e -> calcular());
    }

    private void calcular() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(txtEntrada.getText().getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            txtSalida.setText(hex.toString());
        } catch (Exception ex) {
            txtSalida.setText("Error: " + ex.getMessage());
        }
    }
}
