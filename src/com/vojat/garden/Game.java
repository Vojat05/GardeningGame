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
import com.vojat.Data.Map;
import com.vojat.Data.TutorialBox;
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
    public static final String[] groundTextures = {"Grass1.png", "Grass2.png", "" , "House.png", "Well.png", "Fence.png", "Tiles.png", "FencePole.png"};                            // Texture array for outside
    public static final String[] houseTextures = {"Plank.png", "", "", "doormat.png", "bed.png", "Wardrobe.png", "table.png", "chair.png", "tv.png", "couch.png"};                  // Texture array for the inside of the house
    public static final String[][] flowerTypes = {{"tulip", "120000"}, {"rose", "155000"}, {"tentacle", "240000"}, {"Cactus", "400000"}};                                           // {"flower type", "time for it to die in millis"}
    public static final int flowerChange = 60000;                                                                                                                                   // The time each flower has for being thirsty before they die
    public static final Random random = new Random();                                                                                                                               // A Random object to be used throughout the entire game
    public static Map map = new Map(new char[8][15]);                                                                                                                               // [Y][X] coords
    public static Map houseMap = new Map(new char[8][15]);                                                                                                                          // [Y][X] coords
    public static String stage = "Day";                                                                                                                                             // Current stage of the game ( Night / Day )
    public static String version = "";                                                                                                                                              // Current game version
    public static TutorialBox tutorial = new TutorialBox(0, 0, false);                                                                                                              // The tutorial box                 
    public static boolean alert = false;                                                                                                                                            // Is some type of a alert up?
    public static boolean warning = false;                                                                                                                                          // Is some type of a warning up?
    public static byte errorTime = 0;                                                                                                                                               // Number of secodns for the latest error to be visible
    public static String alertMessage = "None";                                                                                                                                     // The latest alert message
    public static String warningMessage = "";                                                                                                                                       // The latest warning message
    public static String errorMessage = "";                                                                                                                                         // The lastet error message
    public static ArrayList<Flower> flowers = new ArrayList<>();                                                                                                                    // ArrayList for all the flowers present in-game at a time
    public static ArrayList<Character> invisibleWalls = new ArrayList<Character>();                                                                                                 // ArrayList of map objects that are collidable
    public static ArrayList<Bird> birdList = new ArrayList<Bird>();                                                                                                                 // The list of birds currently in game for drawing
    public static Clip gameMusic;                                                                                                                                                   // The clip for playing the game music
    public static boolean pause = false;                                                                                                                                            // Determines wheather the game should be paused or not
    public static byte save = -1;                                                                                                                                                   // The current game save number to be plugged into the respawn
    public static boolean firstStart = true;                                                                                                                                        // Is this the first time the game instance is played? // Shows the tutorial menu inside the house
    public static String langFileName;                                                                                                                                              // Name of the language file currently being used
    public static String texturePack;                                                                                                                                               // The texture pack file path
    public static Font font;                                                                                                                                                        // The custom font used in the game
    public static double dayNightTransitionSpeed = .0;                                                                                                                              // The value that is added to the night block
    public static float volumeTransitionSpeed = .0f;                                                                                                                                // The value that is added to the volume gain
    public static byte FPS_SET = 120;                                                                                                                                               // Frame-Rate cap
    public static short gfps = 0;                                                                                                                                                   // The game current FPS rate
    public static short gtick = 0;                                                                                                                                                  // The game current logic tick rate
    public static GamePanel gamePanel;                                                                                                                                             // The panel that shows the game window
    private static Clip rainClip;                                                                                                                                                   // The rain audio
    private static boolean run = true;                                                                                                                                              // Determines wheather the game-loop should still run
    private static boolean isRaining = false;                                                                                                                                       // Is the current weather raining
    private static ArrayList<Long> dieTimes = new ArrayList<Long>();                                                                                                                // ArrayList for flower die times used when pausing the game
    private static int dayLasts = 0;                                                                                                                                                // How long does the day last in seconds
    private static int nightLasts = 0;                                                                                                                                              // How long does the night last in seconds
    private static float dayNumber = 0;                                                                                                                                             // How many days have past since the game started
    private Thread gameLoop;                                                                                                                                                        // The game loop itself
    private int seconds = 0;                                                                                                                                                        // Seconds since the game started
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
        Map.clear(map.getMap());
        Map.clear(houseMap.getMap());
        stage = "Day";
        isRaining = false;
        
        /*
         * --------------------------------------------------------------------------------
         * Building the main game objects (house, well, etc.)
         * --------------------------------------------------------------------------------
         */

        // Building the fence around the garden
        for (int i=0; i<map.getMap()[0].length; i++) {

            if (i == 0) map.write(i, 0, '5');
            if (i >= 3) map.write(i, 0, '5');
            map.write(i, 7, '5');

        }
        
        // Fill the outside map data
        map.write(1, 0, '3'); // House block
        map.write(2, 0, '3'); // House block
        map.write(1, 1, '3'); // House block
        map.write(2, 1, '3'); // House block
        map.write(1, 5, '4'); // The well
        map.write(2, 2, '6'); // Tile

        // Fill the house map
        houseMap.write(3, 5, '2'); // Empty box to make up for the tabe size
        houseMap.write(5, 6, '3'); // Doormat
        houseMap.write(1, 1, '4'); // Bed
        houseMap.write(8, 3, '5'); // Wardrobe
        houseMap.write(2, 5, '6'); // Table
        houseMap.write(1, 5, '7'); // Chair Left
        houseMap.write(4, 5, '7'); // Chair Right
        houseMap.write(6, 1, '8'); // TV
        houseMap.write(7, 2, '9'); // Couch
        
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
         * Setting up the main in-game panels and player spawn
         * --------------------------------------------------------------------------------
         */

        gamePanel = new GamePanel(window);
        InventoryPanel inventoryPanel = new InventoryPanel(gamePanel, gamePanel.dad);
        MainPanel mainPanel = new MainPanel(gamePanel, inventoryPanel);
        window.setElements(mainPanel);

        /*
         * --------------------------------------------------------------------------------
         * Setting up the tutorial text
         * --------------------------------------------------------------------------------
         */
        // Sets / Re-sets the tutorial box with every instance
        try {

            JSONEditor jsonEditor = new JSONEditor("../../res/Language/" + langFileName);
            tutorial.setRawData(jsonEditor.readData("Tutorial-Data"));
            
            Game.firstStart = Main.tutorial;
            tutorial.setVisibility(Game.firstStart);
            tutorial.setX(GamePanel.blockWidth * 10 - 40);
            tutorial.setY(50);

        } catch (IOException e) {

            e.printStackTrace();

        }

        tutorial.clearLines();

        // Get the number of lines for the tutorial box to draw them
        boolean sliced = false;
        for (int i=0; i<tutorial.getRawData().length(); i++) {

            if (tutorial.getRawData().charAt(i) == '\\' && tutorial.getRawData().charAt(i + 1) == 'n' || i == tutorial.getRawData().length()-1) {

                if (sliced) {

                    tutorial.addLine(tutorial.getRawData().substring(1, i));

                } else {

                    tutorial.addLine(tutorial.getRawData().substring(0, i));
                    sliced = true;

                }

                tutorial.setRawData(tutorial.getRawData().substring(i+1, tutorial.getRawData().length()));
                i = 0;

            }
        }

        // Starts the game and requests focus
        startGame();
        gamePanel.requestFocusInWindow();
        gamePanel.setIPanel(inventoryPanel);
        setClipVolume(gameMusic, Main.musicVolume);

        // Set the player starting position
        gamePanel.dad.level = 1;
        gamePanel.dad.LOCATION_X = GamePanel.blockWidth * 2 - 60;
        gamePanel.dad.LOCATION_Y = GamePanel.blockWidth * 1;
    }

    /*
     * --------------------------------------------------------------------------------
     * Methods for controlling the Game
     * --------------------------------------------------------------------------------
     */

    /**
     * Method to start the game loop
     */
    private void startGame() {

        run = true;
        gameLoop = new Thread(this);
        gameLoop.start();

        rainClip = playSound("../../res/" + texturePack + "/Audio/Rain.wav");
        rainClip.stop();
        rainClip.setFramePosition(0);

    }

    /**
     * Stops the game
     */
    public static void killGame() {

        run = false;
        gameMusic.stop();
        rainClip.stop();

    }

    /**
     * Pauses the game
     */
    public static void togglePauseGame() {

        try {

            Thread.sleep(100);

        } catch (InterruptedException e) {

            e.printStackTrace();
            
        }

        pause = pause ? false : true;

        // Pauses the game music
        if (pause) {

            stopClip(gameMusic);
            stopClip(rainClip);

        } else {

            gameMusic.start();
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

    /**
     * Figures out if it's raining or not
     * @return The isRaining value
     */
    public static boolean isRaining() {

        return isRaining;

    }

    /**
     * Gets how long the day should last in seconds
     * @return The number of seconds a day lasts
     * 
     */
    public static int dayLasts() { return dayLasts; }

    /**
     * Sets how long the day should last in seconds
     * @param value int
     * @return The new number of seconds a day lasts
     * 
     */
    public static int setDayLasts(int value) { return dayLasts = value; }

    /**
     * Gets how long the night should last in seconds
     * @return The number of seconds the night lasts
     * 
     */
    public static int nightLasts() { return nightLasts; }

    /**
     * Sets how long the night should last in seconds
     * @param value
     * @return The new number of seconds the night lasts
     * 
     */
    public static int setNightLasts(int value) { return nightLasts = value; }

    /**
     * Gets the number of days passed since the game started
     * @return The number of days that have passed
     * 
     */
    public static int getDay() { return (int) dayNumber; }

    /**
     * Adds a number to the number of days passed the game started
     * @param value
     * @return The number of days that have passed so far
     * 
     */
    public static int addDays(int value) { return (int) (dayNumber += value); }

    /**
     * Sets the number of days passed since the game started to a select value
     * @param value int
     * @return The number of days that have passed so far
     * 
     */
    public static int setDays(int value) { return (int) (dayNumber = value); }

    /**
     * Allows an error to be displayed for a certian amount of time
     * @param message string error message
     * @param duration int duration of the error being shown
     * 
     */
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

    /**
     * Spawns a bird
     */
    public void spawnBird() {

        System.out.println("Spawn Birb");
        birdList.add(new Bird(-1, gamePanel.dad.LOCATION_Y - 200 - random.nextInt(100)));

    }

    /**
     * Set texture to a specified image
     * @param path string path to the image
     * @return BufferedImage of the texture
     * 
     */
    public static BufferedImage setTexture(String path) {

        try {

            if (!new File(path).exists()) return ImageIO.read(new FileInputStream("../../res/Missing.png"));
            return ImageIO.read(new FileInputStream(path));

        } catch (IOException ioe) {

            System.err.println(ErrorList.ERR_404.message);
            Game.error("Texture not found", 3);
            return null;

        }
    }

    /**
     * Updates the alert that is currently shown
     * @param message the new message string
     * 
     */
    public static void alertUpdate(String message) { alertMessage = message; }

    /**
     * Shows a game alert and repaints the scene
     * @param message string
     * 
     */
    public static void alert(String message) {

        alert = true;
        alertMessage = message;

    }

    /**
     * Shows a game warning
     * @param message string
     */
    public static void warn(String message) {

        warning = true;
        warningMessage = message;

    }

    /*
     * --------------------------------------------------------------------------------
     * Methods for controlling the game audio
     * --------------------------------------------------------------------------------
     */

    /**
     * Plays the wav file at a given path
     * @param path string
     * @return The clpi that is being played
     * 
     */
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

    /**
     * Stops a provided clip
     * @param clip
     * 
     */
    public static void stopClip(Clip clip) { clip.stop(); }

    /**
     * Sets the volume of a given clip
     * @param clip Audio clip
     * @param volume float value between 0 (mute) - 1 (original volume)
     * @return The new volume
     * 
     */
    public static float setClipVolume(Clip clip, float volume) {

        if (volume < -80.0f || volume > 6.0f) {

            error("Audio volume error", 3);
            return volume;

        }

        FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        fc.setValue(volume);
        return volume;

    }

    /*
     * --------------------------------------------------------------------------------
     * Game Logic || Game tick Logic
     * --------------------------------------------------------------------------------
     */

    /**
     * Calculates the game logic tick
     */
    private void gameTick() {

        /*
         * --------------------------------------------------------------------------------
         * Bird flight logic
         * --------------------------------------------------------------------------------
         */

        for (int i=0; i<birdList.size(); i++) {

            Bird bird = birdList.get(i);

            // Bird flapping wings
            if (Map.translateX(bird.positionX) % 2 == 0) {

                bird.texture = setTexture("../../res/" + texturePack + "/Pics/Pigeon1.png");

            } else {

                bird.texture = setTexture("../../res/" + texturePack + "/Pics/Pigeon2.png");

            }

            // Bird shit detection
            if (bird.drawShit && Map.translateY(bird.shitPositionY - 30) == Map.translateY(gamePanel.dad.LOCATION_Y + 64) && Map.translateX(bird.shitPositionX) == Map.translateX(gamePanel.dad.LOCATION_X + 64)) {

                if (gamePanel.dad.HP == 0) gamePanel.dad.currentTexture = setTexture("../../res/" + texturePack + "/Pics/Player/GraveShit.png");
                if (gamePanel.dad.HP != 0) gamePanel.dad.hurt(5);

                bird.shitPositionY = Window.height;
                continue;

            }

            // Bird shit movement and bird removal after out of map
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
        if (gamePanel.dad.level == 0 && Map.translateX(gamePanel.dad.LOCATION_X + 64) == 2 && Map.translateY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY) == 1) {

            playSound("../../res/" + texturePack + "/Audio/DoorInteract.wav");
            gamePanel.dad.level = 1;
            gamePanel.dad.LOCATION_X = GamePanel.blockWidth * 5;
            gamePanel.dad.LOCATION_Y = GamePanel.blockWidth * 6;
            invisibleWalls.remove(invisibleWalls.indexOf('3'));
            invisibleWalls.add('6');

        }

        // Exit house logic
        if (gamePanel.dad.level == 1 && Map.translateX(gamePanel.dad.LOCATION_X + 64) == 5 && Map.translateY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY) == 7) {

            playSound("../../res/" + texturePack +  "/Audio/DoorInteract.wav");
            gamePanel.dad.level = 0;
            gamePanel.dad.LOCATION_X = GamePanel.blockWidth * 2 - GamePanel.blockWidth * 0.1;
            gamePanel.dad.LOCATION_Y = GamePanel.blockWidth * 2 - GamePanel.blockWidth * 0.4;
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
        
        if (map.read(Map.translateX(gamePanel.dad.LOCATION_X + 64), Map.translateY(gamePanel.dad.LOCATION_Y + 100)) == '6' && gamePanel.dad.level == 0) gamePanel.dad.speed = gamePanel.dad.dSpeed + .5;
        else gamePanel.dad.speed = gamePanel.dad.dSpeed;
    } 

    /*
     * --------------------------------------------------------------------------------
     * The game loop is written here
     * --------------------------------------------------------------------------------
     */

    @Override
    public void run() {

        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerTick = 1_000_000_000.0 / 20;
        long now;
        long previousTime = System.nanoTime();
        short fps = 0;
        short tick = 0;
        long lastCheck = System.currentTimeMillis();
        float deltaF = 0F;
        float deltaT = 0F;
        short animationTick = 100;

        // The in-game audio player
        try {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("../../res/" + texturePack + "/Audio/GameMusic.wav"));
            gameMusic = AudioSystem.getClip();
            gameMusic.open(audioStream);
            gameMusic.setFramePosition(0);
            gameMusic.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            System.err.println("Audio error has occured!");
            error("Audio Error", 3);

        }

        // While this loop runs, the game updates (game loop)
        while (run) {
            
            if (pause) {
                
                now = System.nanoTime();
                deltaF += (now - previousTime) * Math.pow(timePerFrame, -1);
                deltaT += (now - previousTime) * Math.pow(timePerTick, -1);
                previousTime = now;
                
                if (deltaF >= 1) deltaF--;
                if (deltaT >= 1) deltaT--;
                continue;
                
            }

            // Execute console commands
            Console.execChain();

            now = System.nanoTime();
            deltaF += (now - previousTime) * Math.pow(timePerFrame, -1);
            deltaT += (now - previousTime) * Math.pow(timePerTick, -1);
            previousTime = now;

            // Repaints 120 times per second
            if (deltaF >= 1) {

                /*
                 * --------------------------------------------------------------------------------
                 * Player Movement & colision logic
                 * --------------------------------------------------------------------------------
                 */
                
                // Y coordinate colision logic
                if (!(gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY < 0 || gamePanel.dad.LOCATION_Y + gamePanel.dad.VECTORY > Player.windowLimitY || invisibleWalls.contains((char) (Map.translate(Map.translateX(gamePanel.dad.LOCATION_X + 64), Map.translateY(gamePanel.dad.LOCATION_Y + 80 + gamePanel.dad.VECTORY), gamePanel.dad.level == 0 ? map : houseMap) + 48)))) gamePanel.dad.LOCATION_Y += gamePanel.dad.canMove() ? gamePanel.dad.VECTORY : 0;
                        
                // X coordinate colision logic
                if (!(gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX < 0 || gamePanel.dad.LOCATION_X + gamePanel.dad.VECTORX > Player.windowLimitX || invisibleWalls.contains((char) (Map.translate(Map.translateX(gamePanel.dad.LOCATION_X + 64 + gamePanel.dad.VECTORX), Map.translateY(gamePanel.dad.LOCATION_Y + 80), gamePanel.dad.level == 0 ? map : houseMap) + 48)))) gamePanel.dad.LOCATION_X += gamePanel.dad.canMove() ? gamePanel.dad.VECTORX : 0;

                // Bird & shit flight logic
                for (Bird bird : birdList) { bird.positionX += bird.vectorX; if (bird.shitPositionY < bird.positionY + 500) bird.shitPositionY += 2; }

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
                        setClipVolume(gameMusic, volumeGain);
                    
                    }
                }

                // Night -> Day fade out
                if (stage.equals("Day") && gamePanel.easeDayNight > 0) {
                
                    gamePanel.easeDayNight -= dayNightTransitionSpeed;
                    if (volumeGain < 0.0f) {
                    
                        volumeGain += volumeTransitionSpeed;
                        setClipVolume(gameMusic, volumeGain);
                    
                    }
                }

                // Move the rain img
                if (isRaining()) {

                    gamePanel.rainPositionY -= .5f;
                    if ((int) gamePanel.rainPositionY <= 0) gamePanel.rainPositionY = 432f;

                }

                // The main scene repaint DO NOT DELETE !!!
                if (gamePanel.hasFocus()) gamePanel.repaint();
                fps++;
                deltaF--;
            }

            /*
             * --------------------------------------------------------------------------------
             * Game logic calculations
             * --------------------------------------------------------------------------------
             * 20 times per second
             */
            if (deltaT >= 1) {
                gameTick();

                tick++;
                deltaT--;
            }

            // Animation tick | 10 times per second
            if (System.currentTimeMillis() - lastCheck >= animationTick) {

                animationTick += 100;

            }

            // The FPS counter. This occures ever second
            if (System.currentTimeMillis() - lastCheck >= 1000) {

                lastCheck = System.currentTimeMillis();
                System.out.println(ANSI_GREEN + "FPS: " + fps + " | TICK: " + tick + ANSI_RESET + "\nSecs < " + seconds + " >");

                if (Main.debug) System.out.println("LOCATION:\n\tX: " + gamePanel.dad.LOCATION_X + "\n\tY: " + gamePanel.dad.LOCATION_Y + "\nSPEED:\n\tX: " + gamePanel.dad.VECTORX + "\n\tY: " + gamePanel.dad.VECTORY + "\nNo. Birds: " + birdList.size());
                
                // Checks if the player is on level 0 "outside"
                if (gamePanel.dad.level == 0) gamePanel.changeGrass = true;

                // Spawns the birb
                if (gamePanel.dad.level == 0 && random.nextInt(100) == 0) spawnBird();

                // Bird shitting logic
                for (int i=0; i<birdList.size(); i++) {

                    Bird bird = birdList.get(i);
                    if (gamePanel.dad.level == 0 && Map.translateX(bird.positionX) == Map.translateX(gamePanel.dad.LOCATION_X + 64)) bird.shit();

                }
                
                // Replays the in-game music if it had reached the end.
                if (!gameMusic.isRunning() && gamePanel.dad.HP != 0) {

                    gameMusic.setFramePosition(0);
                    gameMusic.start();

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
                        map.write(plant.LOCATION_X, plant.LOCATION_Y, '0');
                        continue;
                    
                    }

                    if (isRaining() && !plant.STATUS.equals("Dead")) plant.resetTime();
                }

                // Resets the FPS counter each second
                gfps = fps;
                gtick = tick;
                fps = 0;
                tick = 0;
                seconds++;
                animationTick = 100;
                if (errorTime != 0) errorTime--;
                if (errorTime == 0) errorMessage = "";

                /*
                 * --------------------------------------------------------------------------------
                 * Stamina depleeting logic
                 * --------------------------------------------------------------------------------
                 */

                if ((gamePanel.dad.VECTORX != 0 || gamePanel.dad.VECTORY != 0) && gamePanel.dad.canMove() && !gamePanel.dad.isSitting) gamePanel.dad.tire(5);
                else gamePanel.dad.tire(-10);

                /*
                 * --------------------------------------------------------------------------------
                 * 0 Stamina penalty
                 * --------------------------------------------------------------------------------
                 */
                if (gamePanel.dad.stamina == 0) {

                    gamePanel.dad.outOfStamina = true;
                    gamePanel.dad.setTexture("Player/Dad_Texture_Low_Stamina_" + (gamePanel.dad.VECTORX > 0 ? "R" : "L") + gamePanel.dad.getTextureModifier() + ".png");
                    gamePanel.dad.setMove(false);
                    error("Out of stamina", 3);

                } else if (!gamePanel.dad.canMove() && gamePanel.dad.stamina == 100 && !gamePanel.dad.isSitting) {

                    gamePanel.dad.outOfStamina = false;
                    gamePanel.dad.setTexture("Player/Dad_Texture_" + (gamePanel.dad.VECTORX > 0 ? "R" : "L") + gamePanel.dad.getTextureModifier() + ".png");
                    gamePanel.dad.setMove(true);
                    gamePanel.getKeyboardInput().resetMovement();
                    gamePanel.dad.VECTORX = 0;
                    gamePanel.dad.VECTORY = 0;
                    
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
        gameMusic.stop();
    }

    /*
     * -------------------------------------------------------------------------
     * Save and load methods
     * -------------------------------------------------------------------------
     */

    /**
     * Saves the game progress into a seperate JSON file
     * @param saveFilePath string path where the game should be saved
     * @param dad Player
     * @param saveNumber byte save number
     * @throws FileNotFoundException
     * 
     */
    public static void saveGame(String saveFilePath, Player dad, byte saveNumber) throws FileNotFoundException, IOException {
        save = saveNumber;
        String mapData = "\"map\":\"" + map.getData("") + "\"";
        String value = "";

        // Foramts the flower information to be saved | {plant_number + plant_type : time_to_die | location X | location Y |}
        for (int i=0; i<flowers.size(); i++) {

            value += ",\"" + (flowers.get(i).PLANT_NUMBER + flowers.get(i).TYPE) + "\":\"" + ((flowers.get(i).TIME_TO_DIE - System.currentTimeMillis()) + "!" + flowers.get(i).LOCATION_X + "!" + flowers.get(i).LOCATION_Y) + "!\"";

        }

        if (!new File(saveFilePath).exists()) JSONEditor.createFile(saveFilePath);
        JSONEditor jEditor = new JSONEditor(saveFilePath);
        jEditor.write(mapData + value);

        if (Main.debug) System.out.println("Game saved succesfully into \"" + saveFilePath + "\"");
    }

    /**
     * Loads the game progress from a given save
     * @param saveFilePath string path to the save file
     * @param saveNumber byte save number
     * @throws FileNotFoundException
     * 
     */
    public static void loadGame(String saveFilePath, byte saveNumber) throws FileNotFoundException {
        
        // Loads the map
        firstStart = false;
        tutorial.setVisibility(false);
        save = saveNumber;
        JSONEditor jEditor = new JSONEditor(saveFilePath);
        String[][] strMap = jEditor.read2DArr();
        String mapValues = strMap[0][1];
        String value = "";

        for (int i=0, num = 0; i<mapValues.length(); i++) {

            if (mapValues.charAt(i) == '!' && i != mapValues.length()-1) {

                for (int j=0; j<value.length(); j++) {

                    map.write(j, num, value.charAt(j));

                }

                value = "";
                num++;

            } else value += mapValues.charAt(i);
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
}
