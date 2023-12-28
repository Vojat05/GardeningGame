package com.vojat.garden;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.Data.Map;
import com.vojat.Enums.ErrorList;
import com.vojat.inputs.*;
import com.vojat.menu.Window;

public class GamePanel extends JPanel {

    /*
     * --------------------------------------------------------------------------------
     * Game panel variables
     * --------------------------------------------------------------------------------
     */

    public static Boolean overlay;                                                          // The FPS & Tick overlay
    public static int blockWidth = 0;                                                       // The width of blocks to be rendered | For FullHD = 128
    public Player dad = new Player(this, 0, 0);                                             // The player instance
    public InventoryPanel inventoryPanel;                                                   // Inventory panel used to display selected item
    public JPanel fullInv = new JPanel();                                                   // Player inventory panel visible after pressing "T"
    public boolean changeGrass = true;                                                      // Determines wheather the grass should have a wind effect applied
    public boolean saveMenuOpen = false;                                                    // Should the save menu be shown
    public boolean skinMenuOpen = false;                                                    // Determines if the wardrobe is open
    public double easeDayNight = .0;                                                        // Makes the Day -> Night cycle more fluent
    public float rainPositionY = 432f;                                                      // Vertical position of the rain
    public int selectedSkinSlot = 0;                                                        // The number of a selected skin slot
    private static BufferedImage rainBase;                                                  // A base image for the rain to create a subimage from
    private int hoverSaveSlotNumber = 0;                                                    // Number of a save slot that is currently in hover
    private int hoverSkinSlot = 0;                                                          // Number of a skin slot that is currently in hover
    private MouseInput mouseInput = new MouseInput(this);                                   // The mouse input class ( Used for the save box hover effect )
    private KeyboardInput keyboardInput = new KeyboardInput(this, dad);                     // The keyboard input class
    private int selectedSaveSlotNumber = 1;                                                 // Number of a save slot into which the game should be saved
    private HashMap<String, Image> textures = new HashMap<String, Image>();                 // A Hash Map containing all ground textures | structure: <Key:path | Value:image>

    /*
     * --------------------------------------------------------------------------------
     * Methods for interaction with game logic
     * --------------------------------------------------------------------------------
     */

    // width == window width & height == window height
    public GamePanel(Window window) {

        // Calculate the block width size
        blockWidth = (int) (Window.width * Math.pow(Game.map.getColumns(), -1));
        
        dad.setLimit(Window.width, Window.height-50);
        setFocusable(true);         // Sets the JPanel focusable, it is later packed into the JFrame

        /*
         * --------------------------------------------------------------------------------
         * Game panel visuals & location setup
         * --------------------------------------------------------------------------------
         */

        {
            setBounds(0, 0, Window.width, Window.height-50);
            setPreferredSize(new Dimension(Window.width, Window.height-50));
            setBackground(null);
            setBorder(new LineBorder(Color.BLACK));
        }

        /*
         * --------------------------------------------------------------------------------
         * Adding the mouse & keyboard listeners
         * --------------------------------------------------------------------------------
         */
        
        {
            addKeyListener(keyboardInput);
            addMouseListener(mouseInput);
            addMouseMotionListener(mouseInput);
            addMouseWheelListener(mouseInput);
        }

        /*
         * --------------------------------------------------------------------------------
         * Inventory panel setup     | Visible after pressing "T"
         * --------------------------------------------------------------------------------
         */
        
        {
            fullInv.setBorder(new LineBorder(Color.BLACK));
            fullInv.setPreferredSize(new Dimension(Window.width - 20, 80));
            fullInv.setBackground(new Color(0, 0, 0, 50));

            for (int i=0; i<dad.inventory.size(); i++) {

                JLabel item = new JLabel();
                InventoryPanel.repaintItem(i, item, dad);
                fullInv.add(item);

            }

            fullInv.setVisible(false);
            add(fullInv);
        }

        /*
         * --------------------------------------------------------------------------------
         * Filling up the texture HashMaps
         * --------------------------------------------------------------------------------
         */

        {
            textures.put("Missing.png", new ImageIcon("../../res/Missing.png").getImage());
            for (int i=0; i < Game.groundTextures.length; i++) { 
                
                if (Game.groundTextures[i].equals("")) { continue; }
                if (!(new File("../../res/" + Game.texturePack + "/Pics/Garden/" + Game.groundTextures[i]).exists())) { textures.put(Game.groundTextures[i], new ImageIcon("../../res/Missing.png").getImage()); continue; }
                textures.put(Game.groundTextures[i], new ImageIcon("../../res/" + Game.texturePack + "/Pics/Garden/" + Game.groundTextures[i]).getImage()); 
            
            }

            for (int i=0; i < Game.houseTextures.length; i++) { 
                
                if (Game.houseTextures[i].equals("")) { continue; }
                else if (Game.houseTextures[i].equals("chair.png")) {

                    if (!(new File("../../res/" + Game.texturePack + "/Pics/House/chair_left.png").exists())) textures.put("chair_left.png", new ImageIcon("../../res/Missing.png").getImage());
                    else textures.put("chair_left.png", new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/chair_left.png").getImage());textures.put("chair_left.png", new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/chair_left.png").getImage());
                    
                    if (!(new File("../../res/" + Game.texturePack + "/Pics/House/chair_right.png").exists())) textures.put("chair_right.png", new ImageIcon("../../res/Missing.png").getImage());
                    else textures.put("chair_right.png", new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/chair_right.png").getImage());

                }

                if (!(new File("../../res/" + Game.texturePack + "/Pics/House/" + Game.houseTextures[i]).exists())) { textures.put(Game.houseTextures[i], new ImageIcon("../../res/Missing.png").getImage()); continue; }
                textures.put(Game.houseTextures[i], new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + Game.houseTextures[i]).getImage()); 
            
            }

            String[] textureNames = {"cornerTL", "cornerBL", "window", "wallT", "wallB", "wallL", "wallR", "cornerTR", "cornerBR", "door"};

            for (String texture : textureNames) {
                
                if ((new File("../../res/" + Game.texturePack + "/Pics/House/" + texture + ".png")).exists()) {

                    textures.put(texture + ".png", new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + texture + ".png").getImage());

                } else {

                    textures.put(texture + ".png", new ImageIcon("../../res/Missing.png").getImage());
                    
                }
            }
        }

        /*
         * --------------------------------------------------------------------------------
         * Initialize the rain base image
         * --------------------------------------------------------------------------------
         */

        try {

            rainBase = ImageIO.read(new File("../../res/Missing.png"));
            rainBase = ImageIO.read(new File("../../res/" + Game.texturePack + "/Pics/Rain.png"));

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    // Sets the inventory panel field (just for the repaint method to be functional in the listener)
    public void setIPanel(InventoryPanel iPanel) {

        this.inventoryPanel = iPanel;

    }

    // Adds a flower to flowers ArrayList
    public void summonFlower(Flower flower) {

        Game.flowers.add(flower);

    }

    // Changes the visibility of an inventory table
    public void changeVisibility(JPanel panel) {

        panel.setVisible(panel.isVisible() ? false : true);

    }

    // Changes the save menu box visibility to true
    public void showSaveMenu() {

        this.saveMenuOpen = true;

    }

    // Changes the save menu box visibility to false
    public void hideSaveMenu() {

        this.saveMenuOpen = false;

    }

    // Sets the save slot number
    public void setSaveNumber(int value) {

        this.selectedSaveSlotNumber = value;
        this.repaint();

    }

    // Returns the save slot number
    public int getSaveNumber() {

        return this.selectedSaveSlotNumber;

    }

    // Sets the save slot number that is in mouse hover
    public void setHoverSaveNumber(int value) {

        this.hoverSaveSlotNumber = value;

    }

    // Sets the skin slot number that is in mouse hover
    public void setHoverSkinNumber(int value) {

        this.hoverSkinSlot = value;

    }

    // Sets the skin slot number
    public void setSelectedSkinNumber(int value) {
        
        this.selectedSkinSlot = value;

    }

    // Gets the slot number that is in mouse hover
    public int getHoverSaveNumber() {
        
        return this.hoverSaveSlotNumber;

    }

    // Gets the mouseInput used in this panel
    public MouseInput getMouseInput() {

        return this.mouseInput;

    }

    public KeyboardInput getKeyboardInput() {

        return this.keyboardInput;

    }

    // Finds the plant in the flowers ArrayList and runs checks if it's dead or not, if passed, restores texture and resets death timer
    public void waterFlower(Flower flower) {

        // Checks if the flower is dead and returns
        if (flower.STATUS.equals("Dead")) return;

        // Resets the flower times to die and dissapear
        for (int i=0; i<Game.flowerTypes.length; i++) {

            if (flower.TYPE.equals(Game.flowerTypes[i][0])) {

                flower.TIME_TO_DIE = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]);
                flower.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]) + 5000;
                flower.CURRENT_TEXTURE = flower.setTexture(flower.ALIVE_TEXTURE);
                flower.STATUS = "Alive";
                break;

            }
        }

        // Resets the flower texture
        flower.CURRENT_TEXTURE = flower.setTexture(flower.ALIVE_TEXTURE);
        if (this.hasFocus()) this.inventoryPanel.repaint();
    }

    /*
     * --------------------------------------------------------------------------------
     * Drawing the terrain based on the player's level
     * --------------------------------------------------------------------------------
     */

    private void drawTerrain(Map map, Graphics2D g) {

        // Draws the interior of the house
        if (dad.level == 1) {

            // Drawing the grass background
            for (int i=0; i<Game.houseMap.getMap().length; i++) {

                for (int j=0; j<Game.houseMap.getMap()[0].length; j++) {
    
                    g.drawImage(textures.get(Game.groundTextures[0]), blockWidth*j, blockWidth*i, blockWidth, blockWidth, null);
    
                }
            }

            // Drawing the plank floor inside
            for (int i=1; i<7; i++) {

                for (int j=1; j<9; j++) {
    
                    g.drawImage(new ImageIcon(textures.get(Game.houseTextures[0])).getImage(), blockWidth*j, blockWidth*i, blockWidth, blockWidth, null);
    
                }
            }

            // Drawing the house walls
            // Horizontal
            for (int i=0; i<10; i++) {
                Game.houseMap.write(i, 0, '2');
                Game.houseMap.write(i, 7, '2');

                if (i == 0) {

                    g.drawImage(textures.get("cornerTL.png"), blockWidth*i, 0, blockWidth, blockWidth, null);
                    g.drawImage(textures.get("cornerBL.png"), blockWidth*i, blockWidth*7, blockWidth, blockWidth, null);
                    continue;

                } else if (i == 2) {

                    g.drawImage(textures.get("window.png"), blockWidth*i, blockWidth*7, blockWidth, blockWidth, null);
                    g.drawImage(textures.get("wallT.png"), blockWidth*i, 0, blockWidth, blockWidth, null);
                    continue;

                } else if (i == 5) {

                    g.drawImage(textures.get("door.png"), blockWidth*i, blockWidth*7, blockWidth, blockWidth, null);
                    g.drawImage(textures.get("wallT.png"), blockWidth*i, 0, blockWidth, blockWidth, null);
                    continue;

                } else if (i == 9) {

                    g.drawImage(textures.get("cornerTR.png"), blockWidth*i, 0, blockWidth, blockWidth, null);
                    g.drawImage(textures.get("cornerBR.png"), blockWidth*i, blockWidth*7, blockWidth, blockWidth, null);
                    continue;

                }

                g.drawImage(textures.get("wallB.png"), blockWidth*i, blockWidth*7, blockWidth, blockWidth, null);
                g.drawImage(textures.get("wallT.png"), blockWidth*i, 0, blockWidth, blockWidth, null);

            }

            // Vertical
            for (int i=1; i<7; i++) {
                Game.houseMap.write(0, i, '2');
                Game.houseMap.write(9, i, '2');

                g.drawImage(textures.get("wallL.png"), 0, blockWidth*i, blockWidth, blockWidth, null);
                g.drawImage(textures.get("wallR.png"), blockWidth*9, blockWidth*i, blockWidth, blockWidth, null);

            }

            for (int i=0; i<map.getMap().length; i++) {

                for (int j=0; j<map.getMap()[0].length; j++) {

                    // Drawing the objects
                    if ((int) map.read(j, i) - 48 > 1) {

                        if (map.read(j, i) == '8') {

                            // The TV - wall offset
                            g.drawImage(textures.get(Game.houseTextures[(int) map.read(j, i) - 48]), blockWidth*j, blockWidth*i-30, blockWidth, blockWidth, null);

                        } else if (i == 5 && ( j == 1 || j == 4 )) {

                            // Draws the chairs with their respective orientation
                            if (j == 1) g.drawImage(textures.get((Game.houseTextures[(int) map.read(j, i) - 48]).substring(0, 5) + "_left.png"), blockWidth*j+60, blockWidth*i+20, blockWidth, blockWidth, null);
                            else g.drawImage(textures.get((Game.houseTextures[(int) map.read(j, i) - 48]).substring(0, 5) + "_right.png"), blockWidth*j-40, blockWidth*i+20, blockWidth, blockWidth, null);

                        } else if (map.read(j, i) == '6') {

                            // The table resizing
                            g.drawImage(textures.get(Game.houseTextures[(int) map.read(j, i) - 48]), blockWidth*j, blockWidth*i-52, blockWidth * 2, blockWidth * 2, null);

                        } else if (map.read(j, i) == '5') {

                            // The wardrobe - wall offset
                            g.drawImage(textures.get(Game.houseTextures[(int) map.read(j, i) - 48]), blockWidth*j+40, blockWidth*i-30, blockWidth, blockWidth, null);

                        } else {
    
                            g.drawImage(textures.get(Game.houseTextures[(int) map.read(j, i) - 48]), blockWidth*j, blockWidth*i, blockWidth, blockWidth, null);
    
                        }
                    }
                }
            }
        }
        

        try {

            if (dad.level == 0) {

                // Drawing the grass textures and other static objects (well, house, etc.)
                for (int i=0; i<map.getMap().length; i++) {

                    for (int j=0; j<map.getMap()[0].length; j++) {

                        // Interaction with map done via ASCII table values | 49 == '1'
                        if ((int) map.read(j, i) - 48 <= 1 && changeGrass) {

                            // Changes the grass in map value if the player is outside
                            Random rnd = new Random();
                            map.write(j, i, (char) (48 + rnd.nextInt(2)));

                        }

                        // Draw everything except flowers and house
                        if ((int) map.read(j, i) - 48 != 3 || (int) map.read(j, i) - 48 != 2) {

                            if (map.read(j, i) == '6') {

                                g.drawImage(textures.get(Game.groundTextures[0]), blockWidth*j, blockWidth*i, blockWidth, blockWidth, null);
                                g.drawImage(textures.get(Game.groundTextures[6]), blockWidth*j, blockWidth*i, blockWidth, blockWidth, null);
                                continue;

                            }

                            g.drawImage(textures.get(Game.groundTextures[(int) map.read(j, i) - 48]), blockWidth*j, blockWidth*i, blockWidth, blockWidth, null);

                        }
                    }
                }
                
                // Draw the house itself
                g.drawImage(textures.get(Game.groundTextures[(int) map.read(1, 0) - 48]), blockWidth, 0, blockWidth * 2, blockWidth * 2, null);
                changeGrass = false;

                // Drawing all the placed plants by a for loop to allow editing the plants
                for (int i=0; i<Game.flowers.size(); i++) {

                    Flower plant = Game.flowers.get(i);

                    // Draw the flowers
                    g.drawImage(plant.baseTexture, plant.LOCATION_X * blockWidth, plant.LOCATION_Y * blockWidth, blockWidth, blockWidth, null);
                    g.drawImage(plant.CURRENT_TEXTURE, plant.LOCATION_X * blockWidth, plant.LOCATION_Y * blockWidth, blockWidth, blockWidth, null);
                }

                // Drawing the side fence poles
                for (int i=2; i<32; i++) {

                    g.drawImage(textures.get(Game.groundTextures[7]), 1877, i*30, 22, 96, null);

                }

                // Drawing the birds
                for (int i=0; i<Game.birdList.size(); i++) {

                    Bird bird = Game.birdList.get(i);
                    g.drawImage(bird.texture, (int) bird.positionX, (int) bird.positionY, 76, 71, null);

                }
            }

            // Drawing the errors
            g.setFont(Game.font.deriveFont(24f));
            g.setColor(new Color(238, 16, 16));
            g.drawString(Game.errorMessage, (int) dad.LOCATION_X, (int) dad.LOCATION_Y + 10);

        } catch(NullPointerException npe) {

            System.out.println(npe.getMessage());
            System.err.println(ErrorList.ERR_NPE.message);
            Game.error("Null Pointer Exception Draw", 3);

        }
    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the bird shit mid air and the splat on the ground
     * --------------------------------------------------------------------------------
     */

    private void drawBirdShit(Graphics2D g) {

        for (int i=0; i<Game.birdList.size(); i++) {

            Bird bird = Game.birdList.get(i);
            if (!bird.drawShit && !bird.splat) continue;

            // Drawing the bird shit splat on the ground
            if (!bird.drawShit && bird.splat && System.currentTimeMillis() < bird.timeToCleanShit) {

                if (bird.audio) Game.playSound("../../res/" + Game.texturePack + "/Audio/Splash.wav");
                bird.audio = false;
                
                g.setPaint(new Color(236, 236, 236));
                g.fillOval((int) bird.shitPositionX, (int) bird.shitPositionY, 15, 15);
                g.setPaint(new Color(191, 191, 191));
                g.fillOval((int) bird.shitPositionX + 3, (int) bird.shitPositionY + 2, 10, 10);
                continue;

            }

            if (!bird.drawShit) continue;

            // The white part
            g.setPaint(new Color(236, 236, 236));
            g.fillRect((int) bird.shitPositionX, (int) bird.shitPositionY, 10, 10);

            // The gray part
            g.setPaint(new Color(191, 191, 191));
            g.fillRect((int) bird.shitPositionX, (int) bird.shitPositionY - 10, 10, 10);

        }
    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the agree button
     * --------------------------------------------------------------------------------
     */


    private void drawAgreeButton(Graphics2D g2d, int x, int y) {

        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(x - 17, y, 50, 50);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(x - 14, y + 3, 44, 44);
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(x - 11, y + 6, 38, 38);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(x - 4, y + 13, 24, 24);
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(x, y + 17, 16, 16);

    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the reject button
     * --------------------------------------------------------------------------------
     */


    private void drawRejectButton(Graphics2D g2d, int x, int y) {

        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(x, y, 50, 50);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(x + 3, y + 3, 44, 44);
        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(x + 6, y + 6, 38, 38);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(x + 18, y + 18, x + 30, y + 31);
        g2d.drawLine(x + 30, y + 18, x + 18, y + 31);

    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the warning panel
     * --------------------------------------------------------------------------------
     */


    private void drawWarning(Graphics2D g2d) {

        // Drawing the red hexagon background
        g2d.setPaint(new Color(231, 44, 22, 155));

        int middleX = (int) (this.getWidth() * 0.5);
        int middleY = (int) (this.getHeight() * 0.5);

        int[] backgroundX = {middleX - 200, middleX + 200, middleX + 250, middleX + 200, middleX - 200, middleX - 250};
        int[] backgroundY = {(int) (middleY - middleY * 0.5), (int) (middleY - middleY * 0.5), (int) (middleY - middleY * 0.5) + 86, (int) (middleY - middleY * 0.5) + 172, (int) (middleY - middleY * 0.5) + 172, (int) (middleY - middleY * 0.5) + 86};

        g2d.fillPolygon(backgroundX, backgroundY, backgroundX.length);

        // Drawing the red hexagon border
        g2d.setPaint(new Color(105, 7, 7, 220));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawPolygon(backgroundX, backgroundY, backgroundX.length);

        // Drawing the text inside
        g2d.setFont(Game.font.deriveFont(64f));
        g2d.drawString(Game.warningMessage, middleX - Game.warningMessage.length() * 11, (int) ((int) (middleY - middleY * 0.5) + 107.5));

        if (this.hasFocus()) this.repaint();

    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the alert panel
     * --------------------------------------------------------------------------------
     */


    private void drawAlert(Graphics2D g2d) {

        int middleX = (int) (this.getWidth() * 0.5);
        int middleY = (int) (this.getHeight() * 0.5);

        // The upper white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * 0.5) + 180, 400, 80);

        // The middle gray rectangle
        g2d.setPaint(new Color(210, 210, 210, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * 0.5) + 260, 400, 120);
        
        // The bottom white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * 0.5) + 380, 400, 80);
        

        // Alert box title
        g2d.setFont(Game.font.deriveFont(42f));
        g2d.setPaint(new Color(30, 30, 30, 240));
        g2d.drawString("Alert", middleX - 35, (int) (middleY - middleY * 0.5) + 240);

        g2d.setFont(Game.font.deriveFont(24f));
        g2d.drawString(Game.alertMessage, middleX - Game.alertMessage.length() * 4, (int) (middleY - middleY * 0.5) + 330);


        // The selection buttons

        // Agree button
        drawAgreeButton(g2d, middleX - 133, (int) (middleY - middleY * 0.5) + 395);

        // Rejct button
        drawRejectButton(g2d, middleX + 100, (int) (middleY - middleY * 0.5) + 395);

        if (this.hasFocus()) this.repaint();

    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the save panel
     * --------------------------------------------------------------------------------
     */


    private void drawSaveBox(Graphics2D g2d, int marginTopPx) {
        
        int middleX = (int) (this.getWidth() * 0.5);
        int middleY = (int) (this.getHeight() * 0.5);

        // The upper white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * 0.5) - 100, 400, 80);

        // The middle gray rectangle
        g2d.setPaint(new Color(210, 210, 210, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * 0.5) - 20, 400, 400);
        
        // The bottom white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * 0.5) + 380, 400, 80);
        

        // Save box title
        g2d.setFont(Game.font.deriveFont(42f));
        g2d.setPaint(new Color(30, 30, 30, 240));
        g2d.drawString("Save", middleX - 35, (int) (middleY - middleY * 0.5) - 40);

        // The save slots
        for (int i=1; i<=6; i++) {

            // Drawing the box
            g2d.setPaint(new Color(30, 30, 30, 240));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(middleX - 180, (int) (middleY - middleY * 0.5) - 65 + ( 60 * i + marginTopPx ), 360, 50);
            g2d.setPaint(i == hoverSaveSlotNumber ? new Color(255, 217, 29) : i == selectedSaveSlotNumber ? new Color(48, 222, 17) : new Color(245, 245, 245, 245));
            g2d.fillRect(middleX - 179, (int) (middleY - middleY * 0.5) - 64 + ( 60 * i + marginTopPx ), 358, 48);
            
            // Drawing the slot text
            g2d.setFont(Game.font.deriveFont(36f));
            g2d.setPaint(new Color(30, 30, 30, 240));
            g2d.drawString("Slot " + i, middleX - 36, (int) (middleY * 0.5) - 25 + ( 60 * i + marginTopPx ));

        }


        // The selection buttons

        // Agree button
        drawAgreeButton(g2d, middleX - 150, (int) (middleY - middleY * 0.5) + 395);

        // Rejct button
        drawRejectButton(g2d, middleX + 100, (int) (middleY - middleY * 0.5) + 395);

        if (this.hasFocus()) this.repaint();

    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the tutorial panel
     * --------------------------------------------------------------------------------
     */


    private void drawHelpScreen(Graphics2D g2d) {
        // Drawing the Help screen inside the house if it's started for the first time
        int x, y;
        x = blockWidth * 10;
        y = 50;

        // The first block // White
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(x, y, 650, 100);

        // The second block // Gray
        g2d.setPaint(new Color(210, 210, 210, 245));
        g2d.fillRect(x, y + 100, 650, 700);

        // The third block // White
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(x, y + 800, 650, 100);

        // The Help text in the first white block
        g2d.setPaint(new Color(30, 30, 30, 240));
        g2d.setFont(Game.font.deriveFont(48f));
        g2d.drawString("Tutorial", x + 255, y + 70);

        // Tutorial box content
        g2d.setFont(Game.font.deriveFont(24f));

        for (int i=0; i<Game.tutorial.getLines(); i++) {

            g2d.drawString(Game.tutorial.getLine(i), x + 30, y + 150 + 30*i);

        }

        g2d.drawString(Game.tutorial.getRawData(), x + 30, y + 150);

        // Rejct button
        drawRejectButton(g2d, x + 305, y + 825);

    }


    /*
     * --------------------------------------------------------------------------------
     * Drawing the skin selection panel
     * --------------------------------------------------------------------------------
     */


    private void drawSkinBox(Graphics2D g2d) {

        int middleX = (int) (this.getWidth() * 0.5);
        int middleY = (int) (this.getHeight() * 0.5);
        int y = (int) (middleY - middleY * 0.5);

        // The upper white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 400, y, 800, 80);

        // The middle gray rectangle
        g2d.setPaint(new Color(210, 210, 210, 245));
        g2d.fillRect(middleX - 400, y + 80, 800, 400);

        // The bottom white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 400, y + 480, 800, 80);

        // The skin box name
        g2d.setFont(Game.font.deriveFont(48f));
        g2d.setPaint(new Color(30, 30, 30, 240));
        g2d.drawString("Select Skin", middleX - 80, (int) (middleY - middleY * 0.5 + 60));

        // The skin previews to be selected
        for (int i=0; i<4; i++) {

            int x = (int) ((i % 2 == 0 ? 300 : 150) * Math.pow(-1, i + 1));
            int y2 = (int) (middleY - middleY * 0.5) + (i < 2 ? 100 : 300);

            g2d.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + i + ".png").getImage(), middleX + x, y2, 160, 160, null);
            if (i == selectedSkinSlot || i == hoverSkinSlot) {

                g2d.setPaint(i == selectedSkinSlot ? new Color(48, 222, 17) : new Color(255, 217, 29));
                g2d.setStroke(new BasicStroke(6));
                g2d.drawRect(middleX + x, y2, 160, 160);

            }
        }

        // Action buttons

        // Agree button
        drawAgreeButton(g2d, middleX - 250, (int) (middleY - middleY * 0.5) + 495);

        // Rejct button
        drawRejectButton(g2d, middleX + 200, (int) (middleY - middleY * 0.5) + 495);

        if (this.hasFocus()) this.repaint();

    }


    


    /*
     * --------------------------------------------------------------------------------
     * Game panel repaint method
     * --------------------------------------------------------------------------------
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Smoothening render hint
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        g2d.setRenderingHints(rh);
        
        drawTerrain(dad.level == 0 ? Game.map : Game.houseMap, g2d);
        if (dad.level == 0) drawBirdShit(g2d);

        // Drawing the player character in 128 x 128
        g2d.drawImage(dad.currentTexture, (int) dad.LOCATION_X, (int) dad.LOCATION_Y, blockWidth, blockWidth, null);

        if (Game.isRaining()) {

            BufferedImage raingImg = rainBase.getSubimage(0, (int) rainPositionY, 384, 216);
            g2d.drawImage(raingImg, dad.level == 0 ? 0 : 1200, 0, 1920, 1080, null);

        }

        // Draw night
        if (easeDayNight > 0) {

            Color nightColor = new Color(6, 10, 12, (int) easeDayNight);
            g2d.setPaint(nightColor);

            if (dad.selectedItem == 2) {

                Point2D center = new Point2D.Float((float) (dad.LOCATION_X + 64), (float) (dad.LOCATION_Y + 64));
                float radius = 150f;
                float[] dist = {0.0f, 1.0f};
                Color[] colors = {new Color(100, 80, 20, (int) (easeDayNight - easeDayNight * Math.pow(5, -1))), nightColor};
                RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);

                g2d.setPaint(gradient);
                g2d.fillArc((int) (dad.LOCATION_X - Window.width), (int) (dad.LOCATION_Y - Window.width), (int) (Window.width * 2.5), (int) (Window.width * 2.5), 0, 360);

            } else g2d.fillRect(0, 0, Window.width, Window.height);

        }

        // Drawing the help menu box
        if (dad.level == 1 && Game.firstStart || Game.tutorial.isVisible()) { drawHelpScreen(g2d); }

        // Drawing the death screen
        if (dad.HP == 0) {

            drawWarning(g2d);
            drawAlert(g2d);
            return;

        }

        // Drawing the overlay
        if (GamePanel.overlay) {

            g2d.setFont(g2d.getFont().deriveFont(24f));
            g2d.setPaint(Color.BLACK);
            g2d.drawString("FPS: " + Game.gfps + "  Tick: " + Game.gtick, Window.width - 165, 25);
        }

        if (Game.warning) drawWarning(g2d);
        else if (Game.alert) drawAlert(g2d);
        else if (saveMenuOpen) drawSaveBox(g2d, 10);
        else if (skinMenuOpen) drawSkinBox(g2d);

    }
}
