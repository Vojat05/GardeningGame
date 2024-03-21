package com.vojat.garden;

import com.vojat.Data.Map;
import com.vojat.menu.Window;

public class Bird {

    /*
     * --------------------------------------------------------------------------------
     * Bird data
     * --------------------------------------------------------------------------------
     */

    public float vectorX = 0f, positionY = 0f, positionX = Window.width, shitPositionX = 0f, shitPositionY = 0f;                     // Birds location and velocity data
    public boolean drawShit = false, splat = false, audio = false;                                                                   // Has the bird shat yet?
    public long timeToCleanShit = 0;                                                                                                 // Time at which the shit should be cleaned

    /*
     * --------------------------------------------------------------------------------
     * Bird Constructor and methods
     * --------------------------------------------------------------------------------
     */

    public Bird(int velocity, float positionY) {

        this.vectorX = velocity;
        this.positionY = positionY;

    }

    /**
     * Makes the bird shit in it's current positon
     */
    public void shit() {

        // Returns if the bird already shat
        if (drawShit) return;

        this.drawShit = true;
        this.shitPositionX = Map.translateX(this.positionX) * GamePanel.blockWidth + GamePanel.blockWidth * 0.5f;
        this.shitPositionY = this.positionY + GamePanel.blockWidth * 0.2f;
        this.timeToCleanShit = System.currentTimeMillis() + 10_000;
        this.vectorX = -2;
        Game.playSound("../../res/" + Game.texturePack + "/Audio/BirdShit.wav");

    }
}
