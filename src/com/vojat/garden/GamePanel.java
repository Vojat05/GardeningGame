package com.vojat.garden;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.Enums.ErrorList;
import com.vojat.inputs.*;
import com.vojat.menu.Window;

public class GamePanel extends JPanel {

    /*
     * --------------------------------------------------------------------------------
     * Game panel variables
     * --------------------------------------------------------------------------------
     */

    public Player dad = new Player(this, 240, 200);                                         // The player instance
    public InventoryPanel inventoryPanel;                                                   // Inventory panel used to display selected item
    public JPanel fullInv = new JPanel();                                                   // Player inventory panel visible after pressing "T"
    public boolean changeGrass = true;                                                      // Determines wheather the grass should have a wind effect applied
    public boolean saveMenuOpen = false;                                                    // Should the save menu be shown
    public double easeDayNight = .0;                                                        // Makes the Day -> Night cycle more fluent
    public float rainPositionY = 432f;                                                      // Vertical position of the rain
    private int hoverSaveSlotNumber = 0;                                                    // Number of a save slot that is currently in hover
    private MouseInput mouseInput = new MouseInput(this);                                   // The mouse input class ( Used for the save box hover effect )
    private int selectedSaveSlotNumber = 1;                                                 // Number of a save slot into which the game should be saved
    private BufferedImage raingImg;                                                         // The full image of rain

    /*
     * --------------------------------------------------------------------------------
     * Methods for interaction with game logic
     * --------------------------------------------------------------------------------
     */

    // width == window width & height == window height
    public GamePanel(int windowWidth, int windowHeight, Window window) {
        dad.setLimit(windowWidth, windowHeight-50);
        setFocusable(true);         // Sets the JPanel focusable, it is later packed into the JFrame

        /*
         * --------------------------------------------------------------------------------
         * Game panel visuals & location setup
         * --------------------------------------------------------------------------------
         */

        {
            setBounds(0, 0, windowWidth, windowHeight-50);
            setPreferredSize(new Dimension(windowWidth, windowHeight-50));
            setBackground(null);
            setBorder(new LineBorder(Color.BLACK));
        }

        /*
         * --------------------------------------------------------------------------------
         * Adding the mouse & keyboard listeners
         * --------------------------------------------------------------------------------
         */
        
        {
            addKeyListener(new KeyboardInput(this, dad));
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
            fullInv.setPreferredSize(new Dimension(windowWidth - 20, 80));
            fullInv.setBackground(new Color(0, 0, 0, 50));

            for (int i=0; i<dad.inventory.size(); i++) {

                JLabel item = new JLabel();
                InventoryPanel.repaintItem(i, item, dad);
                fullInv.add(item);

            }

            fullInv.setVisible(false);
            add(fullInv);
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

    // Sets the slot number that is in mouse hover
    public void setHoverSaveNumber(int value) {

        this.hoverSaveSlotNumber = value;

    }

    // Gets the slot number that is in mouse hover
    public int getHoverSaveNumber() {
        
        return this.hoverSaveSlotNumber;

    }

    // Gets the mouseInput used in this panel
    public MouseInput getMouseInput() {

        return this.mouseInput;

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

    private void drawTerrain(char[][] map, Graphics2D g) {

        // Draws the interior of the house
        if (dad.level == 1) {

            // Drawing the grass background
            for (int i=0; i<Game.houseMap.length; i++) {

                for (int j=0; j<Game.houseMap[0].length; j++) {
    
                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Garden/" + Game.groundTextures[0]).getImage(), 128*j, 128*i, 128, 128, null);
    
                }
            }

            // Drawing the plank floor inside
            for (int i=1; i<7; i++) {

                for (int j=1; j<9; j++) {
    
                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + Game.houseTextures[0]).getImage(), 128*j, 128*i, 128, 128, null);
    
                }
            }

            // Drawing the house walls
            // Horizontal
            for (int i=0; i<10; i++) {
                Game.houseMap[0][i] = '2';
                Game.houseMap[7][i] = '2';

                if (i == 0) {

                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/cornerTL.png").getImage(), 128*i, 0, 128, 128, null);
                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/cornerBL.png").getImage(), 128*i, 128*7, 128, 128, null);
                    continue;

                } else if (i == 2) {

                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/window.png").getImage(), 128*i, 128*7, 128, 128, null);
                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/wallT.png").getImage(), 128*i, 0, 128, 128, null);
                    continue;

                } else if (i == 5) {

                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/door.png").getImage(), 128*i, 128*7, 128, 128, null);
                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/wallT.png").getImage(), 128*i, 0, 128, 128, null);
                    continue;

                } else if (i == 9) {

                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/cornerTR.png").getImage(), 128*i, 0, 128, 128, null);
                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/cornerBR.png").getImage(), 128*i, 128*7, 128, 128, null);
                    continue;

                }

                g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/wallB.png").getImage(), 128*i, 128*7, 128, 128, null);
                g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/wallT.png").getImage(), 128*i, 0, 128, 128, null);

            }

            // Vertical
            for (int i=1; i<7; i++) {
                Game.houseMap[i][0] = '2';
                Game.houseMap[i][9] = '2';

                g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/wallL.png").getImage(), 0, 128*i, 128, 128, null);
                g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/wallR.png").getImage(), 128*9, 128*i, 128, 128, null);

            }

            for (int i=0; i<map.length; i++) {

                for (int j=0; j<map[0].length; j++) {

                    // Drawing the objects
                    if ((int) map[i][j] - 48 > 1) {

                        if (map[i][j] == '8') {

                            // The TV - wall offset
                            g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + Game.houseTextures[(int) map[i][j] - 48]).getImage(), 128*j, 128*i-30, 128, 128, null);

                        } else if (i == 5 && ( j == 1 || j == 4 )) {

                            // Draws the chairs with their respective orientation
                            if (j == 1) g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + (Game.houseTextures[(int) map[i][j] - 48]).substring(0, 5) + "_left.png").getImage(), 128*j+60, 128*i+20, 128, 128, null);
                            else g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + (Game.houseTextures[(int) map[i][j] - 48]).substring(0, 5) + "_right.png").getImage(), 128*j-40, 128*i+20, 128, 128, null);

                        } else if (map[i][j] == '6') {

                            // The table resizing
                            g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + Game.houseTextures[(int) map[i][j] - 48]).getImage(), 128*j, 128*i-52, 256, 256, null);

                        } else if (map[i][j] == '5') {

                            // The wardrobe - wall offset
                            g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + Game.houseTextures[(int) map[i][j] - 48]).getImage(), 128*j+40, 128*i-30, 128, 128, null);

                        } else {
    
                            g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/House/" + Game.houseTextures[(int) map[i][j] - 48]).getImage(), 128*j, 128*i, 128, 128, null);
    
                        }
                    }
                }
            }
        }
        

        try {

            if (dad.level == 0) {

                // Drawing the grass textures and other static objects (well, house, etc.)
                for (int i=0; i<map.length; i++) {

                    for (int j=0; j<map[0].length; j++) {

                        // Interaction with map done via ASCII table values | 49 == '1'
                        if ((int) map[i][j] - 48 <= 1 && changeGrass) {

                            // Changes the grass in map value if the player is outside
                            Random rnd = new Random();
                            map[i][j] = (char) (48 + rnd.nextInt(2));

                        }

                        // Draw everything except flowers and house
                        if ((int) map[i][j] - 48 != 3 || (int) map[i][j] - 48 != 2) {

                            if (map[i][j] == '6') {

                                g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Garden/Grass1.png").getImage(), 128*j, 128*i, 128, 128, null);
                                g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Garden/Tiles.png").getImage(), 128*j, 128*i, 128, 128, null);
                                continue;

                            }

                            g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Garden/" + Game.groundTextures[(int) map[i][j] - 48]).getImage(), 128*j, 128*i, 128, 128, null);

                        }
                    }
                }
                
                // Draw the house itself
                g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Garden/" + Game.groundTextures[(int) map[0][1] - 48]).getImage(), 128, 0, 256, 256, null);
                changeGrass = false;

                // Drawing all the placed plants by a for loop to allow editing the plants
                for (int i=0; i<Game.flowers.size(); i++) {

                    Flower plant = Game.flowers.get(i);

                    // Draw the flowers
                    g.drawImage(plant.baseTexture, plant.LOCATION_X * 128, plant.LOCATION_Y * 128, 128, 128, null);
                    g.drawImage(plant.CURRENT_TEXTURE, plant.LOCATION_X * 128, plant.LOCATION_Y * 128, 128, 128, null);
                }

                // Drawing the side fence poles
                for (int i=2; i<32; i++) {

                    g.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Garden/FencePole.png").getImage(), 1877, i*30, 22, 96, null);

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
     * Drawing the alert panel
     * --------------------------------------------------------------------------------
     */


    private void drawWarning(Graphics2D g2d) {

        // Drawing the red hexagon background
        g2d.setPaint(new Color(231, 44, 22, 155));

        int middleX = (int) (this.getWidth() * Math.pow(2, -1));
        int middleY = (int) (this.getHeight() * Math.pow(2, -1));

        int[] backgroundX = {middleX - 200, middleX + 200, middleX + 250, middleX + 200, middleX - 200, middleX - 250};
        int[] backgroundY = {(int) (middleY - middleY * Math.pow(2, -1)), (int) (middleY - middleY * Math.pow(2, -1)), (int) (middleY - middleY * Math.pow(2, -1)) + 86, (int) (middleY - middleY * Math.pow(2, -1)) + 172, (int) (middleY - middleY * Math.pow(2, -1)) + 172, (int) (middleY - middleY * Math.pow(2, -1)) + 86};

        g2d.fillPolygon(backgroundX, backgroundY, backgroundX.length);

        // Drawing the red hexagon border
        g2d.setPaint(new Color(105, 7, 7, 220));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawPolygon(backgroundX, backgroundY, backgroundX.length);

        // Drawing the text inside
        g2d.setFont(Game.font.deriveFont(64f));
        g2d.drawString(Game.warningMessage, middleX - Game.warningMessage.length() * 11, (int) ((int) (middleY - middleY * Math.pow(2, -1)) + 107.5));

    }

    private void drawAlert(Graphics2D g2d) {

        int middleX = (int) (this.getWidth() * Math.pow(2, -1));
        int middleY = (int) (this.getHeight() * Math.pow(2, -1));

        // The upper white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * Math.pow(2, -1)) + 180, 400, 80);

        // The middle gray rectangle
        g2d.setPaint(new Color(210, 210, 210, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * Math.pow(2, -1)) + 260, 400, 120);
        
        // The bottom white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * Math.pow(2, -1)) + 380, 400, 80);
        

        // Alert box title
        g2d.setFont(Game.font.deriveFont(42f));
        g2d.setPaint(new Color(30, 30, 30, 240));
        g2d.drawString("Alert", middleX - 35, (int) (middleY - middleY * Math.pow(2, -1)) + 240);

        g2d.setFont(Game.font.deriveFont(24f));
        g2d.drawString(Game.alertMessage, middleX - Game.alertMessage.length() * 4, (int) (middleY - middleY * Math.pow(2, -1)) + 330);


        // The selection buttons

        // Agree button
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(middleX - 150, (int) (middleY - middleY * Math.pow(2, -1)) + 395, 50, 50);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(middleX - 147, (int) (middleY - middleY * Math.pow(2, -1)) + 398, 44, 44);
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(middleX - 144, (int) (middleY - middleY * Math.pow(2, -1)) + 401, 38, 38);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(middleX - 137, (int) (middleY - middleY * Math.pow(2, -1)) + 408, 24, 24);
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(middleX - 133, (int) (middleY - middleY * Math.pow(2, -1)) + 412, 16, 16);

        // Rejct button
        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(middleX + 100, (int) (middleY - middleY * Math.pow(2, -1)) + 395, 50, 50);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(middleX + 103, (int) (middleY - middleY * Math.pow(2, -1)) + 398, 44, 44);
        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(middleX + 106, (int) (middleY - middleY * Math.pow(2, -1)) + 401, 38, 38);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(middleX + 118, (int) (middleY - middleY * Math.pow(2, -1)) + 413, middleX + 130, (int) (middleY - middleY * Math.pow(2, -1)) + 426);
        g2d.drawLine(middleX + 130, (int) (middleY - middleY * Math.pow(2, -1)) + 413, middleX + 118, (int) (middleY - middleY * Math.pow(2, -1)) + 426);

    }

    private void drawSaveBox(Graphics2D g2d, int marginTopPx) {
        
        int middleX = (int) (this.getWidth() * Math.pow(2, -1));
        int middleY = (int) (this.getHeight() * Math.pow(2, -1));

        // The upper white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * Math.pow(2, -1)) - 100, 400, 80);

        // The middle gray rectangle
        g2d.setPaint(new Color(210, 210, 210, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * Math.pow(2, -1)) - 20, 400, 400);
        
        // The bottom white rectangle
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(middleX - 200, (int) (middleY - middleY * Math.pow(2, -1)) + 380, 400, 80);
        

        // Save box title
        g2d.setFont(Game.font.deriveFont(42f));
        g2d.setPaint(new Color(30, 30, 30, 240));
        g2d.drawString("Save", middleX - 35, (int) (middleY - middleY * Math.pow(2, -1)) - 40);

        // The save slots
        for (int i=1; i<=6; i++) {

            // Drawing the box
            g2d.setPaint(new Color(30, 30, 30, 240));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(middleX - 180, (int) (middleY - middleY * Math.pow(2, -1)) - 65 + ( 60 * i + marginTopPx ), 360, 50);
            g2d.setPaint(i == hoverSaveSlotNumber ? new Color(255, 217, 29) : i == selectedSaveSlotNumber ? new Color(48, 222, 17) : new Color(245, 245, 245, 245));
            g2d.fillRect(middleX - 179, (int) (middleY - middleY * Math.pow(2, -1)) - 64 + ( 60 * i + marginTopPx ), 358, 48);
            
            // Drawing the slot text
            g2d.setFont(Game.font.deriveFont(36f));
            g2d.setPaint(new Color(30, 30, 30, 240));
            g2d.drawString("Slot " + i, middleX - 36, (int) (middleY * Math.pow(2, -1)) - 25 + ( 60 * i + marginTopPx ));

        }


        // The selection buttons

        // Agree button
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(middleX - 150, (int) (middleY - middleY * Math.pow(2, -1)) + 395, 50, 50);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(middleX - 147, (int) (middleY - middleY * Math.pow(2, -1)) + 398, 44, 44);
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(middleX - 144, (int) (middleY - middleY * Math.pow(2, -1)) + 401, 38, 38);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(middleX - 137, (int) (middleY - middleY * Math.pow(2, -1)) + 408, 24, 24);
        g2d.setPaint(new Color(10, 126, 236, 250));
        g2d.fillOval(middleX - 133, (int) (middleY - middleY * Math.pow(2, -1)) + 412, 16, 16);

        // Rejct button
        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(middleX + 100, (int) (middleY - middleY * Math.pow(2, -1)) + 395, 50, 50);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(middleX + 103, (int) (middleY - middleY * Math.pow(2, -1)) + 398, 44, 44);
        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(middleX + 106, (int) (middleY - middleY * Math.pow(2, -1)) + 401, 38, 38);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(middleX + 118, (int) (middleY - middleY * Math.pow(2, -1)) + 413, middleX + 130, (int) (middleY - middleY * Math.pow(2, -1)) + 426);
        g2d.drawLine(middleX + 130, (int) (middleY - middleY * Math.pow(2, -1)) + 413, middleX + 118, (int) (middleY - middleY * Math.pow(2, -1)) + 426);

    }

    private void drawHelpScreen(Graphics2D g2d) {
        // Drawing the Help screen insode the house if it's started for the first time

        // The first block // White
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(1245, 50, 650, 100);

        // The second block // Gray
        g2d.setPaint(new Color(210, 210, 210, 245));
        g2d.fillRect(1245, 150, 650, 700);

        // The third block // White
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillRect(1245, 850, 650, 100);

        // The Help text in the first white block
        g2d.setPaint(new Color(30, 30, 30, 240));
        g2d.setFont(Game.font.deriveFont(48f));
        g2d.drawString("Tutorial", 1500, 120);

        // Tutorial box content
        g2d.setFont(Game.font.deriveFont(24f));

        for (int i=0; i<Game.tutorialStrings.size(); i++) {

            g2d.drawString(Game.tutorialStrings.get(i), 1275, 200 + 30*i);
        }

        g2d.drawString(Game.tutorialStringPulledData, 1275, 200);

        // Rejct button
        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(1450 + 100, 480 + 395, 50, 50);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.fillOval(1450 + 103, 480 + 398, 44, 44);
        g2d.setPaint(new Color(236, 9, 68, 250));
        g2d.fillOval(1450 + 106, 480 + 401, 38, 38);
        g2d.setPaint(new Color(245, 245, 245, 245));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(1450 + 118, 480 + 413, 1450 + 130, 480 + 426);
        g2d.drawLine(1450 + 130, 480 + 413, 1450 + 118, 480 + 426);

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
        g2d.drawImage(dad.currentTexture, (int) dad.LOCATION_X, (int) dad.LOCATION_Y, 128, 128, null);

        if (Game.isRaining()) {

            
        try {
                
            raingImg = ImageIO.read(new File("../../res/" + Game.texturePack + "/Pics/Rain.png"));
            raingImg = raingImg.getSubimage(0, (int) rainPositionY, 384, 216);
            g2d.drawImage(raingImg, dad.level == 0 ? 0 : 1200, 0, 1920, 1080, null);

        } catch (IOException e) {
            
            Game.error("Rain texture error", 3);
            e.printStackTrace();

        }

        }

        if (easeDayNight > 0) {

            Color nightColor = new Color(6, 10, 12, (int) easeDayNight);
            g2d.setPaint(nightColor);

            if (dad.selectedItem == 2) {

                Point2D center = new Point2D.Float((float) (dad.LOCATION_X + 64), (float) (dad.LOCATION_Y + 64));
                float radius = 150f;
                float[] dist = {0.0f, 1.0f};
                Color[] colors = {new Color(100, 80, 20, (int) (easeDayNight - easeDayNight * Math.pow(5, -1))), nightColor};

                RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);

                // Player has the light selected
                Area background = new Area(new Rectangle2D.Double(0, 0, Window.width, Window.height));
                Area lightCircle = new Area(new Ellipse2D.Double(dad.LOCATION_X - 86, dad.LOCATION_Y - 86, 300, 300));

                background.subtract(lightCircle);

                //g2d.fill(background);
                g2d.setPaint(gradient);
                g2d.fillArc((int) (dad.LOCATION_X - Window.width), (int) (dad.LOCATION_Y - Window.width), Window.width * 2, Window.width * 2, 0, 360);
                //g2d.fill(lightCircle);

            } else g2d.fillRect(0, 0, Window.width, Window.height);

        }

        // Drawing the help menu box
        if (dad.level == 1 && Game.firstStart) {

            drawHelpScreen(g2d);

        }

        // Drawing the death screen
        if (dad.HP == 0) {

            drawWarning(g2d);
            drawAlert(g2d);
            return;

        }

        if (Game.warning) drawWarning(g2d);
        else if (Game.alert) drawAlert(g2d);
        else if (saveMenuOpen) {

            drawSaveBox(g2d, 10);
            mouseInput.hoverEffect();

        }
    }
}
