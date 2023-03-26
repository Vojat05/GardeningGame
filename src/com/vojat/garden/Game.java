package com.vojat.garden;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.vojat.Main;
import com.vojat.menu.Window;

public class Game implements Runnable{
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    public ArrayList<Flower> flowers = new ArrayList<>();
    public static byte[][] map = new byte[8][15];                                                                   // [Y][X] coords  -> Now it's a total of 120 spots to place a flower
    public static String[] textures = {"res/Pics/Water_Can.png", "res/Pics/Red_Tulip.png", "res/Pics/Blue_Rose.png"};              // Array of texture paths
    private GamePanel gamePanel;
    private Thread gameLoop;
    private final int FPS_SET = 120;
    public static boolean run = true;

    public Game(int panelWidth, int panelHeight, Window window) {
        gamePanel = new GamePanel(panelWidth, panelHeight, window, this);
        InventoryPanel inventoryPanel = new InventoryPanel(panelWidth, panelHeight, gamePanel, gamePanel.dad);                          // Creates a new InventoryPanel object to pass into the main panel
        MainPanel mainPanel = new MainPanel(gamePanel, inventoryPanel);
        window.setElements(mainPanel);

        startGame();
        gamePanel.requestFocusInWindow();
        gamePanel.setIPanel(inventoryPanel);
    }

    public void startGame() {                                                                                                       // Method to start the Game Loop
        run = true;
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public static void stopGame() {
        run = false;
    }

    public static void playMusic(String path) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            System.gc();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Audio error has occured!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {                                                                                                                 // Game Loop

        double timePerFrame = 1000000000.0 / FPS_SET;
        long now;
        long previousTime = System.nanoTime();
        short fps = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaF = 0;
        Clip clip = null;
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("res/Audio/GameMusic.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Audio error has occured!");
            e.printStackTrace();
        }

        // While this loop runs, the game updates (game loop)
        while (run) {
            now = System.nanoTime();
            
            deltaF += (now - previousTime) / timePerFrame;
            previousTime = now;

            // Repaints every 120 frames
            if (deltaF >= 1) {
                // Moves the player based on vectors + colision logic
                if (!(gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY < 0 || gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY > Player.windowLimitY)) {
                    gamePanel.dad.LOCATION_Y += gamePanel.dad.VECTORY;
                }
                if (!(gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX < 0 || gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX > Player.windowLimitX)) {
                    gamePanel.dad.LOCATION_X += gamePanel.dad.VECTORX;
                }

                gamePanel.repaint();
                fps++;
                deltaF--;
            }

            // The FPS counter
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println(ANSI_GREEN + "FPS: " + fps + ANSI_RESET);
                if (Main.debug) {
                    System.out.println("LOC X: " + gamePanel.dad.LOCATION_X + " | LOC Y: " + gamePanel.dad.LOCATION_Y + " | SPEED: " + gamePanel.dad.VECTORY);
                }
                fps = 0;
                if (!clip.isRunning()) {
                    clip.setFramePosition(0);
                    clip.start();
                }
            }
        }
        clip.stop();
    }

    public static void saveGame(File saveFile) {;}

    public static void loadGame(File saveFile) {
        // ArrayList<Flower> flowers
        // byte[][] map
    }

    public static void wirteIntoMap(int i, int j, int value) {                                                    // Writes data into map at specified location
        map[i][j] = (byte) value;
    }

    public static void getMapData() {                                                                             // Retrieves all data from map and prints it into console
        for (int i=0; i<map.length; i++) {
            for (int j=0; j<map[0].length; j++) {
                System.out.print(" | " + map[i][j] + " | ");
            }
            System.out.println("");
        }
    }
}
