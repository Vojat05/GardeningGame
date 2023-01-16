package com.vojat.garden;

public class Game implements Runnable{
    private Window gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoop;
    public final int FPS_SET = 120;

    public Game(int panelWidth, int panelHeight) {
        gamePanel = new GamePanel(panelWidth, panelHeight);
        gameWindow = new Window(gamePanel, panelWidth, panelHeight);

        startGameLoop();
        gamePanel.requestFocusInWindow();
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
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();
        short fps = 0;
        long lastCheck = System.currentTimeMillis();

        // While this loop runs, the game updates
        while (true) {
            now = System.nanoTime();

            // Repaints every 120 frames
            if (now - lastFrame >= timePerFrame) {
                gamePanel.repaint();
                lastFrame = now;
                fps++;
            }

            // The FPS counter
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + fps);
                fps = 0;
            }
        }
    }
}
