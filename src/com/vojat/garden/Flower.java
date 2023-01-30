package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;

import com.vojat.Errors.ErrorList;

import javax.imageio.ImageIO;

public class Flower {
    private String PICTURE_PATH;
    public BufferedImage CURRENT_TEXTURE;
    public String FLOWER_TYPE;
    public String STATUS;
    public int LOCATION_X;
    public int LOCATION_Y;
    
    public Flower(String path, String type, int locationX, int locationY, String status) {
        this.PICTURE_PATH = path;
        this.FLOWER_TYPE = type;
        this.LOCATION_X = locationX;
        this.LOCATION_Y = locationY;
        this.STATUS = status;
        setTexture(path);
    }

    public Flower() {       // The base example
        this.FLOWER_TYPE = "fialka";
        this.STATUS = "Alive";
        this.PICTURE_PATH = "res/Red_Tulip.png";
        this.LOCATION_X = 256;
        this.LOCATION_Y = 64;
        setTexture(PICTURE_PATH);
    }

    public void setTexture(String path) {
        try {
            CURRENT_TEXTURE = ImageIO.read(new FileInputStream(path));
        } catch (IOException ioe) {
            System.err.println(ErrorList.ERR_404.message);
            ioe.printStackTrace();
            CURRENT_TEXTURE = null;
        }
    }
}
