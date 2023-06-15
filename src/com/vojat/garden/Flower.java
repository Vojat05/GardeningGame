package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vojat.Enums.*;

public class Flower {
    public BufferedImage CURRENT_TEXTURE;
    public String ALIVE_TEXTURE, THIRSTY_TEXTURE, DEAD_TEXTURE;
    public String TYPE;
    public String STATUS;
    public int LOCATION_X, LOCATION_Y;
    public int PLANT_NUMBER;
    public long TIME_TO_DIE, TIME_TO_DISSAPEAR;
    
    // Used when constructing new flowers
    public Flower(String path, String type, int locationX, int locationY, String status, int number) {

        for (int i=0; i<Game.flowerTypes.length; i++) {
            if (type == Game.flowerTypes[i][0]) {
                this.TIME_TO_DIE = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]);
                this.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]) + 5000;
                break;
            }
        }

        this.ALIVE_TEXTURE = "res/Pics/" + type + ".png";
        this.THIRSTY_TEXTURE = "res/Pics/Land.png";
        this.DEAD_TEXTURE = "res/Pics/MrUgly.png";
        this.TYPE = type;
        this.LOCATION_X = locationX;
        this.LOCATION_Y = locationY;
        this.STATUS = status;
        this.PLANT_NUMBER = number;
        this.CURRENT_TEXTURE = setTexture(path);
    }

    // Used when loading flowers from game save file
    public Flower(String path, String type, int locationX, int locationY, String status, int number, int dieTime) {
        this.ALIVE_TEXTURE = "res/Pics/" + type + ".png";
        this.THIRSTY_TEXTURE = "res/Pics/Land.png";
        this.DEAD_TEXTURE = "res/Pics/MrUgly.png";
        this.TIME_TO_DIE = dieTime + System.currentTimeMillis();
        this.TIME_TO_DISSAPEAR = dieTime + 5000 + System.currentTimeMillis();
        this.TYPE = type;
        this.LOCATION_X = locationX;
        this.LOCATION_Y = locationY;
        this.STATUS = status;
        this.PLANT_NUMBER = number;
        this.CURRENT_TEXTURE = setTexture(path);
    }

    public BufferedImage setTexture(String path) {
        try {
            return ImageIO.read(new FileInputStream(path));
        } catch (IOException ioe) {
            System.err.println(ErrorList.ERR_404.message);
            return null;
        }
    }
}
