package com.vojat.garden;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

import com.vojat.Data.JSONEditor;
import com.vojat.menu.Window;

public class Player {

    /*
     * --------------------------------------------------------------------------------
     * Player data
     * --------------------------------------------------------------------------------
     */

    public static int windowLimitX, windowLimitY;                           // Coordinate limits for the player to move in
    public GamePanel gamePanel;                                             // Panel on which the player is
    public double LOCATION_X = .0, LOCATION_Y = .0;                         // Position of the player
    public double VECTORX = .0, VECTORY = .0;                               // Player's movement vectors
    public BufferedImage currentTexture;                                    // Player's current texture {front, back, left, right}
    public ArrayList<String> inventory = new ArrayList<String>();           // Player inventory with all object he holds
    public byte selectedItem = 0;                                           // Index of a specific item from the inventory
    public byte reachLevel = 0x11;                                          // 4 bits == reach & 4 bits == level
    public float speed = 1;                                                 // Player's movement speed
    public float dSpeed = 1;                                                // Player's default movement speed
    public byte HP = 100;                                                   // Player hit points number < 100 - 60 Green | 60 - 20 Orange | 20 - 0 Red >
    public byte stamina = 100;                                              // Player stamina for the player to run on the tiles
    public boolean outOfStamina = false;                                    // Does the player have a 0 stamina penalty ( tells the bar to change color as it regenerates )
    public boolean isSitting = false;                                       // Is the player character sitting?
    private boolean canMove = true;                                         // Can player can move around or not?
    private char textureModifier = '0';                                     // Changes the players skin

    /*
     * --------------------------------------------------------------------------------
     * Methods and constructors
     * --------------------------------------------------------------------------------
     */

    public Player(GamePanel gamepanel, int positionX, int positionY) {

        this.gamePanel = gamepanel;
        this.LOCATION_X = positionX;
        this.LOCATION_Y = positionY;
        
        try {

            JSONEditor jEditor = new JSONEditor("../../res/Config.json");
            this.reachLevel = (byte) ((Byte.parseByte(jEditor.readData("Player-Reach")) << 4) + 1);
            this.dSpeed = Window.width * 0.00052f * Float.parseFloat(jEditor.readData("Player-Default-Speed"));

        } catch (IOException e) {

            e.printStackTrace();
            
        }
        inventorySetup();
    }

    // With this constructor, the player doesn't have the access to his abilities
    public Player() { inventorySetup(); }

    // Fills up the starting inventory
    private void inventorySetup() {

        this.inventory.add("water9");
        this.inventory.add("Tiles");
        this.inventory.add("Light");
        this.inventory.add("Magnifying_glass");

        for (int i=0; i<Game.flowerTypes.length; i++) this.inventory.add(Game.flowerTypes[i][0]);

    }

    public void setLimit(int limitX, int limitY) {

        windowLimitX = limitX-128;
        windowLimitY = limitY-170;

        // Sets the default player texture on startup to look forward
        if (!(new File("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + getTextureModifier() + ".png").exists())) currentTexture = Game.setTexture("../com/vojat/Data/Missing.png");
        else currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + getTextureModifier() + ".png");

    }

    // Plants the given flower
    public void plant(Flower flower) { gamePanel.summonFlower(flower); }

    // Waters the given flower at a given position
    public void water(Flower flower) { gamePanel.waterFlower(flower); }

    // Sets the player level
    public void setLevel(byte level) { this.reachLevel = (byte) ((this.reachLevel & 0xf0) + level); }

    // Refills the water bucket
    public void waterRefill() {

        inventory.set(0, "water9");
        if (gamePanel.hasFocus()) gamePanel.inventoryPanel.repaint();

    }

    // Deals damage to the player // negative dmg heals the player
    public int hurt(int dmg) {

        if (dmg > 0 && this.HP - dmg > 0) Game.playSound("../../res/" + Game.texturePack + "/Audio/Damage.wav");

        if (this.HP - dmg <= 0) {

            // Kills the player
            kill();

        } else if (this.HP - dmg > 100) this.HP = 100;
        else this.HP -= dmg;

        if (gamePanel.hasFocus()) gamePanel.inventoryPanel.repaint();
        return this.HP;

    }

    // Instantly kills the player
    public void kill() {

        this.HP = 0;
        this.VECTORX = .0;
        this.VECTORY = .0;
        this.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Grave.png");

        Game.alert("Do you want to reload your last save?");
        Game.warn("You are dead");

        Game.gameMusic.stop();
        Game.playSound("../../res/" + Game.texturePack + "/Audio/Death.wav");
        if (gamePanel.hasFocus()) gamePanel.inventoryPanel.repaint();

    }

    // Sets the player health to a certian value
    public int setHealth(byte HP) {

        if (HP == 0) kill();
        if (gamePanel.hasFocus()) gamePanel.inventoryPanel.repaint();
        return this.HP = HP;

    }

    // Removes the player stamina tiring him // The same as hurt, just for stamina
    public int tire(byte value) {

        if (this.stamina - value <= 0) this.stamina = 0;
        else if (this.stamina - value >= 100) this.stamina = 100;
        else this.stamina = (byte) (this.stamina - value);
        if (gamePanel.hasFocus()) gamePanel.inventoryPanel.repaint();

        return this.stamina;
    }

    // Sets the player stamina to a certian value
    public int setStamina(byte value) {

        if (value < 0) this.stamina = 0;
        if (value > 100) this.stamina = 100;
        else this.stamina = value;

        return this.stamina;
    }

    // Returns the currently used texture modifier for selecting player texture
    public char getTextureModifier() { return this.textureModifier; }

    /**
     * Sets the player texture modifier
     * @param newTextureModifier char new texture modifier
     */
    public void setTextureModifier(char newTextureModifier) {

        this.textureModifier = newTextureModifier;
        this.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + getTextureModifier() + ".png");

    }

    /**
     * Sets the player texture to the texture at the specified path.
     * @param path file path from the <code>../../res/{TexturePack}/Pics/</code> point.
     */
    public void setTexture(String path) { this.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/" + path); }

    /**
     * Determines wheather the player can move or not
     * @return The player move value
     */
    public boolean canMove() { return this.canMove; }

    // Sets the player move value
    public boolean setMove(boolean value) { return this.canMove = value; }
}
