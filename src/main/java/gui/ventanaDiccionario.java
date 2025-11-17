package gui;

import javax.swing.*;
import modelo.AtaqueDiccionario;
import proyectocriptografia.ataques.ParametrosAtaque;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ventanaDiccionario extends JFrame {

    private JTextField txtHash, txtDiccionario;
    private JTextArea txtSalida;

    public ventanaDiccionario() {
        setTitle("Ataque de Diccionario");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtHash = new JTextField(40);
        txtDiccionario = new JTextField(30);
        txtSalida = new JTextArea(15, 50);

        JButton btnExaminar = new JButton("Examinar...");
        JButton btnIniciar = new JButton("Iniciar Ataque");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Hash objetivo:"));
        panel.add(txtHash);
        panel.add(new JLabel("Diccionario:"));
        panel.add(txtDiccionario);
        panel.add(btnExaminar);
        panel.add(btnIniciar);
        panel.add(new JScrollPane(txtSalida));

        add(panel);

        btnExaminar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtDiccionario.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        btnIniciar.addActionListener(e -> ejecutarAtaque());
    }

    private void ejecutarAtaque() {
        try {
            txtSalida.setText("Cargando diccionario...\n");

            List<String> dic = Files.readAllLines(Paths.get(txtDiccionario.getText()));

            ParametrosAtaque p = new ParametrosAtaque();
            p.diccionario = dic;
            p.hashObjetivo = txtHash.getText();
            p.limiteTiempoMs = 0;

            AtaqueDiccionario ataque = new AtaqueDiccionario();
            ataque.configurar(p);

            txtSalida.append("Ejecutando...\n");
            ataque.run();

            txtSalida.append("\nRESULTADO:\n");
            txtSalida.append("Palabra encontrada: " + ataque.getResultadoClave() + "\n");
            txtSalida.append("Intentos: " + ataque.getIntentosRealizados() + "\n");

        } catch (Exception ex) {
            txtSalida.append("ERROR: " + ex.getMessage());
        }
    }
}

