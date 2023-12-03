package com.vojat.garden;

import java.awt.image.BufferedImage;
import com.vojat.menu.Window;

public class Bird {

    /*
     * --------------------------------------------------------------------------------
     * Bird data
     * --------------------------------------------------------------------------------
     */

    public static final int shitSpeed = 2;                                                                                           // The speed at which the bird shit falls to the ground
    public double vectorX = .0, positionY = 0, positionX = Window.width, shitPositionX = 0, shitPositionY = 0;                       // Birds location and velocity data
    public boolean drawShit = false, splat = false, audio = false;                                                                   // Has the bird shat yet?
    public BufferedImage texture = Game.setTexture("../../res/" + Game.texturePack + "/pics/Pigeon1.png");                           // Bird texture
    public long timeToCleanShit = 0;                                                                                                 // Time at which the shit should be cleaned

    /*
     * --------------------------------------------------------------------------------
     * Bird Constructor and methods
     * --------------------------------------------------------------------------------
     */

    public Bird(int velocity, double positionY) {

        this.vectorX = velocity;
        this.positionY = positionY;

    }

    /**
     * Makes the bird shit in it's current positon
     */
    public void shit() {

        if (drawShit) return;

        this.drawShit = true;
        this.shitPositionX = Game.intoMapX(this.positionX) * 128 + 59;
        this.shitPositionY = this.positionY + 32;
        this.timeToCleanShit = System.currentTimeMillis() + 10_000;
        this.vectorX = -2;
        Game.playSound("../../res/" + Game.texturePack + "/Audio/BirdShit.wav");

    }
}
