package com.xanxa.objectfight;

import com.xanxa.objectfight.ui.JFrameFight;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IniciarJuego extends JFrame {

    public IniciarJuego() {
        setUndecorated(true); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        // Cargar el panel de fondo con la ruta de la imagen
        BackgroundPanel backgroundPanel = new BackgroundPanel("img/brickBreaker.jpg");
        backgroundPanel.setLayout(new GridBagLayout()); 

        // Crear un panel para el botón con borde blanco
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);  
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); 
        buttonPanel.setLayout(new BorderLayout());

        // Configuración del botón
        JButton playButton = new JButton("NUEVA PARTIDA!");
        playButton.setFont(new Font("Arial", Font.BOLD, 60));
        playButton.setOpaque(true); 
        playButton.setBackground(Color.BLACK); 
        playButton.setBorder(null); 
        playButton.setForeground(Color.RED); 

        playButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                playButton.setForeground(Color.GREEN); // Color al pasar el mouse
            }

            public void mouseExited(MouseEvent evt) {
                playButton.setForeground(Color.RED); // Color al salir el mouse
            }
        });

        // Acción al hacer clic en el botón
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrameFight frameFights = new JFrameFight();
                frameFights.setVisible(true);
                dispose(); 
            }
        });

        // Agregar el botón al panel con el borde
        buttonPanel.add(playButton, BorderLayout.CENTER);

        // Establecer las restricciones de GridBagLayout para ubicar el botón
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(-120, 0, 0, 0); // Espaciado del botón
        backgroundPanel.add(buttonPanel, gbc); // Añadir el panel con borde al fondo

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new IniciarJuego();
            }
        });
    }
    
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            // Intentar cargar la imagen de fondo desde el path especificado
            try {
                backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(imagePath)).getImage();
                if (backgroundImage == null) {
                    throw new IllegalArgumentException("La imagen de fondo no se pudo cargar. Verifica el path: " + imagePath);
                }
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen de fondo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
