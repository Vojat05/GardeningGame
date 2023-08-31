package com.vojat.garden;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.vojat.Main;
import com.vojat.Data.JSONEditor;
import com.vojat.Enums.ErrorList;
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
    public static final String[] groundTextures = {"Grass1.png", "Grass2.png", "" , "House.png", "Well.png", "Fence.png"};                                                          // Texture array for outside
    public static final String[] houseTextures = {"Plank.png", "Grass1.png", "woodWall.png", "doormat.png", "bed.png", "wallpaint.png", "Wardrobe.png"};                            // Texture array for the inside of the house
    public static final String[][] flowerTypes = {{"tulip", "120000"}, {"rose", "155000"}, {"tentacle", "240000"}};                                                                 // {"flower type", "time for it to die in millis"}
    public static final int flowerChange = 5000;                                                                                                                                    // The time each flower has for being thirsty before they die
    public static final Random random = new Random();                                                                                                                               // A Random object to be used throughout the entire game
    public static boolean alert = false;                                                                                                                                            // Is some type of a alert up?
    public static String alertMessage = "";                                                                                                                                         // The latest alert message
    public static boolean warning = false;                                                                                                                                          // Is some type of a warning up?
    public static String warningMessage = "";                                                                                                                                       // The latest warning message
    public static byte errorTime = 0;                                                                                                                                               // Number of secodns for the latest error to be visible
    public static String errorMessage = "";                                                                                                                                         // The lastet error message
    public static ArrayList<Flower> flowers = new ArrayList<>();                                                                                                                    // ArrayList for all the flowers present in-game at a time
    public static char[][] map = new char[8][15];                                                                                                                                   // [Y][X] coords
    public static char[][] houseMap = new char[8][15];                                                                                                                              // [Y][X] coords
    public static ArrayList<Integer> invisibleWalls = new ArrayList<Integer>();                                                                                                     // ArrayList of map objects that are collidable
    public static ArrayList<Bird> birdList = new ArrayList<Bird>();                                                                                                                 // The list of birds currently in game for drawing
    public static Clip clip;                                                                                                                                                        // The clip for playing audio and sound effects
    public static boolean pause = false;                                                                                                                                            // Determines wheather the game should be paused or not
    public static byte save = -1;                                                                                                                                                   // The current game save number to be plugged into the respawn
    private final int FPS_SET = 120;                                                                                                                                                // Frame-Rate cap
    private static boolean run = true;                                                                                                                                              // Determines wheather the game-loop should still run
    private static ArrayList<Long> dieTimes = new ArrayList<Long>();                                                                                                                // ArrayList for flower die times used when pausing the game
    private GamePanel gamePanel;                                                                                                                                                    // The panel that shows the game window
    private Thread gameLoop;                                                                                                                                                        // The game loop itself


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
        
        map[0][1] = '3';      // Builds the house
        map[5][1] = '4';      // Builds the well

        // Building the fence around the garden
        for (int i=0; i<map[0].length; i++) {

            if (i >= 3) map[0][i] = '5';
            map[7][i] = '5';

        }
        
        // Fill with house spaces
        map[0][2] = '3';
        map[1][1] = '3';
        map[1][2] = '3';

        // Grass field & walls wisible from house
        for (int i=0; i<houseMap.length; i++) {
            
            for (int j=houseMap[0].length - 5; j<houseMap[0].length; j++) houseMap[i][j] = '1';
            houseMap[i][houseMap[0].length - 6] = '2';
            
        }

        // Fill the house map
        houseMap[7][4] = '3';
        houseMap[1][0] = '4';
        houseMap[2][9] = '5';
        houseMap[5][9] = '6';
        
        /*
         * --------------------------------------------------------------------------------
         * Filling up the arraylist with invisible walls
         * --------------------------------------------------------------------------------
         */

        for (int i=2; i<( houseMap.length > map.length ? houseMap.length : map.length ); i++) invisibleWalls.add(i);

        /*
         * --------------------------------------------------------------------------------
         * Setting up the main in-game panels and player spawn
         * --------------------------------------------------------------------------------
         */

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

    // Stops the game
    public static void killGame() {

        run = false;
        clip.stop();

    }

    // Pauses the game
    public static void pauseGame() {
        try {

            Thread.sleep(100);

        } catch (InterruptedException e) {

            e.printStackTrace();
            
        }

        pause = pause ? false : true;

        // Pauses the game music
        if (pause) clip.stop(); 
        else clip.start();

        // Saves the flower death times into the array list if paused and resets the die times when resumed
        if (pause) {

            for (Flower plant : flowers) dieTimes.add(plant.TIME_TO_DIE - System.currentTimeMillis());

        } else {

            for (int i=0; i<flowers.size(); i++) {

                flowers.get(i).TIME_TO_DIE = dieTimes.get(i) + System.currentTimeMillis();
                flowers.get(i).TIME_TO_DISSAPEAR = flowers.get(i).TIME_TO_DIE + 5000;

            }

            dieTimes.clear();

        }
    }

    // Writes data into map at specified location
    public static void wirteIntoMap(int i, int j, int value) {

        map[i][j] = (char) (48 + value);

    }

    // Retrieves all data from map and prints it into console if desired
    public static String getMapData(String type) {

        if (type.equals("print")) {

            for (int i=0; i<map.length; i++) {

                for (int j=0; j<map[0].length; j++) System.out.print(" | " + map[i][j] + " | ");
                System.out.println("");

            }

            return "";

        } else {

            String value = "";

            for (int i=0; i<map.length; i++) {

                for (int j=0; j<map[0].length; j++) value += map[i][j];
                value += "!";

            }

            return value;

        }
    }

    // Cleares a given 2D array
    public static void clearMap(char[][] map) {

        for (int i=0; i<map.length; i++) {

            for (int j=0; j<map[0].length; j++) {

                map[i][j] = '0';

            }
        }
    }

    // Allows an error to be displayed for a certian amount of time
    public static void error(String message, int duration) {

        if (message.length() < 22) {

            String helpString = "";

            for (int i=0; i<22 - message.length(); i += 2) {

                helpString += " ";

            }

            message = helpString + message;

        }

        errorMessage = message;
        errorTime = (byte) duration;
    }

    public void spawnBird() {

        System.out.println("Spawn Birb");
        birdList.add(new Bird(-1, gamePanel.dad.LOCATION_Y - 200 - random.nextInt(100)));

    }

    public static BufferedImage setTexture(String path) {

        try {

            return ImageIO.read(new FileInputStream(path));

        } catch (IOException ioe) {

            System.err.println(ErrorList.ERR_404.message);
            Game.error("Bird texture not found", 3);
            return null;

        }
    }

    public static void alertUpdate(String message, GamePanel gamePanel) {

        alertMessage = message;
        gamePanel.repaint();

    }

    public static void alert(String message, GamePanel gamePanel) {

        alert = true;
        alertMessage = message;
        gamePanel.repaint();

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
            error("Audio Error", 3);

        }
    }

    public static void stopMusic() {

        clip.stop();
        clip.flush();

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
            error("Audio Error", 3);

        }

        // While this loop runs, the game updates (game loop)
        while (run) {

            if (!pause) {

                now = System.nanoTime();
                deltaF += (now - previousTime) / timePerFrame;
                previousTime = now;

                // Repaints 120 times per second
                if (deltaF >= 1) {

                    /*
                     * --------------------------------------------------------------------------------
                     * Movement & colision logic
                     * --------------------------------------------------------------------------------
                     */

                    // Y coordinate colision logic
                    if (!(gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY < 0 || gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY > Player.windowLimitY || invisibleWalls.contains(intoMap(intoMapX(gamePanel.dad.LOCATION_X + 64), intoMapY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY), gamePanel.dad.level == 0 ? map : houseMap)))) gamePanel.dad.LOCATION_Y += gamePanel.dad.VECTORY;

                    // X coordinate colision logic
                    if (!(gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX < 0 || gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX > Player.windowLimitX || invisibleWalls.contains(intoMap(intoMapX(gamePanel.dad.LOCATION_X + 64 + gamePanel.dad.VECTORX), intoMapY(gamePanel.dad.LOCATION_Y + 80), gamePanel.dad.level == 0 ? map : houseMap)))) gamePanel.dad.LOCATION_X += gamePanel.dad.VECTORX;

                    // Bird flight logic
                    for (int i=0; i<birdList.size(); i++) {

                        // Bird flight
                        Bird bird = birdList.get(i);
                        bird.positionX += bird.vectorX;

                        // Bird flapping wings
                        if (intoMapX(bird.positionX) % 2 == 0) {

                            bird.texture = setTexture("res/Pics/Pigeon1.png");

                        } else {

                            bird.texture = setTexture("res/Pics/Pigeon2.png");

                        }

                        // Bird shit detection
                        if (bird.drawShit && intoMapY(bird.shitPositionY - 30) == intoMapY(gamePanel.dad.LOCATION_Y + 64) && intoMapX(bird.shitPositionX) == intoMapX(gamePanel.dad.LOCATION_X + 64)) {

                            System.out.println(ANSI_RED + "Shit hit" + ANSI_RESET);
                            gamePanel.dad.hurt(5);
                            bird.shitPositionY = Window.height;
                            continue;

                        }

                        // Bird shit movement and bird removal after out of map
                        if (bird.shitPositionY < bird.positionY + 500) bird.shitPositionY += Bird.shitSpeed;
                        if (bird.shitPositionY >= bird.positionY + 500) {

                            if (!bird.splat) bird.audio = true;
                            bird.drawShit = false;
                            bird.splat = true;

                        }
                        if (bird.positionX + 1000 < 0) birdList.remove(i);

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

                    if (Main.debug) System.out.println("LOC X: " + gamePanel.dad.LOCATION_X + " | LOC Y: " + gamePanel.dad.LOCATION_Y + " | SPEED: " + gamePanel.dad.VECTORY + "\nNo. Birds: " + birdList.size());
                    
                    // Checks if the player is on level 0 "outside"
                    if (gamePanel.dad.level == 0) gamePanel.changeGrass = true;

                    // Spawns the birb
                    if (gamePanel.dad.level == 0 && random.nextInt(100) == 0) spawnBird();

                    // Bird shitting logic
                    for (int i=0; i<birdList.size(); i++) {

                        Bird bird = birdList.get(i);
                        if (gamePanel.dad.level == 0 && intoMapX(bird.positionX) == intoMapX(gamePanel.dad.LOCATION_X + 64)) bird.shit();

                    }
                    
                    // Replays the in-game music if it had reached the end.
                    if (!clip.isRunning()) {

                        clip.setFramePosition(0);
                        clip.start();

                    }

                    // Resets the FPS counter each second
                    fps = 0;
                    if (errorTime != 0) errorTime--;
                    if (errorTime == 0) errorMessage = "";

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
    public static void saveGame(String saveFilePath, Player dad, byte saveNumber) throws FileNotFoundException {
        save = saveNumber;
        String map = "\"map\":\"" + getMapData("") + "\"";
        String value = "";

        // Foramts the flower information to be saved | {plant_number + plant_type : time_to_die | location X | location Y |}
        for (int i=0; i<flowers.size(); i++) {

            value += ",\"" + (flowers.get(i).PLANT_NUMBER + flowers.get(i).TYPE) + "\":\"" + ((flowers.get(i).TIME_TO_DIE - System.currentTimeMillis()) + "!" + flowers.get(i).LOCATION_X + "!" + flowers.get(i).LOCATION_Y) + "!\"";

        }

        JSONEditor jEditor = new JSONEditor(saveFilePath);
        jEditor.write(map + value);

        if (Main.debug) System.out.println("Game saved succesfully into \"" + saveFilePath + "\"");

        // Move player out of the bed
        dad.LOCATION_X = 80;
        dad.LOCATION_Y = 120;
    }

    // Loads the game progress from a given save
    public static void loadGame(String saveFilePath, byte saveNumber) throws FileNotFoundException {
        
        // Loads the map
        save = saveNumber;
        JSONEditor jEditor = new JSONEditor(saveFilePath);
        String[][] strMap = jEditor.read2DArr();
        String mapValues = strMap[0][1];
        String value = "";
        int num = 0;

        for (int i=0; i<mapValues.length(); i++) {

            if (mapValues.charAt(i) == '!' && i != mapValues.length()-1) {

                for (int j=0; j<value.length(); j++) {

                    wirteIntoMap(num, j, (int) value.charAt(j) - 48);      // [8][15] is the max size

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
                if (value.charAt(j) >= 48 && value.charAt(j) <= 57) plantNumber += value.charAt(j); 
                else plantType += value.charAt(j);

            }

            value = strMap[i][1];
            String data = "";
            byte symbols = 0;

            for (int j=0; j<value.length(); j++) {

                if (value.charAt(j) == '!') {

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

                } else data += value.charAt(j);
            }

            flowers.add(new Flower(
                Integer.parseInt(timeToDie) > flowerChange ? "res/Pics/" + plantType + ".png" : "res/Pics/" + plantType + "_thirsty.png", 
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

        return (int) positionX/128;

    }

    // Gets the theoretical Y location in the map
    public static int intoMapY(double positionY) {

        return (int) positionY/128;

    }

    // Gets the object in located in the map at the specific location
    public static int intoMap(int x, int y, char[][] map) {

        return (int) map[y][x] - 48;

    }
}
