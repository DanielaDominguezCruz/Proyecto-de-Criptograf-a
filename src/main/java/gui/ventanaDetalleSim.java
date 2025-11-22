package gui;

import javax.swing.*;
import dao.SimulacionesDao;

/**
 * Ventana que muestra el detalle de una simulación.
 * Permite ingresar un ID de simulación y mostrar su información correspondiente.
 */
public class ventanaDetalleSim extends JFrame {

    /**
     * Constructor de la ventanaDetalleSim.
     * Configura el título, tamaño, posición y elementos de la interfaz.
     */
    public ventanaDetalleSim() {
        setTitle("Detalle de Simulación"); // Establece el título de la ventana
        setSize(500, 300); // Define el tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        JTextField txtId = new JTextField(10); // Campo de texto para ingresar el ID de la simulación
        JTextArea area = new JTextArea(10, 40); // Área de texto para mostrar los detalles de la simulación
        JButton btnBuscar = new JButton("Buscar"); // Botón para iniciar la búsqueda

        JPanel panel = new JPanel(); // Panel contenedor de los componentes
        panel.add(new JLabel("ID simulación:")); // Etiqueta para el campo de ID
        panel.add(txtId); // Añade el campo de texto al panel
        panel.add(btnBuscar); // Añade el botón al panel
        panel.add(new JScrollPane(area)); // Añade el área de texto dentro de un scroll

        add(panel); // Añade el panel a la ventana

        // Acción del botón Buscar: obtiene los detalles de la simulación y los muestra en el área de texto
        btnBuscar.addActionListener(e -> {
            SimulacionesDao dao = new SimulacionesDao(); // Crea instancia del DAO para acceder a la base de datos
            area.setText(dao.obtenerDetalle(txtId.getText())); // Muestra el detalle obtenido en el área de texto
        });
    }
}
