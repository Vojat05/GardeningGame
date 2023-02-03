package com.vojat.garden;

// import java.awt.GraphicsDevice;
// import java.awt.GraphicsEnvironment;

public class Main{
    public static void main(String[] args) {
        int windowWidth;
        int windowHeight;
        // GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();     // Get the graphics device for window sizes

        // windowWidth = gd.getDisplayMode().getWidth();       // Game values
        // windowHeight = gd.getDisplayMode().getHeight() - 32;

        windowWidth = 1920;     // Testing values
        windowHeight = 1080;

        new Game(windowWidth, windowHeight);
    }
}
