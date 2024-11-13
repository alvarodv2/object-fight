package com.xanxa.objectfight.ui;

import com.xanxa.objectfight.game.GameManager;
import com.xanxa.objectfight.game.LevelGenerator;
import com.xanxa.objectfight.game.gameobject.Ball;
import com.xanxa.objectfight.game.gameobject.Player;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Clase principal que gestiona la ventana del juego.
 */
public class JFrameFight extends JFrame {

    private final double FPMILLIS = 60.0 / 1000.0; // 60fps
    private final double TIME_DIFF_STANDARD = 1 / FPMILLIS; 
    private long lastUpdateTime = 0;
    private GameManager manager;

    public JFrameFight() throws HeadlessException {
        super();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage("");
        Point cursorHotSpot = new Point(0, 0);
        Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "InvisibleCursor");
        this.setCursor(invisibleCursor);
        
        JPanel panel = new JPanel() {

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                initGameObjects(this);
                manager.fixedUpdate();
                manager.update(g);

                // Dibujar UI
                GameUI.drawGameInfo(g, manager.getCurrentLevel(), manager.getPlayerLives(), manager.getPoints());

                checkFPSToRepaint();
            }

            /**
             * Chequea los Frames por segundo y en caso de necesitar llama a
             * repaint o espera.
             */
            public void checkFPSToRepaint() {
                long actualTime = System.currentTimeMillis();
                double timeDiff = actualTime - lastUpdateTime;
                lastUpdateTime = actualTime;

                if (timeDiff > TIME_DIFF_STANDARD) {
                    repaint();
                } else {
                    double timeToWait = TIME_DIFF_STANDARD - timeDiff;
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep((long) timeToWait);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(JFrameFight.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            repaint();
                        }
                    }.start();
                }
            }
        };

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_RIGHT) {
                    JFrameFight.this.manager.rightPushed(true);
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    JFrameFight.this.manager.leftPushed(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_RIGHT) {
                    JFrameFight.this.manager.rightPushed(false);
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    JFrameFight.this.manager.leftPushed(false);
                }
            }
        });

        // Hacer que el panel pueda escuchar teclas
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);

    }

    /**
     * Inicializa los objetos del juego (jugador, pelota y nivel inicial).
     *
     * @param panel El panel donde se dibujarán los objetos.
     */
    public void initGameObjects(JPanel panel) {
        if (manager == null) {
            manager = new GameManager();
        }
        if (manager.getGameZone() == null) {
            // Establecer la zona de juego
            manager.setGameZone(new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight()));
            panel.setBackground(Color.BLACK);

            // Crear jugador
            double playerX = this.getWidth() / 2 - 100;
            double playerY = this.getHeight() - 150;
            double playerWidth = 300;
            double playerHeight = 20;
            Player player = new Player(playerX, playerY, playerWidth, playerHeight, Color.BLUE);

            // Crear pelota
            Ball ball = new Ball(playerX + playerWidth / 2, playerY - 30, 20, 20, Color.WHITE);

            manager.addGameObject(player);
            manager.addGameObject(ball);
            
            LevelGenerator.generateLevel(manager, 1);
        }
    }
}
