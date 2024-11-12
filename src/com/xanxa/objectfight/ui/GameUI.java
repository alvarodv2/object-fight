package com.xanxa.objectfight.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GameUI {
    private static final Font LEVEL_FONT = new Font("Monospaced", Font.BOLD, 24);
    private static final Font LIVES_FONT = new Font("Monospaced", Font.BOLD, 20);

    public static void drawGameInfo(Graphics g, int level, int lives, int points) {
        g.setFont(LEVEL_FONT);
        g.setColor(Color.WHITE);

        // Dibujar nivel
        String levelText = "NIVEL " + level;
        g.drawString(levelText, 20, 30);

        // Dibujar vidas
        g.setFont(LIVES_FONT);
        String livesText = "♥" + lives;
        g.setColor(Color.RED);
        g.drawString(livesText, 20, 60);

        // Dibujar puntos
        g.setColor(Color.BLUE);
        String pointsText = "PUNTOS: " + points;
        g.drawString(pointsText, 20, 90);
    }
}