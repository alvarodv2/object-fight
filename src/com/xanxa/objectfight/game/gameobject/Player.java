package com.xanxa.objectfight.game.gameobject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

/**
 * Representa el jugador en el juego.
 */
public class Player extends GameObject {
    private Color color;
    private double speedX = 0;
    private double accelerationX = 0.3;
    private int lessLife = 50;
    private double maxSpeed = 8;

    public Player(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;
   
    }

    @Override
    public boolean paint(Graphics g) {
        Color tmpColor = g.getColor();
        g.setColor(color);
        double x = getX();
        double y = getY();
        double width = getWidth();
        double height = getHeight();
        g.fillRect((int) x, (int) y, (int) width, (int) height);
        g.setColor(tmpColor);
        super.paint(g);
        return true;
    }

    @Override
    public boolean behaviour() {
        boolean result = super.behaviour();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();

        double newX = getX() + speedX;

        // Asegurar que el jugador no salga de los límites de la pantalla
        if (newX < 0) {
            newX = 0;
        } else if (newX + getWidth() > screenWidth) {
            newX = screenWidth - getWidth();
        }

        setX(newX);
        return result;
    }

    @Override
    public boolean isAlive() {
        return color.getAlpha() > 0; // El jugador "muere" cuando el alpha es 0
    }

    /**
     * Actualiza la velocidad del jugador en función de la entrada.
     *
     * @param rightPushed si se está presionando hacia la derecha
     * @param leftPushed si se está presionando hacia la izquierda
     */
    public void updateSpeed(double rightPushed, double leftPushed) {
        if (rightPushed > 0) {
            speedX += accelerationX;
            if (speedX > maxSpeed) {
                speedX = maxSpeed;
            }
        } else if (leftPushed > 0) {
            speedX -= accelerationX;
            if (speedX < -maxSpeed) {
                speedX = -maxSpeed;
            }
        } else {
            // Desaceleración gradual cuando no hay entrada
            if (speedX > 0) {
                speedX -= accelerationX;
                if (speedX < 0) speedX = 0; // Asegura que no pase a negativo
            } else if (speedX < 0) {
                speedX += accelerationX;
                if (speedX > 0) speedX = 0; // Asegura que no pase a positivo
            }
        }
    }

    @Override
    public String toString() {
        return "Player [" + ((int) getX()) + ", " + ((int) getY()) + ", " + ((int) getWidth()) + ", " + ((int) getHeight()) + "]";
    }

    /**
     * Reduce la "vida" del jugador reduciendo la opacidad (alpha) del color.
     */
    public void touched() {
        int alpha = color.getAlpha() - lessLife;
        if (alpha < 0) {
            alpha = 0;
        }
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    // Getters y setters

    public double getSpeedX() {
        return speedX;
    }

    public void setLessLife(int lessLife) {
        this.lessLife = lessLife;
    }
}
