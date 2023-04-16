package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vojat.Enums.ErrorList;

public class Player {
    public GamePanel gamePanel;
    public static int windowLimitX, windowLimitY;
    public double LOCATION_X = 0, LOCATION_Y = 0;
    public double VECTORX = .0, VECTORY = .0;
    public BufferedImage currentTexture;
    public String[] inventory = {"water", "tulip", "rose"};                                                 // This is a set of items the player has (inventory)
    public byte selectedItem = 0;
    public byte reach = 1;
    public byte level = 0;

    public Player(GamePanel gamePanel, int positionX, int positionY) {
        this.gamePanel = gamePanel;
        this.LOCATION_X = positionX;
        this.LOCATION_Y = positionY;
    }

    public Player() {;}                                                                                     // With this constructor, the player doesn't have the access to his abilities

    public void setLimit(int limitX, int limitY) {
        windowLimitX = limitX-128;
        windowLimitY = limitY-170;

        setTexture("res/Pics/Dad_Texture_F.png");                                                          // Sets the default player texture on startup to look forward
    }

    public void setTexture(String path) {
        try {
            currentTexture = ImageIO.read(new FileInputStream(path));
        } catch (IOException ioe) {
            System.err.println(ErrorList.ERR_404.message);
            currentTexture = null;
        }
    }

    public void plant(Flower flower) {                                                                      // Plants the passed flower
        gamePanel.summonFlower(flower);
    }

    public void water(Flower flower, int positionX, int positionY) {
        gamePanel.waterFlower(flower, positionX, positionY);
    }

    public void waterRefill() {
        Game.textures[0] = "res/Pics/WaterDrop9.png";
        gamePanel.inventoryPanel.repaintItem(this);
    }
}
