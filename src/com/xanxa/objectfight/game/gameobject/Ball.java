package com.xanxa.objectfight.game.gameobject;
import com.xanxa.objectfight.game.colliders.Collider;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa la bola en el juego.
 */
public class Ball extends GameObject {
    private Color color;
    private boolean alive = true;
    private double speedX = -1, speedY = -1; 
    private double acceleration = 0.01; 

    public Ball(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = Color.WHITE;
        this.speedX = 5.0;
        this.speedY = -8.0;
    }

    @Override
    public boolean paint(Graphics g) {
        Color previousColor = g.getColor();
        g.setColor(color);
        double tmpX = col.getX();
        double tmpY = col.getY();
        double width = col.getWidth();
        double height = col.getHeight();
        g.fillOval((int) tmpX, (int) tmpY, (int) width, (int) height);
        g.setColor(previousColor);
        super.paint(g);
        return isAlive();
    }

    @Override
    public boolean behaviour() {
        boolean result = super.behaviour();
        speedX += acceleration;
        speedY += acceleration;
        double x = getX();
        double y = getY();
        x += speedX; 
        y += speedY; 
        updatePosition(x, y);
        return result;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean goAway(GameObject block) {
        boolean normalCollision = false;
        col.setDebugColor(Color.CYAN);
        double x = this.col.getX();
        double y = this.col.getY();
        double width = this.col.getWidth();
        double height = this.col.getHeight();
        Collider blockCollider = block.getCollider();

        if (blockCollider.collide(getBottom())) {
            if (speedY > 0) speedY = -speedY;
            normalCollision = true;
        } else if (blockCollider.collide(getTop())) {
            if (speedY < 0) speedY = -speedY;
        } else if (blockCollider.collide(getLeft())) {
            if (speedX < 0) speedX = -speedX;
        } else if (blockCollider.collide(getRight())) {
            if (speedX > 0) speedX = -speedX;
        }

        return normalCollision;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    @Override
    public String toString() {
        return "Ball [" + ((int) getX()) + ", " + ((int) getY()) + ", " + ((int) getWidth()) + ", " + ((int) getHeight()) + "]";
    }
}
