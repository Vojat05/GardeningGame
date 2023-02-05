package com.vojat.garden;

//import java.util.HashMap;

public class Game implements Runnable{
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    private GamePanel gamePanel;
    private Thread gameLoop;
    private final int FPS_SET = 120;

    public Game(int panelWidth, int panelHeight) {
        gamePanel = new GamePanel(panelWidth, panelHeight);
        InventoryPanel inventoryPanel = new InventoryPanel(panelWidth, panelHeight);
        MainPanel mainPanel = new MainPanel(gamePanel, inventoryPanel);
        new Window(mainPanel, gamePanel.dad);

        startGameLoop();
        gamePanel.requestFocusInWindow();
    }

    public void startGameLoop() {       // Method to start the Game Loop
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run() {     // Game Loop

        double timePerFrame = 1000000000.0 / FPS_SET;
        long now;
        long previousTime = System.nanoTime();
        short fps = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaF = 0;

        while (true) {     // While this loop runs, the game updates
            now = System.nanoTime();
            
            deltaF += (now - previousTime) / timePerFrame;
            previousTime = now;

            if (deltaF >= 1) {      // Repaints every 120 frames
                gamePanel.repaint();
                fps++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {       // The FPS counter
                lastCheck = System.currentTimeMillis();
                System.out.println(ANSI_GREEN + "FPS: " + fps + ANSI_RESET);
                fps = 0;
            }
        }
    }
}
