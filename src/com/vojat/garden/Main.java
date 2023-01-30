package com.vojat.garden;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Main{
    public static void main(String[] args) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();     // Get the graphics device for window sizes

        int windowWidth = gd.getDisplayMode().getWidth();       // Game values
        int windowHeight = gd.getDisplayMode().getHeight() - 32;

        windowWidth = 1920;     // Testing values
        windowHeight = 1080;

        new Game(windowWidth, windowHeight);
    }
}
