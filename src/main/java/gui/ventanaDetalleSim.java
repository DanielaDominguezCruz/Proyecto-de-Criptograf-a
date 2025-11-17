package gui;

import javax.swing.*;
import dao.SimulacionesDao;

public class ventanaDetalleSim extends JFrame {

    public ventanaDetalleSim() {
        setTitle("Detalle de Simulación");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JTextField txtId = new JTextField(10);
        JTextArea area = new JTextArea(10, 40);
        JButton btnBuscar = new JButton("Buscar");

        JPanel panel = new JPanel();
        panel.add(new JLabel("ID simulación:"));
        panel.add(txtId);
        panel.add(btnBuscar);
        panel.add(new JScrollPane(area));

        add(panel);

        btnBuscar.addActionListener(e -> {
            SimulacionesDao dao = new SimulacionesDao();
            area.setText(dao.obtenerDetalle(txtId.getText()));
        });
    }
}
