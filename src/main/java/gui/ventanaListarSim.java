package gui;

import javax.swing.*;
import dao.SimulacionesDao;

/**
 * Ventana que muestra un listado de todas las simulaciones registradas.
 */
public class ventanaListarSim extends JFrame {

    /**
     * Constructor de la ventanaListarSim.
     * Configura la interfaz y carga las simulaciones en un área de texto.
     */
    public ventanaListarSim() {
        setTitle("Listado de Simulaciones"); // Título de la ventana
        setSize(600, 400); // Tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        JTextArea area = new JTextArea(15, 50); // Área de texto para mostrar las simulaciones

        SimulacionesDao dao = new SimulacionesDao(); // DAO para acceder a la base de datos
        area.setText(dao.listarSimulaciones()); // Carga el listado de simulaciones en el área de texto

        add(new JScrollPane(area)); // Añade el área de texto con scroll a la ventana
    }
}
