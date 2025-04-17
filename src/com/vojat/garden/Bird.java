package com.vojat.garden;

import com.vojat.Data.Map;
import com.vojat.Interface.IEntity;
import com.vojat.menu.Window;

public class Bird implements IEntity {

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
        this.shitPositionX = Map.translateX(this.positionX) * Render.blockWidth + Render.blockWidth * 0.5f;
        this.shitPositionY = this.positionY + Render.blockWidth * 0.2f;
        this.timeToCleanShit = System.currentTimeMillis() + 10_000;
        this.vectorX = -2;
        Game.playSound("../../res/" + Game.texturePack + "/Audio/BirdShit.wav");

    }

    /**
     * Moves the bird
     */
    public void action() {
        // Bird shit detection
        if (drawShit && Map.translateY(shitPositionY - 30) == Map.translateY(Game.render.dad.LOCATION_Y + 64) && Map.translateX(shitPositionX) == Map.translateX(Game.render.dad.LOCATION_X + 64)) {

            if (Game.render.dad.HP == 0) Game.render.dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/GraveShit.png");
            if (Game.render.dad.HP != 0) Game.render.dad.hurt(5);
            shitPositionY = Window.height;
        }

        // Bird shit movement and bird removal after out of map
        if (shitPositionY >= positionY + 500) {
            if (!splat) audio = true;
            drawShit = false;
            splat = true;
        }

        if (positionX + 500 < 0) Game.entities.remove(this);
    }
}
