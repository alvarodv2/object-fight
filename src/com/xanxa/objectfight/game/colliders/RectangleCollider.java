package com.xanxa.objectfight.game.colliders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class RectangleCollider implements Collider {
    private double x, y, width, height;
    private Color debugColor = Color.RED;

    public RectangleCollider(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean collide(Collider collider) {
        return this.getRectangle().intersects(collider.getRectangle());
    }

    @Override
    public boolean collide(Point2D point) {
        return getRectangle().contains(point);
    }

    @Override
    public void updatePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public void paintDebug(Graphics g) {
        // Eliminamos para evitar el borde rojo
        /*
        g.setColor(debugColor);
        g.drawRect((int) x, (int) y, (int) width, (int) height);
        */
    }

    @Override
    public void setDebugColor(Color c) {
        this.debugColor = c;
    }

    @Override
    public Point2D getLeft() {
        return new Point2D.Double(x, y + height / 2);
    }

    @Override
    public Point2D getRight() {
        return new Point2D.Double(x + width, y + height / 2);
    }

    @Override
    public Point2D getBottom() {
        return new Point2D.Double(x + width / 2, y + height);
    }

    @Override
    public Point2D getTop() {
        return new Point2D.Double(x + width / 2, y);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }
}
