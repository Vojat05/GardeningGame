package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vojat.Enums.ErrorList;

public class Player {
    private GamePanel gamePanel;
    public static int windowLimitX, windowLimitY;
    public int LOCATION_X = 0, LOCATION_Y = 0;
    public BufferedImage currentTexture;
    public String[] inventory = {"water", "tulip", "rose"};                                                 // This is a set of items the player has (inventory)
    public byte selectedItem = 0;

    public Player(GamePanel gamePanel, int positionX, int positionY) {
        this.gamePanel = gamePanel;
        this.LOCATION_X = positionX;
        this.LOCATION_Y = positionY;
    }

    public Player() {;}                                                                                     // With this constructor, the player doesn't have the access to his abilities

    public void setLimit(int limitX, int limitY) {
        windowLimitX = limitX;
        windowLimitY = limitY;

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

    public void moveUP(int speed) {                                                                         // Moves the player on the Y line
        if (LOCATION_Y < 0) {
            LOCATION_Y += 1;
        } else if (LOCATION_Y > windowLimitY-230) {
            LOCATION_Y -= 1;
        } else {
            LOCATION_Y += speed;
        }
    }

    public void moveSIDE(int speed) {                                                                       // Moves the player on the X line
        if (LOCATION_X < 0) {
            LOCATION_X += 1;
        } else if (LOCATION_X > windowLimitX-128) {
            LOCATION_X -= 1;
        } else {
            LOCATION_X += speed;
        }
    }

    public void plant(Flower flower) {                                                                      // Plants the passed flower
        gamePanel.summonFlower(flower);
    }

    public void water(Flower flower, int positionX, int positionY) {
        gamePanel.waterFlower(flower, positionX, positionY);
    }
}
