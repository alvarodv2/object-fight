package com.xanxa.objectfight.game;

import com.xanxa.objectfight.game.gameobject.Ball;
import com.xanxa.objectfight.game.gameobject.GameObject;
import com.xanxa.objectfight.game.gameobject.Player;
import com.xanxa.objectfight.game.gameobject.Wall;
import com.xanxa.objectfight.ui.GameUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import com.xanxa.objectfight.GameOver;


public class GameManager {

    private List<GameObject> gameObjects;
    private int points = 0;
    private Rectangle gameZone;
    private double rightPushed = 0;
    private double leftPushed = 0;
    private int playerLives = 5;
    private int wallsNumber = 0;
    private int ballsNumber = 0;
    private static final int MAX_LEVEL = 5;
    private int currentLevel = 1;
    private boolean gameOver = false;

    // Velocidades base ajustadas para cada nivel
    private static final double[] VELOCIDAD_BOLA = {3.0, 5.0, 6.0, 7.0, 9.0};

    private static final double BALL_INITIAL_X_OFFSET = 0.5;
    private static final double BALL_INITIAL_Y_OFFSET = -30;

    public GameManager() {
        this.gameObjects = new ArrayList<>();
    }

    public void update(Graphics g) {
        for (GameObject gameObject : gameObjects) {
            gameObject.paint(g);
        }
        GameUI.drawGameInfo(g, currentLevel, playerLives, points);
    }

    public void fixedUpdate() {
        if (gameOver) {
            return;
        }

        boolean isInside = true;
        List<Player> players = new ArrayList<>();
        wallsNumber = 0;
        ballsNumber = 0;

        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject actual = gameObjects.get(i);
            actual.behaviour();
            List<GameObject> collisions = GameObject.collision(actual, gameObjects);
            boolean isInsideTmp = solveCollision(actual, collisions);

            if (!isInsideTmp) {
                isInside = false;
            }

            if (actual instanceof Player) {
                Player player = (Player) actual;
                player.updateSpeed(rightPushed, leftPushed);
                players.add(player);
            }

            if (!actual.isAlive()) {
                gameObjects.remove(i);
            } else if (actual instanceof Wall) {
                wallsNumber++;
            } else if (actual instanceof Ball) {
                ballsNumber++;
            }
        }

        if (!isInside) {
            handleBallLost();
        }

        if (wallsNumber == 0) {
            nextLevel();
        }
    }

    private boolean solveCollision(GameObject actual, List<GameObject> collided) {
        boolean inside = true;
        if (actual instanceof Ball) {
            Ball ball = (Ball) actual;
            for (GameObject gameObject : collided) {
                if (gameObject instanceof Wall) {
                    Wall block = (Wall) gameObject;
                    block.touched();
                    // PUNTOS POR COLISION (5)
                    points += 5;
                    ball.goAway(block);
                } else if (gameObject instanceof Player) {
                    Player player = (Player) gameObject;
                    ball.goAway(player);
                }
            }
            inside = checkBallInside(ball);
        }
        return inside;
    }

    private boolean checkBallInside(Ball ball) {
        boolean isInside = true;
        if (!gameZone.contains(ball.getCollider().getRectangle())) {
            Point2D ballLeft = ball.getLeft();
            double velocidadInicial = Math.sqrt(
                    ball.getSpeedX() * ball.getSpeedX()
                    + ball.getSpeedY() * ball.getSpeedY()
            );

            if (ballLeft.getX() < 0) {
                double ballSpeed = ball.getSpeedX();
                if (ballSpeed < 0) {
                    ball.setSpeedX(-ballSpeed);
                }
            } else if (ball.getTop().getY() < 0) {
                double ballSpeed = ball.getSpeedY();
                if (ballSpeed < 0) {
                    ball.setSpeedY(-ballSpeed);
                }
            } else if (ball.getRight().getX() > gameZone.getWidth()) {
                double ballSpeed = ball.getSpeedX();
                if (ballSpeed > 0) {
                    ball.setSpeedX(-ballSpeed);
                }
            } else if (ball.getBottom().getY() > gameZone.getHeight()) {
                ball.setAlive(false);
                isInside = false;
            }

            // Normalizamos la velocidad después de rebotar en los bordes
            double velocidadActual = Math.sqrt(
                    ball.getSpeedX() * ball.getSpeedX()
                    + ball.getSpeedY() * ball.getSpeedY()
            );

            if (velocidadActual > 0 && isInside) {
                double factor = velocidadInicial / velocidadActual;
                ball.setSpeedX(ball.getSpeedX() * factor);
                ball.setSpeedY(ball.getSpeedY() * factor);
            }
        }
        return isInside;
    }

    private void handleBallLost() {
    playerLives--;
    if (playerLives <= 0) {
        gameOver = true;
        
        int puntosFinales = points;

        // Iniciar la ventana GameOver en el hilo de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameOver(puntosFinales).setVisible(true);
            }
        });

        return;
    }
    resetBall();
}


    private void resetBall() {
        removeExtraBalls();

        for (GameObject obj : gameObjects) {
            if (obj instanceof Player) {
                Player player = (Player) obj;

                double ballX = player.getX() + player.getWidth() * BALL_INITIAL_X_OFFSET;
                double ballY = player.getY() + BALL_INITIAL_Y_OFFSET;

                Ball newBall = new Ball(ballX, ballY, 20, 20, Color.BLACK);
                gameObjects.add(newBall);
                break;
            }
        }
    }

    private void removeExtraBalls() {
        gameObjects.removeIf(obj -> obj instanceof Ball);
    }

    private void nextLevel() {
        currentLevel++;
        if (currentLevel > MAX_LEVEL) {
            gameOver = true;
            return;
        }

        gameObjects.removeIf(obj -> obj instanceof Wall);
        LevelGenerator.generateLevel(this, currentLevel);
        resetBall();
    }

    // Getters y setters
    public Rectangle getGameZone() {
        return gameZone;
    }

    public void setGameZone(Rectangle gameZone) {
        this.gameZone = gameZone;
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public void rightPushed(boolean pushed) {
        rightPushed = pushed ? 1 : 0;
    }

    public void leftPushed(boolean pushed) {
        leftPushed = pushed ? 1 : 0;
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getPoints() {
        return points;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}
