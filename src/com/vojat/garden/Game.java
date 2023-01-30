package com.vojat.garden;

//import java.util.HashMap;

public class Game implements Runnable{
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    private GamePanel gamePanel;
    private Thread gameLoop;
    public final int FPS_SET = 120;
    private volatile boolean stopGame = false;

    protected byte[][] map = new byte[16][30];      // [Y][X] coords  -> Now it's a total of 480 spots to place a flower
    // private HashMap<Integer, String> decoder = new HashMap<Integer, String>();

    public Game(int panelWidth, int panelHeight) {
        gamePanel = new GamePanel(panelWidth, panelHeight);
        new Window(gamePanel);

        startGameLoop();
        gamePanel.requestFocusInWindow();

        // Filling the decoder for the map area
        // decoder.put(0, null);
        // decoder.put(1, "Flower");
        // decoder.put(2, "Player");
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
        long now;
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
                System.out.println(ANSI_GREEN + "FPS: " + fps + ANSI_RESET);
                fps = 0;
            }
        }
    }

    public void stopGameLoop() {        // This method stops the game loop
        stopGame = true;
    }
}
