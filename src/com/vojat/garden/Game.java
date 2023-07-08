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

    /*
     * --------------------------------------------------------------------------------
     * Here are all the important game variables
     * --------------------------------------------------------------------------------
     */

    public static final String ANSI_GREEN = "\u001B[32m";                                                                                                                           // Set the console text color to green
    public static final String ANSI_RED = "\u001B[31m";                                                                                                                             // Set the console text color to red
    public static final String ANSI_RESET = "\u001B[0m";                                                                                                                            // Reset the console text color
    public static ArrayList<Flower> flowers = new ArrayList<>();                                                                                                                    // ArrayList for all the flowers present in-game at a time
    public static byte[][] map = new byte[8][15];                                                                                                                                   // [Y][X] coords
    public static byte[][] houseMap = new byte[8][15];                                                                                                                              // [Y][X] cords
    public static final String[] groundTextures = {"res/Pics/Grass1.png", "res/Pics/Grass2.png", "" , "res/Pics/House.png", "res/Pics/Well.png", "res/Pics/Fence.png"};             // Texture array for outside
    public static final String[] houseTextures = {"res/Pics/Plank.png", "res/Pics/Grass1.png", "res/Pics/woodWall.png", "res/Pics/doormat.png", "res/Pics/bed.png"};                // Texture array for the inside of the house
    public static final String[][] flowerTypes = {{"tulip", "20000"}, {"rose", "25000"}};                                                                                           // {"flower type", "time for it to die in millis"}
    public static ArrayList<Integer> invisibleWalls = new ArrayList<Integer>();                                                                                                     // ArrayList of map objects that are collidable
    public static Clip clip;                                                                                                                                                        // The clip for playing audio and sound effects
    public final static int flowerChange = 5000;                                                                                                                                    // The time each flower has for being thirsty before they die
    public static boolean pause = false;                                                                                                                                            // Determines wheather the game should be paused or not
    private GamePanel gamePanel;                                                                                                                                                    // The panel that shows the game window
    private Thread gameLoop;                                                                                                                                                        // The game loop itself
    private final int FPS_SET = 120;                                                                                                                                                // Frame-Rate cap
    private static boolean run = true;                                                                                                                                              // Determines wheather the game-loop should still run
    private static ArrayList<Long> dieTimes = new ArrayList<Long>();                                                                                                                // ArrayList for flower die times used when pausing the game


    /*
     * --------------------------------------------------------------------------------
     * Setting up the game
     * --------------------------------------------------------------------------------
     */

    public Game(int panelWidth, int panelHeight, Window window) {

        // Cleares the maps and flowers
        flowers.clear();
        clearMap(map);
        clearMap(houseMap);
        
        /*
        * --------------------------------------------------------------------------------
        * Building the main game objects (house, well, etc.)
        * --------------------------------------------------------------------------------
        */
        
        map[0][1] = 3;      // Builds the house
        map[5][1] = 4;      // Builds the well

        // Building the fence around the garden
        for (int i=3; i<map[0].length; i++) {
            map[0][i] = 5;
        }

        for (int i=0; i<map[0].length; i++) {
            map[7][i] = 5;
        }
        
        // Fill with house spaces
        map[0][2] = 3;
        map[1][1] = 3;
        map[1][2] = 3;

        // Fill the house map
        houseMap[7][4] = 3;
        houseMap[1][0] = 4;

        // Grass field wisible from house
        for (int i=0; i<houseMap.length; i++) {
            for (int j=houseMap[0].length-5; j<houseMap[0].length; j++) {
                houseMap[i][j] = 1;
            }
            houseMap[i][houseMap[0].length-6] = 2;
        }

        /*
         * --------------------------------------------------------------------------------
         * Filling up the arraylist with invisible walls
         * --------------------------------------------------------------------------------
         */

        // Fill the invisible walls arraylist
        for (int i=2; i<6; i++) invisibleWalls.add(i);

        /*
         * --------------------------------------------------------------------------------
         * Setting up the main in-game panels
         * --------------------------------------------------------------------------------
         */

        // Sets up all the panels
        gamePanel = new GamePanel(panelWidth, panelHeight, window);
        InventoryPanel inventoryPanel = new InventoryPanel(panelWidth, panelHeight, gamePanel, gamePanel.dad);
        MainPanel mainPanel = new MainPanel(gamePanel, inventoryPanel);
        window.setElements(mainPanel);

        // Starts the game and requests focus
        startGame();
        gamePanel.requestFocusInWindow();
        gamePanel.setIPanel(inventoryPanel);

        // Set the player starting position
        gamePanel.dad.level = 1;
        gamePanel.dad.LOCATION_X = 80;
        gamePanel.dad.LOCATION_Y = 120;
    }

    /*
     * --------------------------------------------------------------------------------
     * Methods for controlling the Game
     * --------------------------------------------------------------------------------
     */

    // Method to start the Game Loop
    private void startGame() {
        run = true;
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    // Kills the game
    public static void killGame() {
        run = false;
    }

    // Pauses the game
    public static void pauseGame() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pause = pause ? false : true;

        if (pause) {
            clip.stop();
        } else {
            clip.start();
        }

        // Saves the flower death times into the array list if paused and resets the die times when resumed
        if (pause) {
            for (Flower plant : flowers) {
                dieTimes.add(plant.TIME_TO_DIE - System.currentTimeMillis());
            }
        } else {
            for (int i=0; i<flowers.size(); i++) {
                flowers.get(i).TIME_TO_DIE = dieTimes.get(i) + System.currentTimeMillis();
                flowers.get(i).TIME_TO_DISSAPEAR = dieTimes.get(i) + System.currentTimeMillis() + 5000;
                dieTimes.set(i, null);
            }
        }
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

    // Cleares a given 2D array
    public static void clearMap(byte[][] map) {
        for (int i=0; i<map.length; i++) {
            for (int j=0; j<map[0].length; j++) {
                map[i][j] = 0;
            }
        }
    }

    /*
     * --------------------------------------------------------------------------------
     * Methods for controlling the game audio
     * --------------------------------------------------------------------------------
     */

    // Plays the wav file at a given path
    public static void playSound(String path) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            Clip sound = AudioSystem.getClip();
            sound.open(audioStream);
            sound.start();
            System.gc();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Audio error has occured!");
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        clip.stop();
    }

    /*
     * --------------------------------------------------------------------------------
     * The game loop is written here
     * --------------------------------------------------------------------------------
     */

    @Override
    public void run() {

        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        long now;
        long previousTime = System.nanoTime();
        short fps = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaF = 0;

        // The in-game audio player
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("res/Audio/GameMusic.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Audio error has occured!");
        }

        // While this loop runs, the game updates (game loop)
        while (run) {
            if (!pause) {
                now = System.nanoTime();
            
                deltaF += (now - previousTime) / timePerFrame;
                previousTime = now;     // Updates previous time after the calculation

                // Repaints 120 times per second
                if (deltaF >= 1) {

                    // Movement + colision logic
                    if (!(gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY < 0 || gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY > Player.windowLimitY || invisibleWalls.contains(intoMap(intoMapY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY), intoMapX(gamePanel.dad.LOCATION_X + 64), gamePanel.dad.level == 0 ? map : houseMap)))) {
                        gamePanel.dad.LOCATION_Y += gamePanel.dad.VECTORY;
                    }
                    if (!(gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX < 0 || gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX > Player.windowLimitX || invisibleWalls.contains(intoMap(intoMapY(gamePanel.dad.LOCATION_Y + 80), intoMapX(gamePanel.dad.LOCATION_X + 64 + gamePanel.dad.VECTORX), gamePanel.dad.level == 0 ? map : houseMap)))) {
                        gamePanel.dad.LOCATION_X += gamePanel.dad.VECTORX;
                    }

                    // Enter house logic
                    if (gamePanel.dad.level == 0 && intoMapX(gamePanel.dad.LOCATION_X + 64) == 2 && intoMapY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY) == 1) {
                        playSound("res/Audio/DoorInteract.wav");
                        gamePanel.dad.level = 1;
                        gamePanel.dad.LOCATION_X = 510;
                        gamePanel.dad.LOCATION_Y = 810;
                    }

                    // Exit house logic
                    if (gamePanel.dad.level == 1 && intoMapX(gamePanel.dad.LOCATION_X + 64) == 4 && intoMapY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY) == 7) {
                        playSound("res/Audio/DoorInteract.wav");
                        gamePanel.dad.level = 0;
                        gamePanel.dad.LOCATION_X = 240;
                        gamePanel.dad.LOCATION_Y = 200;
                    }

                    gamePanel.repaint();
                    fps++;
                    deltaF--;
                }

                // The FPS counter. This occures ever second
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.println(ANSI_GREEN + "FPS: " + fps + ANSI_RESET);
                    if (Main.debug) {
                        System.out.println("LOC X: " + gamePanel.dad.LOCATION_X + " | LOC Y: " + gamePanel.dad.LOCATION_Y + " | SPEED: " + gamePanel.dad.VECTORY);
                    }
                    
                    // Checks if the player is on level 0 "outside"
                    if (gamePanel.dad.level == 0) {
                        gamePanel.changeGrass = true;
                    }
                    
                    // Replays the in-game music if it had reached the end.
                    if (!clip.isRunning()) {
                        clip.setFramePosition(0);
                        clip.start();
                    }

                    // Resets the FPS counter
                    fps = 0;
                }
            }
        }
        clip.stop();
    }

    /*
     * -------------------------------------------------------------------------
     * Save and load methods
     * -------------------------------------------------------------------------
     */

    // Saves the game progress into a seperate JSON file
    public static void saveGame(String saveFilePath, Player dad) throws FileNotFoundException {
        String map = "\"map\":\"" + getMapData("") + "\"";
        String value = "";

        for (int i=0; i<flowers.size(); i++) {
            value += ",\"" + (flowers.get(i).PLANT_NUMBER + flowers.get(i).TYPE) + "\":\"" + ((flowers.get(i).TIME_TO_DIE - System.currentTimeMillis()) + "|" + flowers.get(i).LOCATION_X + "|" + flowers.get(i).LOCATION_Y) + "|\"";
        }

        JSONEditor jEditor = new JSONEditor(saveFilePath);
        jEditor.write(map + value);

        if (Main.debug) System.out.println("Game saved succesfully into \"" + saveFilePath + "\"");

        // Move player out of the bed
        dad.LOCATION_X = 80;
        dad.LOCATION_Y = 120;
    }

    // Loads the game progress from a given save
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
                    wirteIntoMap(num, j, Character.getNumericValue(value.charAt(j)));      // [8][15] is the max size
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
            flowers.add(new Flower(
                Integer.parseInt(timeToDie) > flowerChange ? "res/Pics/" + plantType + ".png" : "res/Pics/Land.png", 
                plantType, 
                Integer.parseInt(posX), 
                Integer.parseInt(posY), 
                Integer.parseInt(timeToDie) > 0 ? "Alive" : "Dead", 
                Integer.parseInt(plantNumber), 
                Integer.parseInt(timeToDie)));
        }
        System.gc();
    }

    /*
     * --------------------------------------------------------------------------------
     * Methods for translating coordinates into the game map
     * --------------------------------------------------------------------------------
     */

    // Gets the theoretical X location in the map
    public static int intoMapX(double positionX) {
        if (positionX <= 128) {
            return 0;
        } else if (positionX <= 128*2) {
            return 1;
        } else if (positionX <= 128*3) {
            return 2;
        } else if (positionX <= 128*4) {
            return 3;
        } else if (positionX <= 128*5) {
            return 4;
        } else if (positionX <= 128*6) {
            return 5;
        } else if (positionX <= 128*7) {
            return 6;
        } else if (positionX <= 128*8) {
            return 7;
        } else if (positionX <= 128*9) {
            return 8;
        } else if (positionX <= 128*10) {
            return 9;
        } else if (positionX <= 128*11) {
            return 10;
        } else if (positionX <= 128*12) {
            return 11;
        } else if (positionX <= 128*13) {
            return 12;
        } else if (positionX <= 128*14) {
            return 13;
        } else return 14;
    }

    // Gets the theoretical Y location in the map
    public static int intoMapY(double positionY) {
        if (positionY <= 128) {
            return 0;
        } else if (positionY <= 128*2) {
            return 1;
        } else if (positionY <= 128*3) {
            return 2;
        } else if (positionY <= 128*4) {
            return 3;
        } else if (positionY <= 128*5) {
            return 4;
        } else if (positionY <= 128*6) {
            return 5;
        } else if (positionY <= 128*7) {
            return 6;
        } else return 7;
    }

    // Gets the object in located in the map at the specific location
    public static int intoMap(int y, int x, byte[][] map) {
        return map[y][x];
    }
}
