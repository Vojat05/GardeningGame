package com.vojat.garden;

import java.util.HashMap;

public class Game implements Runnable{
    private GamePanel gamePanel;
    private Thread gameLoop;
    public final int FPS_SET = 120;
    private volatile boolean stopGame = false;
    protected byte[][] map = new byte[40][40];      // [Y][X] coords
    private HashMap<Integer, String> decoder = new HashMap<Integer, String>();

    public Game(int panelWidth, int panelHeight) {
        gamePanel = new GamePanel(panelWidth, panelHeight);
        new Window(gamePanel);

        startGameLoop();
        gamePanel.requestFocusInWindow();

        // Filling the decoder for the map area
        decoder.put(0, null);
        decoder.put(1, "Flower");
        decoder.put(2, "Player");
    }

    // Method to start the Game Loop
    public void startGameLoop() {
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    // Game Loop
    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        long now = System.nanoTime();
        long previousTime = System.nanoTime();
        short fps = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaF = 0;

        // While this loop runs, the game updates
        while (!stopGame) {
            now = System.nanoTime();
            
            deltaF += (now - previousTime) / timePerFrame;
            previousTime = now;

            // Repaints every 120 frames
            if (deltaF >= 1) {
                gamePanel.repaint();
                fps++;
                deltaF--;
            }

            // The FPS counter
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + fps);
                fps = 0;
            }
        }
    }

    public void stopGameLoop() {        // This method stops the game loop
        stopGame = true;
    }
}
