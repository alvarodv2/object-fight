/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.xanxa.objectfight.game.colliders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 *
 * @author Manuel
 */
public interface Collider {
    /**
     * 
     * @param collider
     * @return 
     */
    public boolean collide(Collider collider);
    
    public boolean collide(Point2D point);
    /**
     * 
     * @param x
     * @param y 
     */
    public void updatePosition (double x, double y);
    
    /**
     * 
     * @return 
     */
    public double getWidth();
    
    /**
     * 
     * @return 
     */
    public double getHeight();
    
    /**
     * 
     * @return 
     */
    public double getX();
    
    /**
     * 
     * @return 
     */
    public double getY();
    
    /**
     * 
     * @param x 
     */
    public void setX (double x);
    /**
     * 
     * @param y 
     */
    public void setY (double y);
   
    /**
     * 
     * @param width 
     */
    public void setWidth (double width);
    
    /**
     * 
     * @param height 
     */
    public void setHeight (double height);

    public void paintDebug(Graphics g);
    
    public void setDebugColor(Color c);
    
    /**
     * 
     * @return 
     */
    public Point2D getLeft ();
    
    /**
     * 
     * @return 
     */
    public Point2D getRight ();
    
    /**
     *
     * @return 
     */
    public Point2D getBottom ();
    
    /**
     * 
     * @return 
     */
    public Point2D getTop ();
    
    public Rectangle getRectangle ();
}
