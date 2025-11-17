package gui;

import javax.swing.*;
import dao.SimulacionesDao;

public class ventanaListarSim extends JFrame {

    public ventanaListarSim() {
        setTitle("Listado de Simulaciones");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea(15, 50);

        SimulacionesDao dao = new SimulacionesDao();
        area.setText(dao.listarSimulaciones());

        add(new JScrollPane(area));
    }
}

