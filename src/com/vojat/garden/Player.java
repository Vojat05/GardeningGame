package com.vojat.garden;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
    private int WINDOW_LIMIT_X, WINDOW_LIMIT_Y;
    public int LOCATION_X = 0, LOCATION_Y = 0;
    public BufferedImage currentTexture;

    protected void setLimit(int limitX, int limitY) {
        WINDOW_LIMIT_X = limitX;
        WINDOW_LIMIT_Y = limitY;

        setTexture("res/Dad_Texture_F.png");      // Sets the default player texture on startup to look forward
    }

    public void setTexture(String path) {
        try {
            currentTexture = ImageIO.read(new FileInputStream(path));
        } catch (IOException ioe) {
            System.err.println("IOException has occured");
            ioe.printStackTrace();
            currentTexture = null;
        }
    }

    public void moveUP(int speed) {     // Moves the player on the Y line
        if (LOCATION_Y < 0) {
            LOCATION_Y += 1;
        } else if (LOCATION_Y > WINDOW_LIMIT_Y-128) {
            LOCATION_Y -= 1;
        } else {
            LOCATION_Y += speed;
        }
    }

    public void moveSIDE(int speed) {       // Moves the player on the X line
        if (LOCATION_X < 0) {
            LOCATION_X += 1;
        } else if (LOCATION_X > WINDOW_LIMIT_X-128) {
            LOCATION_X -= 1;
        } else {
            LOCATION_X += speed;
        }
    }

    public void plant() {;}
}
