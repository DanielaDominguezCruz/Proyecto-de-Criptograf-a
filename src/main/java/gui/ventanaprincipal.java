package gui;

import javax.swing.*;
import java.awt.*;

public class ventanaprincipal extends JFrame {

    public ventanaprincipal() {
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nibums.NibumsLoosAndFeel");
        } catch (Exception ex) {
            System.out.println("No se pudo aplicar Nibums");
        
        }
        setTitle("Simulador de Ataques - Criptografía");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,0,10,0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton btnFuerzaBruta = createStyledButton("Ataque de Fuerza Bruta");
        JButton btnDiccionario = createStyledButton("Ataque de Diccionario");
        JButton btnListar = createStyledButton("Listar Simulaciones");
        JButton btnDetalle = createStyledButton("Ver Detalle de Simulación");
        JButton btnSha = createStyledButton("Obtener SHA-256 de un texto");
        JButton btnSalir = createStyledButton("Salir");

        panel.add(btnFuerzaBruta,gbc); gbc.gridy++;
        panel.add(btnDiccionario,gbc);gbc.gridy++;
        panel.add(btnListar, gbc);gbc.gridy++;
        panel.add(btnDetalle, gbc);gbc.gridy++;
        panel.add(btnSha, gbc);gbc.gridy++;
        panel.add(btnSalir, gbc);gbc.gridy++;

        add(panel);

        btnFuerzaBruta.addActionListener(e -> new ventanaFuerzaBruta().setVisible(true));
        btnDiccionario.addActionListener(e -> new ventanaDiccionario().setVisible(true));
        btnListar.addActionListener(e -> new ventanaListarSim().setVisible(true));
        btnDetalle.addActionListener(e -> new ventanaDetalleSim().setVisible(true));
        btnSha.addActionListener(e -> new ventanaSha256().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font ("Segoe UI", Font.PLAIN,16));
        btn.setBackground(new Color(230,240,255));
        btn.setBorder(BorderFactory.createLineBorder(new Color(150,180,220)));
        btn.setPreferredSize(new Dimension(250,40));
        return btn;
    }
}

