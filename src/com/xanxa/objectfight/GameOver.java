package com.xanxa.objectfight;

import com.xanxa.objectfight.ui.JFrameFight;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOver extends JFrame {

    public GameOver(int puntos) { // Acepta la puntuación como parámetro
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        BackgroundPanel backgroundPanel = new BackgroundPanel(); // Panel de fondo negro
        backgroundPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("<html><span style='color:white;'>BRICK </span><span style='color:yellow;'>BREAKER</span></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);  
        titleLabel.setForeground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(titleLabel, gbc);

        // JLabel para mostrar la puntuación en color azul
        JLabel scoreLabel = new JLabel("PUNTUACIÓN: " + puntos);  
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 50));
        scoreLabel.setForeground(Color.BLUE);  
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0); 
        backgroundPanel.add(scoreLabel, gbc);  

        // Botón para reiniciar el juego
        JButton restartButton = new JButton("REINICIAR PARTIDA");
        restartButton.setFont(new Font("Arial", Font.BOLD, 50));
        restartButton.setOpaque(false);
        restartButton.setBorder(null);
        restartButton.setContentAreaFilled(false);
        restartButton.setForeground(Color.RED);

        restartButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                restartButton.setForeground(Color.GREEN); 
            }

            public void mouseExited(MouseEvent evt) {
                restartButton.setForeground(Color.RED); 
            }
        });

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrameFight frameFights = new JFrameFight();
                frameFights.setVisible(true);
                dispose();
            }
        });
        
        gbc.gridy = 2; 
        gbc.insets = new Insets(20, 0, 10, 0); 
        backgroundPanel.add(restartButton, gbc);

        // Botón para salir del juego
        JButton exitButton = new JButton("SALIR");
        exitButton.setFont(new Font("Arial", Font.BOLD, 40));
        exitButton.setOpaque(false);
        exitButton.setBorder(null);
        exitButton.setContentAreaFilled(false);
        exitButton.setForeground(Color.RED);

        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                exitButton.setForeground(Color.GREEN); 
            }

            public void mouseExited(MouseEvent evt) {
                exitButton.setForeground(Color.RED); 
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        gbc.gridy = 3;
        backgroundPanel.add(exitButton, gbc);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Suponiendo que tienes una puntuación de ejemplo para pasar
                int ejemploPuntos = 100; 
                new GameOver(ejemploPuntos).setVisible(true);
            }
        });
    }
}

class BackgroundPanel extends JPanel {
    public BackgroundPanel() {
        setBackground(Color.BLACK);  // Fondo negro
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
