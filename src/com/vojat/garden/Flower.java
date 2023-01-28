package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Flower {
    private String PICTURE_PATH;
    public BufferedImage CURRENT_TEXTURE;
    public String FLOWER_TYPE;
    public String STATUS;
    public int LOCATION_X;
    public int LOCATION_Y;
    
    public Flower(String path, String type) {
        this.PICTURE_PATH = path;
        this.FLOWER_TYPE = type;
        setTexture(path);
    }

    public Flower() {       // The base example
        this.FLOWER_TYPE = "fialka";
        this.STATUS = "alive";
        this.PICTURE_PATH = "res/Red_Tulip.png";
        this.LOCATION_X = 256;
        this.LOCATION_Y = 64;
        setTexture(PICTURE_PATH);
    }

    public void setTexture(String path) {
        try {
            CURRENT_TEXTURE = ImageIO.read(new FileInputStream(path));
        } catch (IOException ioe) {
            System.err.println("IOException has occured");
            ioe.printStackTrace();
            CURRENT_TEXTURE = null;
        }
    }
}
