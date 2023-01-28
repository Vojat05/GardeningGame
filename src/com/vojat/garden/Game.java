package com.vojat.garden;

public class Game implements Runnable{
    private GamePanel gamePanel;
    private Thread gameLoop;
    public final int FPS_SET = 120;
    private volatile boolean stopGame = false;
    protected byte[][] map = new byte[40][40];

    public Game(int panelWidth, int panelHeight) {
        gamePanel = new GamePanel(panelWidth, panelHeight);
        new Window(gamePanel);

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
        while (!stopGame) {
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

    public void stopGameLoop() {        // This method stops the game loop
        stopGame = true;
    }
}
