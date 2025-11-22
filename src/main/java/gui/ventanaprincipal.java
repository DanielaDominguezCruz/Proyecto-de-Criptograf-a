package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación "Simulador de Ataques - Criptografía".
 * Permite acceder a diferentes módulos: ataques de fuerza bruta, diccionario,
 * listado y detalle de simulaciones, cálculo de SHA-256 y salir de la aplicación.
 */
public class ventanaprincipal extends JFrame {

    /**
     * Constructor que configura la ventana principal con botones estilizados
     * y sus acciones correspondientes.
     */
    public ventanaprincipal() {
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nibums.NibumsLoosAndFeel"); // Intenta aplicar un look and feel
        } catch (Exception ex) {
            System.out.println("No se pudo aplicar Nibums"); // Mensaje si falla
        }

        setTitle("Simulador de Ataques - Criptografía"); // Título de la ventana
        setSize(450, 400); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centra la ventana en pantalla

        JPanel panel = new JPanel(new GridBagLayout()); // Panel principal con GridBagLayout
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Márgenes internos

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre botones
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Creación de botones estilizados
        JButton btnFuerzaBruta = createStyledButton("Ataque de Fuerza Bruta");
        JButton btnDiccionario = createStyledButton("Ataque de Diccionario");
        JButton btnListar = createStyledButton("Listar Simulaciones");
        JButton btnDetalle = createStyledButton("Ver Detalle de Simulación");
        JButton btnSha = createStyledButton("Obtener SHA-256 de un texto");
        JButton btnSalir = createStyledButton("Salir");

        // Agregar botones al panel con posicionamiento
        panel.add(btnFuerzaBruta, gbc); gbc.gridy++;
        panel.add(btnDiccionario, gbc); gbc.gridy++;
        panel.add(btnListar, gbc); gbc.gridy++;
        panel.add(btnDetalle, gbc); gbc.gridy++;
        panel.add(btnSha, gbc); gbc.gridy++;
        panel.add(btnSalir, gbc); gbc.gridy++;

        add(panel); // Agrega el panel a la ventana

        // Acciones de los botones
        btnFuerzaBruta.addActionListener(e -> new ventanaFuerzaBruta().setVisible(true));
        btnDiccionario.addActionListener(e -> new ventanaDiccionario().setVisible(true));
        btnListar.addActionListener(e -> new ventanaListarSim().setVisible(true));
        btnDetalle.addActionListener(e -> new ventanaDetalleSim().setVisible(true));
        btnSha.addActionListener(e -> new ventanaSha256().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0)); // Cierra la aplicación
    }

    /**
     * Crea un botón con estilo personalizado: fuente, color de fondo, borde y tamaño.
     * @param text Texto que se mostrará en el botón
     * @return JButton estilizado
     */
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(new Color(230, 240, 255));
        btn.setBorder(BorderFactory.createLineBorder(new Color(150, 180, 220)));
        btn.setPreferredSize(new Dimension(250, 40));
        return btn;
    }
}
