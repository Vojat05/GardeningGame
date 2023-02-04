package com.vojat.garden;

public class Main{
    public static void main(String[] args) {
        int windowWidth;
        int windowHeight;

        windowWidth = 1920;     // This game is currently designed to be run in FullHD window
        windowHeight = 1080;

        new Game(windowWidth, windowHeight);
    }
}
