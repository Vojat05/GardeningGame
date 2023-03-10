package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vojat.Enums.*;

public class Flower{
    public BufferedImage CURRENT_TEXTURE;
    public String ALIVE_TEXTURE;
    public String THIRSTY_TEXTURE;
    public String DEAD_TEXTURE;
    public String TYPE;
    public String STATUS;
    public short LOCATION_X;
    public short LOCATION_Y;
    public int PLANT_NUMBER;
    public long TIME_TO_DIE;
    public long TIME_TO_DISSAPEAR;
    public int IN_MAP_X;
    public int IN_MAP_Y;
    public int FLOWER_TEXTURE_NUMBER;
    
    public Flower(String path, String type, short locationX, short locationY, String status, int number, int controlX, int controlY, int flowerNum) {
        switch (type) {
            case "tulip":
                this.TIME_TO_DIE = System.currentTimeMillis() + Values.TODIE_REDTULIP.value;
                this.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Values.TODISSAPEAR_REDTULIP.value;

                this.ALIVE_TEXTURE = "res/Pics/Red_Tulip.png";
                this.THIRSTY_TEXTURE = "res/Pics/Land.png";
                this.DEAD_TEXTURE = "res/Pics/MrUgly.png";
                break;
            
            case "rose":
                this.TIME_TO_DIE = System.currentTimeMillis() + Values.TODIE_ROSE.value;
                this.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Values.TODISSAPEAR_ROSE.value;

                this.ALIVE_TEXTURE = "res/Pics/Blue_Rose.png";
                this.THIRSTY_TEXTURE = "res/Pics/Land.png";
                this.DEAD_TEXTURE = "res/Pics/MrUgly.png";
        }

        this.TYPE = type;
        this.LOCATION_X = locationX;
        this.LOCATION_Y = locationY;
        this.STATUS = status;
        this.PLANT_NUMBER = number;
        this.CURRENT_TEXTURE = setTexture(path);
        this.IN_MAP_X = controlX;
        this.IN_MAP_Y = controlY;
        this.FLOWER_TEXTURE_NUMBER = flowerNum;
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
