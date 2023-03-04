package com.vojat.garden;

public class Game implements Runnable{
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    public static byte[][] map = new byte[8][15];                                                                   // [Y][X] coords  -> Now it's a total of 120 spots to place a flower
    public static String[] textures = {"res/Pics/Water_Can.png", "res/Pics/Red_Tulip.png", "res/Pics/Blue_Rose.png"};              // Array of texture paths
    private GamePanel gamePanel;
    private Thread gameLoop;
    private final int FPS_SET = 120;

    public Game(int panelWidth, int panelHeight) {
        gamePanel = new GamePanel(panelWidth, panelHeight);
        InventoryPanel inventoryPanel = new InventoryPanel(panelWidth, panelHeight, gamePanel, gamePanel.dad);                          // Creates a new InventoryPanel object to pass into the main panel
        MainPanel mainPanel = new MainPanel(gamePanel, inventoryPanel);
        new Window(mainPanel, gamePanel.dad);

        startGameLoop();
        gamePanel.requestFocusInWindow();
        gamePanel.setIPanel(inventoryPanel);
    }

    public void startGameLoop() {                                                                                                       // Method to start the Game Loop
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run() {                                                                                                                 // Game Loop

        double timePerFrame = 1000000000.0 / FPS_SET;
        long now;
        long previousTime = System.nanoTime();
        short fps = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaF = 0;

        while (true) {                                                                                                                  // While this loop runs, the game updates
            now = System.nanoTime();
            
            deltaF += (now - previousTime) / timePerFrame;
            previousTime = now;

            if (deltaF >= 1) {                                                                                                          // Repaints every 120 frames
                gamePanel.repaint();
                fps++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {                                                                       // The FPS counter
                lastCheck = System.currentTimeMillis();
                System.out.println(ANSI_GREEN + "FPS: " + fps + ANSI_RESET);
                fps = 0;
            }
        }
    }

    public static void saveGame() {;}

    public static void loadGame(byte[][] map) {;}
}
