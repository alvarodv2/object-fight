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

public class GameManager {
    private List<GameObject> gameObjects;
    private int points = 0;
    private Rectangle gameZone;
    private double rightPushed = 0;
    private double leftPushed = 0;
    private int playerLives = 3;
    private int wallsNumber = 0;
    private int ballsNumber = 0;
    private static final int MAX_LEVEL = 5;
    private int currentLevel = 1;
    private boolean gameOver = false;

    // Velocidades fijas para cada nivel
    private static final double[] VELOCIDAD_BOLA = {0.3, 1.2, 1.5, 1.8, 2.0};

    // Coordenadas de inicio de la bola (en relación con el jugador)
    private static final double BALL_INITIAL_X_OFFSET = 0.5;  // La bola se coloca en el centro del jugador
    private static final double BALL_INITIAL_Y_OFFSET = -30;   // La bola se coloca encima del jugador

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
        if (gameOver) return;

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
                isInside = isInsideTmp;
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
                    points += 10;

                    // Preservar la magnitud de la velocidad al rebotar
                    double velocidadActual = Math.sqrt(
                            ball.getSpeedX() * ball.getSpeedX() +
                                    ball.getSpeedY() * ball.getSpeedY()
                    );
                    ball.goAway(block);

                    // Normalizar la nueva dirección para mantener la misma velocidad
                    double nuevaVelocidad = Math.sqrt(
                            ball.getSpeedX() * ball.getSpeedX() +
                                    ball.getSpeedY() * ball.getSpeedY()
                    );

                    if (nuevaVelocidad > 0) {
                        double factor = velocidadActual / nuevaVelocidad;
                        ball.setSpeedX(ball.getSpeedX() * factor);
                        ball.setSpeedY(ball.getSpeedY() * factor);
                    }

                } else if (gameObject instanceof Player) {
                    Player player = (Player) gameObject;
                    if (ball.goAway(player)) {
                        // Preservar la magnitud de la velocidad
                        double velocidadActual = Math.sqrt(
                                ball.getSpeedX() * ball.getSpeedX() +
                                        ball.getSpeedY() * ball.getSpeedY()
                        );

                        // Después de que la bola rebote
                        double nuevaVelocidad = Math.sqrt(
                                ball.getSpeedX() * ball.getSpeedX() +
                                        ball.getSpeedY() * ball.getSpeedY()
                        );

                        if (nuevaVelocidad > 0) {
                            double factor = velocidadActual / nuevaVelocidad;
                            ball.setSpeedX(ball.getSpeedX() * factor);
                            ball.setSpeedY(ball.getSpeedY() * factor);
                        }
                    }
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
        }
        return isInside;
    }

    private void handleBallLost() {
        playerLives--;
        if (playerLives <= 0) {
            gameOver = true;
            return;
        }
        resetBall();
    }

    private void resetBall() {
        // Remover todas las bolas excepto una
        removeExtraBalls();

        for (GameObject obj : gameObjects) {
            if (obj instanceof Player) {
                Player player = (Player) obj;

                // Colocamos la bola en el mismo lugar en relación con el jugador
                double ballX = player.getX() + player.getWidth() * BALL_INITIAL_X_OFFSET;
                double ballY = player.getY() + BALL_INITIAL_Y_OFFSET;

                Ball newBall = new Ball(ballX, ballY, 20, 20, Color.RED);

                // Calculamos la velocidad base para este nivel
                double velocidadBase = VELOCIDAD_BOLA[currentLevel - 1];

                // Aplicamos un vector de velocidad normalizado
                double velocidadTotal = 8.0; // Magnitud total del vector de velocidad
                double angle = Math.PI / 4; // 45 grados en radianes

                // Calculamos las componentes X e Y manteniendo la proporción
                newBall.setSpeedX(velocidadTotal * velocidadBase * Math.cos(angle));
                newBall.setSpeedY(-velocidadTotal * velocidadBase * Math.sin(angle));

                gameObjects.add(newBall);
                break;
            }
        }
    }

    private void removeExtraBalls() {
        int ballCount = 0;
        // Iteramos sobre los objetos de juego y contamos las bolas
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject obj = gameObjects.get(i);
            if (obj instanceof Ball) {
                ballCount++;
                // Si hay más de una bola, la eliminamos
                if (ballCount > 1) {
                    gameObjects.remove(i);
                }
            }
        }
    }

    private void nextLevel() {
        currentLevel++;
        if (currentLevel > MAX_LEVEL) {
            gameOver = true;
            return;
        }

        // Limpiar objetos y reiniciar el nivel
        gameObjects.removeIf(obj -> obj instanceof Wall);
        LevelGenerator.generateLevel(this, currentLevel);
        resetBall(); // Reiniciar la bola con la velocidad fija del nivel
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
