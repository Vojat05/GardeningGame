package com.vojat.garden;

import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vojat.Enums.*;

public class Flower {

    /*
     * --------------------------------------------------------------------------------
     * Flower data
     * --------------------------------------------------------------------------------
     */

    public BufferedImage CURRENT_TEXTURE;                                           // Current flower texture
    public final String ALIVE_TEXTURE, THIRSTY_TEXTURE, DEAD_TEXTURE;               // All 3 texture versions of a flower
    public BufferedImage baseTexture;                                               // Texture to be used underneath the flower (allows having flowers with transparent backgrounds)
    public String TYPE;                                                             // Flower type
    public String STATUS;                                                           // Determines wheather the flower is alive or dead
    public int LOCATION_X, LOCATION_Y;                                              // Exact flower location
    public int PLANT_NUMBER;                                                        // The index of the plant
    public long TIME_TO_DIE, TIME_TO_DISSAPEAR;                                     // Time to die and dissapear
    
    /*
     * --------------------------------------------------------------------------------
     * Constructor for building new flowers
     * --------------------------------------------------------------------------------
     */

    public Flower(String type, int locationX, int locationY, String status, int number) {
        
        this.baseTexture = setTexture("./res/" + Game.texturePack + "/Pics/Garden/Grass" + Game.random.nextInt(1, 3) + ".png");
        this.ALIVE_TEXTURE = "./res/" + Game.texturePack + "/Pics/Flowers/" + type + ".png";
        this.THIRSTY_TEXTURE = "./res/" + Game.texturePack + "/Pics/Flowers/" + type + "_thirsty.png";
        this.DEAD_TEXTURE = "./res/" + Game.texturePack + "/Pics/Flowers/" + type + "_dead.png";
        this.TYPE = type;
        this.LOCATION_X = locationX;
        this.LOCATION_Y = locationY;
        this.STATUS = status;
        this.PLANT_NUMBER = number;

        // Sets the die & disappear times based on the flower type
        resetTime();

        
    }

    /*
     * --------------------------------------------------------------------------------
     * Constructor for loading flowers from a save file
     * --------------------------------------------------------------------------------
     */

    public Flower(String type, int locationX, int locationY, String status, int number, int dieTime) {

        this.baseTexture = setTexture("./res/" + Game.texturePack + "/Pics/Garden/Grass" + Game.random.nextInt(1, 3) + ".png");
        this.ALIVE_TEXTURE = "./res/" + Game.texturePack + "/Pics/Flowers/" + type + ".png";
        this.THIRSTY_TEXTURE = "./res/" + Game.texturePack + "/Pics/Flowers/" + type + "_thirsty.png";
        this.DEAD_TEXTURE = "./res/" + Game.texturePack + "/Pics/Flowers/" + type + "_dead.png";
        this.TIME_TO_DIE = dieTime + System.currentTimeMillis();
        this.TIME_TO_DISSAPEAR = dieTime + 20000 + System.currentTimeMillis();
        this.TYPE = type;
        this.LOCATION_X = locationX;
        this.LOCATION_Y = locationY;
        this.STATUS = status;
        this.PLANT_NUMBER = number;
        this.CURRENT_TEXTURE = setTexture(dieTime > Game.flowerChange ? this.ALIVE_TEXTURE : dieTime > this.TIME_TO_DISSAPEAR ? this.THIRSTY_TEXTURE : this.DEAD_TEXTURE);

    }

    /*
     * --------------------------------------------------------------------------------
     * Flower modification methods
     * --------------------------------------------------------------------------------
     */

    // Sets the flower texture
    public BufferedImage setTexture(String path) {

        try {

            return ImageIO.read(new FileInputStream(path));

        } catch (IOException ioe) {

            System.err.println(ErrorList.ERR_404.message);
            Game.error("Texture not found", 3);
            return null;

        }
    }

    // Resets the flower death & dissapear times
    public void resetTime() {

        for (int i=0; i<Game.flowerTypes.length; i++) {
            if (this.TYPE == Game.flowerTypes[i][0]) {
                this.TIME_TO_DIE = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]);
                this.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]) + 20000;
                this.STATUS = "Alive";
                this.CURRENT_TEXTURE = setTexture(ALIVE_TEXTURE);
                break;
            }
        }
    }
}
