package gui;

import proyectocriptografia.ataques.ParametrosAtaque;
import proyectocriptografia.dominio.Simulacion;
import proyectocriptografia.servicios.ServicioSimulacion;
import proyectocriptografia.util.Formato;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Ventana para ejecutar ataques de diccionario desde la interfaz gráfica.
 * Carga un archivo de diccionario y muestra el resultado final de la simulación.
 */
public class ventanaDiccionario extends JFrame {

    private final ServicioSimulacion servicio = new ServicioSimulacion();

    public ventanaDiccionario() {
        setTitle("Ataque por Diccionario");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField txtHash = new JTextField(30);
        JTextField txtRuta = new JTextField(30);
        JTextField txtMuestral = new JTextField(10);
        JTextField txtLimite = new JTextField(10);
        JTextArea areaResultado = new JTextArea(10, 50);
        areaResultado.setEditable(false);
        JButton btnAtacar = new JButton("Iniciar Ataque");

        panel.add(new JLabel("Hash objetivo:")); panel.add(txtHash);
        panel.add(new JLabel("Ruta diccionario:")); panel.add(txtRuta);
        panel.add(new JLabel("Guardar intento muestral cada N:")); panel.add(txtMuestral);
        panel.add(new JLabel("Limite tiempo ms:")); panel.add(txtLimite);
        panel.add(btnAtacar);
        panel.add(new JScrollPane(areaResultado));

        add(panel);

        btnAtacar.addActionListener(e -> {
            try {
                String hash = txtHash.getText().trim();
                Path pArchivo = Path.of(txtRuta.getText().trim());
                if (!Files.exists(pArchivo) || !Files.isReadable(pArchivo)) {
                    areaResultado.setText("Archivo no encontrado o ilegible.");
                    return;
                }
                List<String> dic = Files.readAllLines(pArchivo);

                ParametrosAtaque p = new ParametrosAtaque();
                p.hashObjetivo = hash;
                p.diccionario = dic;
                p.maxIntentosMuestralesCada = Long.parseLong(txtMuestral.getText().trim());
                p.limiteTiempoMs = Long.parseLong(txtLimite.getText().trim());

                Simulacion s = servicio.simularDiccionario(p);

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
