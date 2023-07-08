package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.vojat.Enums.ErrorList;

public class Player {

    /*
     * --------------------------------------------------------------------------------
     * Player data
     * --------------------------------------------------------------------------------
     */

    public GamePanel gamePanel;                                             // Panel on which the player is
    public static int windowLimitX, windowLimitY;                           // Coordinate limits for the player to move in
    public double LOCATION_X = .0, LOCATION_Y = .0;                         // Position of the player
    public double VECTORX = .0, VECTORY = .0;                               // Player's movement vectors
    public BufferedImage currentTexture;                                    // Player's current texture {front, back, left, right}
    public ArrayList<String> inventory = new ArrayList<String>();           // Player inventory with all object he holds
    public byte selectedItem = 0;                                           // Index of a specific item from the inventory
    public byte reach = 1;                                                  // Player reach
    public byte level = 0;                                                  // Level on which the player is located  0 == outside ; 1 == inside house
    public int speed = 2;                                                   // Player's movement speed

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

    private void inventorySetup() {
        this.inventory.add("water9");
        this.inventory.add("tulip");
        this.inventory.add("rose");
    }

    public void setLimit(int limitX, int limitY) {
        windowLimitX = limitX-128;
        windowLimitY = limitY-170;

        // Sets the default player texture on startup to look forward
        setTexture("res/Pics/Dad_Texture_F.png");
    }

    public void setTexture(String path) {
        try {
            currentTexture = ImageIO.read(new FileInputStream(path));
        } catch (IOException ioe) {
            System.err.println(ErrorList.ERR_404.message);
            currentTexture = null;
        }
    }

    // Plants the given flower
    public void plant(Flower flower) {
        gamePanel.summonFlower(flower);
    }

    // Waters the given flower at a given position
    public void water(Flower flower) {
        gamePanel.waterFlower(flower);
    }

    // Refills the water bucket
    public void waterRefill() {
        inventory.set(0, "water9");
        gamePanel.inventoryPanel.repaintItem(this);
    }
}
