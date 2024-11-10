package com.xanxa.objectfight.game;

import com.xanxa.objectfight.game.gameobject.Wall;
import java.awt.Color;
import java.util.Random;

public class LevelGenerator {
    private static final Random random = new Random();

    public static void generateLevel(GameManager gameManager, int level) {
        switch (level) {
            case 1:
                generateBasicLevel(gameManager, level);
                break;
            case 2:
                generateSpiralLevel(gameManager, level);
                break;
            case 3:
                generateRandomLevel(gameManager, level);
                break;
            case 4:
                generateDiagonalLevel(gameManager, level);
                break;
            case 5:
                generateComplexLevel(gameManager, level);
                break;
            default:
                generateBasicLevel(gameManager, level);
                break;
        }
    }

    private static void generateBasicLevel(GameManager gameManager, int level) {
        int rows = Math.min(3 + level, 8);
        int wallsPerRow = 8;
        int wallWidth = 60;
        int wallHeight = 20;
        int spacing = 10;
        int startX = (gameManager.getGameZone().width - (wallsPerRow * (wallWidth + spacing))) / 2;
        int startY = 50;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < wallsPerRow; col++) {
                int x = startX + col * (wallWidth + spacing);
                int y = startY + row * (wallHeight + spacing);
                int resistance = Math.min(1 + (level / 2), 5);
                Color wallColor = getColorByResistance(resistance);
                Wall wall = new Wall(x, y, wallWidth, wallHeight, resistance, wallColor);
                gameManager.addGameObject(wall);
            }
        }
    }

    private static void generateSpiralLevel(GameManager gameManager, int level) {
        int wallWidth = 60;
        int wallHeight = 20;
        int totalWalls = 30 + (level * 5);
        for (int i = 0; i < totalWalls; i++) {
            double angle = (i * Math.PI) / 15;
            int x = (int)(gameManager.getGameZone().width/2 + Math.cos(angle) * 200);
            int y = 100 + (int)(Math.sin(angle) * 100);
            int resistance = Math.min(1 + (level / 2), 5);
            Color wallColor = getColorByResistance(resistance);
            Wall wall = new Wall(x, y, wallWidth, wallHeight, resistance, wallColor);
            gameManager.addGameObject(wall);
        }
    }

    private static void generateRandomLevel(GameManager gameManager, int level) {
        int wallWidth = 60;
        int wallHeight = 20;
        int totalWalls = 40 + (level * 8);
        for (int i = 0; i < totalWalls; i++) {
            int x = random.nextInt((int)gameManager.getGameZone().getWidth() - wallWidth);
            int y = random.nextInt((int)gameManager.getGameZone().getHeight() - wallHeight - 100) + 100;
            int resistance = Math.min(1 + (level / 2), 5);
            Color wallColor = getColorByResistance(resistance);
            Wall wall = new Wall(x, y, wallWidth, wallHeight, resistance, wallColor);
            gameManager.addGameObject(wall);
        }
    }

    private static void generateDiagonalLevel(GameManager gameManager, int level) {
        int wallWidth = 60;
        int wallHeight = 20;
        int spacing = 30;
        int totalWalls = 20 + (level * 5);
        int startX = (int)gameManager.getGameZone().getWidth() / 2 - (totalWalls / 2) * (wallWidth + spacing);
        int startY = 100;
        for (int i = 0; i < totalWalls; i++) {
            int x = startX + i * (wallWidth + spacing);
            int y = startY + i * (wallHeight + spacing);
            int resistance = Math.min(1 + (level / 2), 5);
            Color wallColor = getColorByResistance(resistance);
            Wall wall = new Wall(x, y, wallWidth, wallHeight, resistance, wallColor);
            gameManager.addGameObject(wall);
        }
    }

    private static void generateComplexLevel(GameManager gameManager, int level) {
        int wallWidth = 40;
        int wallHeight = 40;
        int totalWalls = 25 + (level * 5);
        int centerX = (int)gameManager.getGameZone().getWidth() / 2;
        int centerY = (int)gameManager.getGameZone().getHeight() / 2;
        for (int i = 0; i < totalWalls; i++) {
            double angle = (i * 2 * Math.PI) / totalWalls;
            int x = (int)(centerX + Math.cos(angle) * 200);
            int y = (int)(centerY + Math.sin(angle) * 150);
            int resistance = Math.min(1 + (level / 2), 5);
            Color wallColor = getColorByResistance(resistance);
            Wall wall = new Wall(x, y, wallWidth, wallHeight, resistance, wallColor);
            gameManager.addGameObject(wall);
        }
    }

    private static Color getColorByResistance(int resistance) {
        switch(resistance) {
            case 1: return Color.GREEN;
            case 2: return Color.YELLOW;
            case 3: return Color.ORANGE;
            case 4: return Color.RED;
            default: return Color.MAGENTA;
        }
    }
}