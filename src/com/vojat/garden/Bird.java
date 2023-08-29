package com.vojat.garden;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vojat.Enums.ErrorList;
import com.vojat.menu.Window;

public class Bird {

    /*
     * --------------------------------------------------------------------------------
     * Bird data
     * --------------------------------------------------------------------------------
     */

    public double vectorX = .0, positionY = 0, positionX = Window.width, shitPositionX = 0, shitPositionY = 0;                       // Birds location and velocity data
    public boolean drawShit = false, splat = false, audio = false;                                                                   // Has the bird shat yet?
    public BufferedImage texture = setTexture("res/pics/pigeon.png");                                                           // Bird texture
    public static final int shitSpeed = 1;                                                                                           // The speed at which the bird shit falls to the ground
    public long timeToCleanShit = 0;

    /*
     * --------------------------------------------------------------------------------
     * Bird Constructor and methods
     * --------------------------------------------------------------------------------
     */

    public Bird(int velocity, double positionY) {

        this.vectorX = velocity;
        this.positionY = positionY;

    }

    public void shit() {

        if (drawShit) return;

        this.drawShit = true;
        this.shitPositionX = Game.intoMapX(this.positionX) * 128 + 59;
        this.shitPositionY = this.positionY + 32;
        this.timeToCleanShit = System.currentTimeMillis() + 10_000;
        this.vectorX = -2;
        System.out.println(Game.ANSI_RED + "Bird shits" + Game.ANSI_RESET);

    }

    private BufferedImage setTexture(String path) {

        try {

            return ImageIO.read(new FileInputStream(path));

        } catch (IOException ioe) {

            System.err.println(ErrorList.ERR_404.message);
            Game.error("Bird texture not found", 3);
            return null;

        }
    }
}
