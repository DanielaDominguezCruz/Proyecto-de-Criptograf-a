package gui;

import proyectocriptografia.ataques.ParametrosAtaque;
import proyectocriptografia.dominio.Simulacion;
import proyectocriptografia.servicios.ServicioSimulacion;
import proyectocriptografia.util.Formato;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana para ejecutar ataques de fuerza bruta desde la interfaz gráfica.
 * Recibe parámetros de alfabeto y longitudes, y muestra el resultado final.
 */
public class ventanaFuerzaBruta extends JFrame {

    private final ServicioSimulacion servicio = new ServicioSimulacion();

    public ventanaFuerzaBruta() {
        setTitle("Ataque de Fuerza Bruta");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtHash = new JTextField(30);
        JTextField txtAlfabeto = new JTextField(20);
        JTextField txtMin = new JTextField(5);
        JTextField txtMax = new JTextField(5);
        JTextField txtMuestral = new JTextField(10);
        JTextField txtLimite = new JTextField(10);
        JTextArea areaResultado = new JTextArea(10, 50);
        areaResultado.setEditable(false);
        JButton btnAtacar = new JButton("Iniciar Ataque");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Hash objetivo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtHash, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Alfabeto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtAlfabeto, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Longitud mínima:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txtMin, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Longitud máxima:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(txtMax, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Guardar intento muestral cada N:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(txtMuestral, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Limite tiempo ms:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(txtLimite, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; panel.add(btnAtacar, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(new JScrollPane(areaResultado), gbc);

        add(panel);

        btnAtacar.addActionListener(e -> {
            try {
                ParametrosAtaque p = new ParametrosAtaque();
                p.hashObjetivo = txtHash.getText().trim();
                p.alfabeto = txtAlfabeto.getText().trim();
                p.minLongitud = Integer.parseInt(txtMin.getText().trim());
                p.maxLongitud = Integer.parseInt(txtMax.getText().trim());
                p.maxIntentosMuestralesCada = Long.parseLong(txtMuestral.getText().trim());
                p.limiteTiempoMs = Long.parseLong(txtLimite.getText().trim());

                Simulacion s = servicio.simularFuerzaBruta(p);

                areaResultado.setText("--- RESULTADO ---\n" +
                        "ID: " + s.getId() + "\n" +
                        "Tipo: " + s.getTipo() + "\n" +
                        "Hash objetivo: " + s.getObjetivoHash() + "\n" +
                        "Clave hallada: " + s.getClaveHallada() + "\n" +
                        "Intentos totales: " + s.getIntentosTotales() + "\n" +
                        "Intentos/seg: " + Formato.double2(s.getIntentosPorSegundo()) + "\n" +
                        "Éxito: " + s.isExito()
                );

            } catch (Exception ex) {
                areaResultado.setText("Error: " + ex.getMessage());
            }
        });
    }
}
