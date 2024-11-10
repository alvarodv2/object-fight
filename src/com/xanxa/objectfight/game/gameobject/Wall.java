package com.xanxa.objectfight.game.gameobject;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa una pared en el juego.
 */
public class Wall extends GameObject {
    int lives;
    int maxLives;
    Color color;
    Color borderColor;

    public Wall(double x, double y, double width, double height, int lives, Color color) {
        super(x, y, width, height);
        this.lives = lives;
        this.maxLives = lives;
        this.color = color;
        this.borderColor = color;
    }

    @Override
    public boolean paint(Graphics g) {
        Color tmp = g.getColor();
        g.setColor(color);
        double tmpX = getX();
        double tmpY = getY();
        double width = getWidth();
        double height = getHeight();
        g.fillRect((int) tmpX, (int) tmpY, (int) width, (int) height);
        g.setColor(borderColor);
        g.drawRect((int) tmpX, (int) tmpY, (int) width, (int) height);
        super.paint(g);
        g.setColor(tmp);
        return isAlive();
    }

    @Override
    public boolean isAlive() {
        return lives > 0;
    }

    public void touched() {
        lives--;
        int alpha = (int) (255f * (float) lives / (float) maxLives);
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 255) {
            alpha = 255;
        }
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    public String toString() {
        return "Wall [" + ((int) getX()) + ", " + ((int) getY()) + ", " + ((int) getWidth()) + ", " + ((int) getHeight()) + "]";
    }
}
