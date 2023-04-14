package com.vojat.garden;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.vojat.Main;
import com.vojat.Data.JSONEditor;
import com.vojat.menu.Window;

public class Game implements Runnable {
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    public static ArrayList<Flower> flowers = new ArrayList<>();
    public static byte[][] map = new byte[8][15];      // [Y][X] coords  -> Now it's a total of 120 spots to place a flower
    public static String[] textures = {"res/Pics/WaterDrop9.png", "res/Pics/tulip.png", "res/Pics/rose.png"};     // Array of texture paths
    public static String[] groundTextures = {"res/Pics/Grass1.png", "res/Pics/Grass2.png", "" , "res/Pics/Well.png"};     // Array of texture paths for the ground animation. The "" on position 2 is because number 2 in map is reserved for flowers
    private GamePanel gamePanel;
    private Thread gameLoop;
    private final int FPS_SET = 120;
    public static boolean run = true;

    public Game(int panelWidth, int panelHeight, Window window) {
        flowers.clear();
        // Cleares the map
        for (int i=0; i<map.length; i++) {
            for (int j=0; j<map[0].length; j++) {
                map[i][j] = 0;
            }
        }
        map[5][1] = 3;      // Bulds the well


        gamePanel = new GamePanel(panelWidth, panelHeight, window);
        InventoryPanel inventoryPanel = new InventoryPanel(panelWidth, panelHeight, gamePanel, gamePanel.dad);      // Creates a new InventoryPanel object to pass into the main panel
        MainPanel mainPanel = new MainPanel(gamePanel, inventoryPanel);
        window.setElements(mainPanel);

        startGame();
        gamePanel.requestFocusInWindow();
        gamePanel.setIPanel(inventoryPanel);
    }

    // Method to start the Game Loop
    public void startGame() {
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

    // Game Loop
    @Override
    public void run() {

        double timePerFrame = 1_000_000_000.0 / FPS_SET;
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
            previousTime = now;     // Updates previous time after the calculation

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
                gamePanel.changeGrass = true; //    Allows the game to change grass textures
                if (!clip.isRunning()) {
                    clip.setFramePosition(0);
                    clip.start();
                }
            }
        }
        clip.stop();
    }

    // Saves the game progress into a seperate JSON file
    public static void saveGame(String saveFilePath) throws FileNotFoundException {
        String map = "\"map\":\"" + getMapData("") + "\"";
        String value = "";

        for (int i=0; i<flowers.size(); i++) {
            value += ",\"" + (flowers.get(i).PLANT_NUMBER + flowers.get(i).TYPE) + "\":\"" + ((flowers.get(i).TIME_TO_DIE - System.currentTimeMillis()) + "|" + flowers.get(i).LOCATION_X + "|" + flowers.get(i).LOCATION_Y) + "|\"";
        }

        JSONEditor jEditor = new JSONEditor(saveFilePath);
        jEditor.write(map + value);
    }

    public static void loadGame(String saveFilePath) throws FileNotFoundException {
        
        // Loads the map
        JSONEditor jEditor = new JSONEditor(saveFilePath);
        String[][] strMap = jEditor.read2DArr();
        String mapValues = strMap[0][1];
        String value = "";
        int num = 0;
        for (int i=0; i<mapValues.length(); i++) {
            if (mapValues.charAt(i) == '|' && i != mapValues.length()-1) {
                for (int j=0; j<value.length(); j++) {
                    wirteIntoMap(num, j, Character.getNumericValue(value.charAt(j)));      // [8][15]
                }
                value = "";
                num++;
            } else {
                value += mapValues.charAt(i);
            }
        }

        // Loads the flowers
        for (int i=1; i<strMap.length; i++) {
            String plantNumber = "";
            String plantType = "";
            String timeToDie = "";
            String posX = "";
            String posY = "";
            value = strMap[i][0];
            for (int j=0; j<value.length(); j++) {
                // 48 - 57 is the char range for integers 0 - 9
                if (value.charAt(j) >= 48 && value.charAt(j) <= 57) {
                    plantNumber += value.charAt(j);
                } else {
                    plantType += value.charAt(j);
                }
            }
            value = strMap[i][1];
            String data = "";
            byte symbols = 0;
            for (int j=0; j<value.length(); j++) {
                if (value.charAt(j) == '|') {
                    symbols++;
                    switch (symbols) {
                        case 1:
                            timeToDie = data;
                            break;

                        case 2:
                            posX = data;
                            break;

                        case 3:
                            posY = data;
                            break;
                    }
                    data = "";
                } else {
                    data += value.charAt(j);
                }
            }
            flowers.add(new Flower(Integer.parseInt(timeToDie) > 5000 ? ("res/Pics/" + plantType + ".png") : "res/Pics/Land.png", 
                plantType, 
                Integer.parseInt(posX), 
                Integer.parseInt(posY), 
                Integer.parseInt(timeToDie) > 0 ? "Alive" : "Dead", 
                Integer.parseInt(plantNumber), 
                Integer.parseInt(timeToDie)));
        }
        System.gc();
    }

    // Writes data into map at specified location
    public static void wirteIntoMap(int i, int j, int value) {
        map[i][j] = (byte) value;
    }

    // Retrieves all data from map and prints it into console
    public static String getMapData(String type) {
        if (type.equals("print")) {
            for (int i=0; i<map.length; i++) {
                for (int j=0; j<map[0].length; j++) {
                    System.out.print(" | " + map[i][j] + " | ");
                }
                System.out.println("");
            }
            return "";
        } else {
            String value = "";
            for (int i=0; i<map.length; i++) {
                for (int j=0; j<map[0].length; j++) {
                    value += map[i][j];
                }
                value += "|";
            }
            return value;
        }
    }
}
