/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.xanxa.objectfight.game.gameobject;

import com.xanxa.objectfight.game.colliders.Collider;
import com.xanxa.objectfight.game.colliders.RectangleCollider;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manuel
 */
public abstract class GameObject{
    protected Collider col;

    public GameObject(double x, double y, double width, double height) {
        this.col = new RectangleCollider(x, y, width, height);
    }

    /**
     * Check if there is any collision an behaves.
     * @param actual
     * @param gameObjects
     * @return A list of gameObjects that collides with the actual GameObject
     */
    public static List<GameObject> collision(GameObject actual, List<GameObject> gameObjects)
    {
        List<GameObject> result = new ArrayList<GameObject>();
        for (GameObject gameObject : gameObjects)
        {
            if (actual != gameObject && actual.collides (gameObject))
            {
                result.add(gameObject);
            }
        }
        return result;
    }


    /**
     *
     * @param g
     * @return true if the GameObject still Alive
     */
    public boolean paint (Graphics g)
    {
        col.paintDebug (g);
        return true;
    }

    public boolean behaviour ()
    {
        return isAlive();
    }

    public abstract boolean isAlive ();

    //public abstract checkCollision (Collider collider);

    public boolean collides (GameObject checkCollision)
    {
       
        boolean collides = this.col.collide(checkCollision.getCollider());

        return collides;
    }

    public Collider getCollider() {
        return col;
    }

    public double getX ()
    {
        return col.getX();
    }

    public double getY ()
    {
        return col.getY();
    }


    public double getWidth() {
        return col.getWidth();
    }



    public double getHeight ()
    {
        return col.getHeight();
    }

    public void updatePosition (double x, double y)
    {
        setX(x);
        setY(y);
    }

    public void setX (double x)
    {
        col.setX(x);
    }

    public void setY (double y)
    {
        col.setY(y);
    }

    public void setWidth (double width)
    {
        col.setWidth(width);
    }

    public void setHeight (double height)
    {
        col.setHeight(height);
    }

    /**
     *
     * @return
     */
    public Point2D getLeft ()
    {
        double x = this.getX()-this.getWidth();
        Point2D point = new Point2D.Double(x, getY());
        return point;
    }

    /**
     *
     * @return
     */
    public Point2D getRight ()
    {
        double x = this.getX()+this.getWidth();
        Point2D point = new Point2D.Double(x, getY());
        return point;
    }

    /**
     *
     * @return
     */
    public Point2D getBottom ()
    {
        double y = this.getY()+this.getHeight();
        Point2D point = new Point2D.Double(getX(), y);
        return point;
    }

    /**
     *
     * @return
     */
    public Point2D getTop ()
    {
        double y = this.getY();
        Point2D point = new Point2D.Double(getX(), y);
        return point;
    }


}
