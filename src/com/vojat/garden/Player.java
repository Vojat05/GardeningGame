package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.util.ArrayList;

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
    public byte reach = 1;                                                  // Player reach
    public byte level = 1;                                                  // Level on which the player is located  0 == outside ; 1 == inside house
    public double speed = 1;                                                // Player's movement speed
    public int HP = 100;                                                    // Player hit points number < 100 - 60 Green | 60 - 20 Orange | 20 - 0 Red >
    private char textureModifier = '0';                                     // Changes the players skin
    private boolean canMove = true;                                         // Can player can move around or not?

    /*
     * --------------------------------------------------------------------------------
     * Methods and constructors
     * --------------------------------------------------------------------------------
     */

    public Player(GamePanel gamePanel, int positionX, int positionY) {

        this.gamePanel = gamePanel;
        this.LOCATION_X = positionX;
        this.LOCATION_Y = positionY;

        inventorySetup();

    }

    // With this constructor, the player doesn't have the access to his abilities
    public Player() {

        inventorySetup();

    }

    // Fills up the starting inventory
    private void inventorySetup() {

        this.inventory.add("water9");

        for (int i=0; i<Game.flowerTypes.length; i++) this.inventory.add(Game.flowerTypes[i][0]);

    }

    public void setLimit(int limitX, int limitY) {

        windowLimitX = limitX-128;
        windowLimitY = limitY-170;

        // Sets the default player texture on startup to look forward
        currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + getTextureModifier() + ".png");

    }

    // Plants the given flower
    public void plant(Flower flower) {

        gamePanel.summonFlower(flower);

    }

    // Waters the given flower at a given position
    public void water(Flower flower) {

        gamePanel.waterFlower(flower);
        flower.STATUS = "Alive";

    }

    // Refills the water bucket
    public void waterRefill() {

        inventory.set(0, "water9");
        gamePanel.inventoryPanel.repaintItem(this);

    }

    public int hurt(int dmg) {

        if (dmg > 0 && this.HP - dmg > 0) Game.playSound("res/" + Game.texturePack + "/Audio/Damage.wav");

        if (this.HP - dmg <= 0) {

            // Kills the player
            kill();

        } else if (this.HP - dmg > 100) {

            this.HP = 100;

        } else {

            this.HP -= dmg;

        }

        gamePanel.inventoryPanel.repaint();
        return this.HP;

    }

    public void kill() {

        this.HP = 0;
        this.VECTORX = .0;
        this.VECTORY = .0;
        this.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Grave.png");

        Game.alert = true;
        Game.alertMessage = "Do you want to reload your last save?";
        Game.warning = true;
        Game.warningMessage = "You are dead";

        Game.clip.stop();
        Game.playSound("res/" + Game.texturePack + "/Audio/Death.wav");

    }

    public int setHealth(int HP) {

        if (HP == 0) kill();
        gamePanel.inventoryPanel.repaint();
        return this.HP = HP;

    }

    public char getTextureModifier() {

        return this.textureModifier;

    }

    public void setTextureModifier(char newTextureModifier) {

        this.textureModifier = newTextureModifier;
        this.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + getTextureModifier() + ".png");

    }

    public boolean canMove() {

        return this.canMove;

    }

    public boolean setMove(boolean value) {

        return this.canMove = value;
        
    }
}
