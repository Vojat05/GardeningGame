package com.vojat.garden;

import java.awt.Color;
import java.awt.Font;
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
import javax.sound.sampled.FloatControl;
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
    public static final String[] groundTextures = {"Grass1.png", "Grass2.png", "" , "House.png", "Well.png", "Fence.png", "Tiles.png"};                                             // Texture array for outside
    public static final String[] houseTextures = {"Plank.png", "", "", "doormat.png", "bed.png", "Wardrobe.png", "table.png", "chair.png", "tv.png", "couch.png"};                  // Texture array for the inside of the house
    public static final String[][] flowerTypes = {{"tulip", "120000"}, {"rose", "155000"}, {"tentacle", "240000"}, {"Cactus", "400000"}};                                           // {"flower type", "time for it to die in millis"}
    public static final int flowerChange = 60000;                                                                                                                                   // The time each flower has for being thirsty before they die
    public static final Random random = new Random();                                                                                                                               // A Random object to be used throughout the entire game
    public static String stage = "Day";                                                                                                                                             // Current stage of the game ( Night / Day )
    public static String version = "";                                                                                                                                              // Current game version
    public static String tutorialStringPulledData = "";                                                                                                                             // Data to be displayed in the current tutorial screen box
    public static ArrayList<String> tutorialStrings = new ArrayList<String>();                                                                                                      // Every element of this arraylist is a single line to be printed to the output box
    public static boolean alert = false;                                                                                                                                            // Is some type of a alert up?
    public static boolean warning = false;                                                                                                                                          // Is some type of a warning up?
    public static byte errorTime = 0;                                                                                                                                               // Number of secodns for the latest error to be visible
    public static String alertMessage = "None";                                                                                                                                     // The latest alert message
    public static String warningMessage = "";                                                                                                                                       // The latest warning message
    public static String errorMessage = "";                                                                                                                                         // The lastet error message
    public static ArrayList<Flower> flowers = new ArrayList<>();                                                                                                                    // ArrayList for all the flowers present in-game at a time
    public static char[][] map = new char[8][15];                                                                                                                                   // [Y][X] coords
    public static char[][] houseMap = new char[8][15];                                                                                                                              // [Y][X] coords
    public static ArrayList<Character> invisibleWalls = new ArrayList<Character>();                                                                                                 // ArrayList of map objects that are collidable
    public static ArrayList<Bird> birdList = new ArrayList<Bird>();                                                                                                                 // The list of birds currently in game for drawing
    public static Clip clip;                                                                                                                                                        // The clip for playing audio and sound effects
    public static boolean pause = false;                                                                                                                                            // Determines wheather the game should be paused or not
    public static byte save = -1;                                                                                                                                                   // The current game save number to be plugged into the respawn
    public static boolean firstStart = true;                                                                                                                                        // Is this the first time the game instance is played? // Shows the help menu inside the house
    public static String langFileName;                                                                                                                                              // Name of the language file currently being used
    public static String texturePack;                                                                                                                                               // The texture pack file path
    public static Font font;                                                                                                                                                        // The custom font used in the game
    public static double dayNightTransitionSpeed = .0;                                                                                                                              // The value that is added to the night block
    public static float volumeTransitionSpeed = .0f;                                                                                                                                // The value that is added to the volume gain
    public static byte FPS_SET = 120;                                                                                                                                               // Frame-Rate cap
    private static boolean run = true;                                                                                                                                              // Determines wheather the game-loop should still run
    private static boolean isRaining = false;                                                                                                                                       // Is the current weather raining
    private static ArrayList<Long> dieTimes = new ArrayList<Long>();                                                                                                                // ArrayList for flower die times used when pausing the game
    private static int dayLasts = 120;                                                                                                                                              // How long does the day last in seconds
    private static int nightLasts = 60;                                                                                                                                             // How long does the night last in seconds
    private static float dayNumber = 0;                                                                                                                                             // How many days have past since the game started
    private static Clip rainClip;                                                                                                                                                   // The rain audio
    private GamePanel gamePanel;                                                                                                                                                    // The panel that shows the game window
    private Thread gameLoop;                                                                                                                                                        // The game loop itself
    private byte seconds = 0;                                                                                                                                                       // Seconds since the game started
    private float volumeGain = 0f;                                                                                                                                                  // The default music volume


    /*
     * --------------------------------------------------------------------------------
     * Setting up the game
     * --------------------------------------------------------------------------------
     */

    public Game(int panelWidth, int panelHeight, Window window) {

        /*
         * --------------------------------------------------------------------------------
         * Clear old data from previsou game instances
         * --------------------------------------------------------------------------------
         */

        invisibleWalls.clear();
        flowers.clear();
        clearMap(map);
        clearMap(houseMap);
        stage = "Day";
        isRaining = false;
        
        /*
         * --------------------------------------------------------------------------------
         * Building the main game objects (house, well, etc.)
         * --------------------------------------------------------------------------------
         */

        // Building the fence around the garden
        for (int i=0; i<map[0].length; i++) {

            if (i == 0) map[0][i] = '5';
            if (i >= 3) map[0][i] = '5';
            map[7][i] = '5';

        }
        
        // Fill the outside map data
        map[0][2] = '3'; // House block
        map[1][1] = '3'; // House block
        map[1][2] = '3'; // House block
        map[0][1] = '3'; // House block
        map[5][1] = '4'; // The well
        map[2][2] = '6'; // Tile

        // Fill the house map
        houseMap[5][3] = '2'; // Empty box to make up for the tabe size
        houseMap[6][5] = '3'; // Doormat
        houseMap[1][1] = '4'; // Bed
        houseMap[3][8] = '5'; // Wardrobe
        houseMap[5][2] = '6'; // Table
        houseMap[5][1] = '7'; // Chair Left
        houseMap[5][4] = '7'; // Chair Right
        houseMap[1][6] = '8'; // TV
        houseMap[2][7] = '9'; // Couch
        
        /*
         * --------------------------------------------------------------------------------
         * Filling up the arraylist with invisible walls
         * --------------------------------------------------------------------------------
         */

        for (int i=2; i<( houseTextures.length > groundTextures.length ? houseTextures.length : groundTextures.length ); i++) {
            
            if (i == 8 || i == 7 || i == 3) continue;
            invisibleWalls.add((char) (i + 48));
        
        }

        /*
         * --------------------------------------------------------------------------------
         * Setting up the tutorial text
         * --------------------------------------------------------------------------------
         */
        // Sets / Re-sets the tutorial box with every instance
        try {

            JSONEditor jsonEditor = new JSONEditor("../../res/Language/" + langFileName);
            tutorialStringPulledData = jsonEditor.readData("Tutorial-Data");

        } catch (IOException e) {

            e.printStackTrace();

        }

        firstStart = true;
        tutorialStrings.clear();

        // Get the number of lines for the tutorial box to draw them
        boolean sliced = false;
        for (int i=0; i<tutorialStringPulledData.length(); i++) {

            if (tutorialStringPulledData.charAt(i) == '\\' && tutorialStringPulledData.charAt(i + 1) == 'n' || i == tutorialStringPulledData.length()-1) {

                if (sliced) {

                    tutorialStrings.add(tutorialStringPulledData.substring(1, i));

                } else {

                    tutorialStrings.add(tutorialStringPulledData.substring(0, i));
                    sliced = true;

                }

                tutorialStringPulledData = tutorialStringPulledData.substring(i+1, tutorialStringPulledData.length());
                i = 0;

            }
        }

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
        gamePanel.dad.LOCATION_X = 208;
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

        rainClip = playSound("../../res/" + texturePack + "/Audio/Rain.wav");
        rainClip.stop();
        rainClip.setFramePosition(0);

    }

    // Stops the game
    public static void killGame() {

        run = false;
        clip.stop();
        rainClip.stop();

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
        if (pause) {

            stopClip(clip);
            stopClip(rainClip);

        } else {

            clip.start();
            if (isRaining()) rainClip.start();

        }

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

    // Gets the is raining value
    public static boolean isRaining() {

        return isRaining;

    }

    // Gets how long the day should last in seconds
    public static int dayLasts() {

        return dayLasts;

    }

    // Sets how long the day should last in seconds
    public static int setDayLasts(int value) {

        return dayLasts = value;

    }

    // Gets how long the night should last in seconds
    public static int nightLasts() {

        return nightLasts;

    }

    // Sets how long the night should last in seconds
    public static int setNightLasts(int value) {

        return nightLasts = value;

    }

    // Gets the number of days passed since the game started
    public static int getDay() {

        return (int) dayNumber;

    }

    // Adds a number to the number of days passed the game started
    public static int addDays(int value) {

        return (int) (dayNumber += value);

    }

    // Sets the number of days passed since the game started to a select value
    public static int setDays(int value) {

        return (int) (dayNumber = value);

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
            Game.error("Texture not found", 3);
            return null;

        }
    }

    public static void alertUpdate(String message, GamePanel gamePanel) {

        alertMessage = message;
        if (gamePanel.hasFocus()) gamePanel.repaint();

    }

    public static void alert(String message, GamePanel gamePanel) {

        alert = true;
        alertMessage = message;
        if (gamePanel.hasFocus()) gamePanel.repaint();

    }

    /*
     * --------------------------------------------------------------------------------
     * Methods for controlling the game audio
     * --------------------------------------------------------------------------------
     */

    // Plays the wav file at a given path
    public static Clip playSound(String path) {
        try {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            Clip sound = AudioSystem.getClip();
            sound.open(audioStream);
            sound.setFramePosition(0);
            sound.start();
            System.gc();
            return sound;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            System.err.println("Audio error has occured!");
            e.printStackTrace();
            error("Audio Error", 3);
            return null;

        }
    }

    public static void stopClip(Clip clip) {

        clip.stop();

    }

    // Sets the volume of a given clip between 0 ( mute ) and 1 ( original volume )
    public static void setClipVolume(Clip clip, float volume) {

        if (volume < -80.0f || volume > 6.0f) {

            error("Audio volume error", 3);
            return;

        }

        FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        fc.setValue(volume);

    }

    /*
     * --------------------------------------------------------------------------------
     * Game Logic || Game tick Logic
     * --------------------------------------------------------------------------------
     */

    private void gameTick() {

        /*
         * --------------------------------------------------------------------------------
         * Bird flight logic
         * --------------------------------------------------------------------------------
         */

        for (int i=0; i<birdList.size(); i++) {

            // Bird flight
            Bird bird = birdList.get(i);
            bird.positionX += bird.vectorX;

            // Bird flapping wings
            if (intoMapX(bird.positionX) % 2 == 0) {

                bird.texture = setTexture("../../res/" + texturePack + "/Pics/Pigeon1.png");

            } else {

                bird.texture = setTexture("../../res/" + texturePack + "/Pics/Pigeon2.png");

            }

            // Bird shit detection
            if (bird.drawShit && intoMapY(bird.shitPositionY - 30) == intoMapY(gamePanel.dad.LOCATION_Y + 64) && intoMapX(bird.shitPositionX) == intoMapX(gamePanel.dad.LOCATION_X + 64)) {

                if (gamePanel.dad.HP == 0) gamePanel.dad.currentTexture = setTexture("../../res/" + texturePack + "/Pics/Player/GraveShit.png");
                if (gamePanel.dad.HP != 0) gamePanel.dad.hurt(5);

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

        /*
         * --------------------------------------------------------------------------------
         * Enter / Exit house logic // Position listener
         * --------------------------------------------------------------------------------
         */

        // Enter house logic
        if (gamePanel.dad.level == 0 && intoMapX(gamePanel.dad.LOCATION_X + 64) == 2 && intoMapY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY) == 1) {

            playSound("../../res/" + texturePack + "/Audio/DoorInteract.wav");
            gamePanel.dad.level = 1;
            gamePanel.dad.LOCATION_X = 638;
            gamePanel.dad.LOCATION_Y = 810;
            invisibleWalls.remove(invisibleWalls.indexOf('3'));
            invisibleWalls.add('6');

        }

        // Exit house logic
        if (gamePanel.dad.level == 1 && intoMapX(gamePanel.dad.LOCATION_X + 64) == 5 && intoMapY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY) == 7) {

            playSound("../../res/" + texturePack +  "/Audio/DoorInteract.wav");
            gamePanel.dad.level = 0;
            gamePanel.dad.LOCATION_X = 240;
            gamePanel.dad.LOCATION_Y = 200;
            invisibleWalls.add('3');
            invisibleWalls.remove(invisibleWalls.indexOf('6'));

        }

        /*
         * --------------------------------------------------------------------------------
         * Check if the player is located on the tiles and increases his movement speed
         * --------------------------------------------------------------------------------
         */

        if (gamePanel.dad.VECTORX > 0) gamePanel.dad.VECTORX = gamePanel.dad.speed;
        else if (gamePanel.dad.VECTORX < 0) gamePanel.dad.VECTORX = -gamePanel.dad.speed;

        if (gamePanel.dad.VECTORY > 0) gamePanel.dad.VECTORY = gamePanel.dad.speed;
        else if (gamePanel.dad.VECTORY < 0) gamePanel.dad.VECTORY = -gamePanel.dad.speed;
        
        if (map[intoMapY(gamePanel.dad.LOCATION_Y + 100)][intoMapX(gamePanel.dad.LOCATION_X + 64)] == '6' && gamePanel.dad.level == 0) gamePanel.dad.speed = 1.5;
        else gamePanel.dad.speed = 1;

        /*
         * --------------------------------------------------------------------------------
         * Day -> Night fade in / out
         * --------------------------------------------------------------------------------
         */

        // Day -> Night fade in
        if (stage.equals("Night") && gamePanel.easeDayNight < 248) {

            gamePanel.easeDayNight += dayNightTransitionSpeed;
            if (volumeGain > -15.0f) {

                volumeGain -= volumeTransitionSpeed;
                setClipVolume(clip, volumeGain);

            }
        }

        // Night -> Day fade out
        if (stage.equals("Day") && gamePanel.easeDayNight > 0) {

            gamePanel.easeDayNight -= dayNightTransitionSpeed;
            if (volumeGain < 0.0f) {

                volumeGain += volumeTransitionSpeed;
                setClipVolume(clip, volumeGain);

            }
        }

        // Move the rain img
        if (stage.equals("Night") && isRaining()) {

            gamePanel.rainPositionY -= .5f;
            if ((int) gamePanel.rainPositionY <= 0) gamePanel.rainPositionY = 432f;

        }
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
        short animationTick = 100;

        // The in-game audio player
        try {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("../../res/" + texturePack + "/Audio/GameMusic.wav"));
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
            
            if (pause) {
                
                now = System.nanoTime();
                deltaF += (now - previousTime) * Math.pow(timePerFrame, -1);
                previousTime = now;
                
                if (deltaF >= 1) deltaF--;
                if (gamePanel.hasFocus() && gamePanel.saveMenuOpen) gamePanel.repaint();
                continue;
                
            }

            now = System.nanoTime();
            deltaF += (now - previousTime) * Math.pow(timePerFrame, -1);
            previousTime = now;

            // Repaints 120 times per second
            if (deltaF >= 1) {

                /*
                 * --------------------------------------------------------------------------------
                 * Player Movement & colision logic
                 * --------------------------------------------------------------------------------
                 */
                        
                // Y coordinate colision logic
                if (!(gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY < 0 || gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY > Player.windowLimitY || invisibleWalls.contains((char) (intoMap(intoMapX(gamePanel.dad.LOCATION_X + 64), intoMapY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY), gamePanel.dad.level == 0 ? map : houseMap) + 48)))) gamePanel.dad.LOCATION_Y += gamePanel.dad.VECTORY;
                        
                // X coordinate colision logic
                if (!(gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX < 0 || gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX > Player.windowLimitX || invisibleWalls.contains((char) (intoMap(intoMapX(gamePanel.dad.LOCATION_X + 64 + gamePanel.dad.VECTORX), intoMapY(gamePanel.dad.LOCATION_Y + 80), gamePanel.dad.level == 0 ? map : houseMap) + 48)))) gamePanel.dad.LOCATION_X += gamePanel.dad.VECTORX;
    
                gameTick();

                if (gamePanel.hasFocus()) gamePanel.repaint();
                fps++;
                deltaF--;
            }

            // Happens animationTick times per second ( default 100 / 10 )
            if (System.currentTimeMillis() - lastCheck >= animationTick) {
                animationTick += 100;
            }

            // The FPS counter. This occures ever second
            if (System.currentTimeMillis() - lastCheck >= 1000) {

                lastCheck = System.currentTimeMillis();
                System.out.println(ANSI_GREEN + "FPS: " + fps + ANSI_RESET + "\nSecs < " + seconds + " >");

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
                if (!clip.isRunning() && gamePanel.dad.HP != 0) {

                    clip.setFramePosition(0);
                    clip.start();

                }

                /*
                 * --------------------------------------------------------------------------------
                 * Flower life-ending logic
                 * --------------------------------------------------------------------------------
                 */

                for (int j=0; j<flowers.size(); j++) {
                
                    Flower plant = flowers.get(j);
                
                    if (plant.STATUS.equals("Alive") && plant.TIME_TO_DIE - System.currentTimeMillis() <= flowerChange) {
                    
                        plant.STATUS = "Thirsty";
                        plant.CURRENT_TEXTURE = plant.setTexture(plant.THIRSTY_TEXTURE);
                        continue;
                    
                    } else if (plant.STATUS.equals("Thirsty") && plant.TIME_TO_DIE <= System.currentTimeMillis()) {
                    
                        plant.STATUS = "Dead";
                        plant.CURRENT_TEXTURE = plant.setTexture(plant.DEAD_TEXTURE);
                        playSound("../../res/" + texturePack + "/Audio/MagicSound.wav");
                        continue;
                    
                    } else if (plant.STATUS.equals("Dead") && plant.TIME_TO_DISSAPEAR <= System.currentTimeMillis()) {
                    
                        flowers.remove(plant);
                        map[plant.LOCATION_Y][plant.LOCATION_X] = '0';
                        continue;
                    
                    }

                    if (isRaining() && !plant.STATUS.equals("Dead")) plant.resetTime();
                }

                // Resets the FPS counter each second
                fps = 0;
                seconds++;
                animationTick = 100;
                if (errorTime != 0) errorTime--;
                if (errorTime == 0) errorMessage = "";

                /*
                 * --------------------------------------------------------------------------------
                 * Stamina depleeting logic
                 * --------------------------------------------------------------------------------
                 */

                if (gamePanel.dad.VECTORX != 0 || gamePanel.dad.VECTORY != 0) gamePanel.dad.tire(5);
                else gamePanel.dad.tire(-10);

                /*
                 * --------------------------------------------------------------------------------
                 * 0 Stamina penalty
                 * --------------------------------------------------------------------------------
                 */
                if (gamePanel.dad.stamina == 0) {

                    gamePanel.dad.setMove(false);
                    gamePanel.dad.outOfStamina = true;
                    error("Out of stamina", 3);

                } else if (!gamePanel.dad.canMove() && gamePanel.dad.stamina == 100 && !gamePanel.dad.isSitting) {

                    gamePanel.dad.setMove(true);
                    gamePanel.dad.outOfStamina = false;
                    
                }

                if (gamePanel.dad.outOfStamina) gamePanel.inventoryPanel.SColor = gamePanel.inventoryPanel.SColor == Color.RED ? new Color(0xfadc05) : Color.RED;

                /*
                 * --------------------------------------------------------------------------------
                 * Day -> Night cycle
                 * --------------------------------------------------------------------------------
                 */

                // The Day / Night change
                if (seconds >= (stage.equals("Day") ? dayLasts() : nightLasts())) {

                    stage = stage.equals("Day") ? "Night" : "Day";
                    seconds = 0;
                    dayNumber += 0.5;
                    System.out.println("Stage change: " + stage + "\nDay-Lasts: " + dayLasts() + " | Night-Lasts: " + nightLasts() + " | Day: " + getDay());

                }

                if (isRaining() || stage.equals("Night") && !rainClip.isRunning() && (int) gamePanel.easeDayNight == 248) {

                    if (!isRaining()) isRaining = true;

                    rainClip.setFramePosition(0);
                    rainClip.start();

                } else if (!isRaining()) {

                    rainClip.setFramePosition(0);
                    rainClip.stop();

                }

                if (isRaining() && stage.equals("Day")) isRaining = false;

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
        dad.LOCATION_X = 208;
        dad.LOCATION_Y = 120;
    }

    // Loads the game progress from a given save
    public static void loadGame(String saveFilePath, byte saveNumber) throws FileNotFoundException {
        
        // Loads the map
        firstStart = false;
        save = saveNumber;
        JSONEditor jEditor = new JSONEditor(saveFilePath);
        String[][] strMap = jEditor.read2DArr();
        String mapValues = strMap[0][1];
        String value = "";

        for (int i=0, num = 0; i<mapValues.length(); i++) {

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

            for (int j=0, symbols = 0; j<value.length(); j++) {

                if (value.charAt(j) != '!') {

                    data += value.charAt(j);
                    continue;

                }

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

                    default:
                        break;

                }

                data = "";

            }

            flowers.add(new Flower(
                plantType, 
                Integer.parseInt(posX), 
                Integer.parseInt(posY), 
                Integer.parseInt(timeToDie) > 0 ? "Alive" : "Dead", 
                Integer.parseInt(plantNumber), 
                Integer.parseInt(timeToDie))
            );

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

        return (int) (positionX * .0078125);

    }

    // Gets the theoretical Y location in the map
    public static int intoMapY(double positionY) {

        return (int) (positionY * .0078125);

    }

    // Gets the object in located in the map at the specific location
    public static int intoMap(int x, int y, char[][] map) {

        return (int) map[y][x] - 48;

    }
}
