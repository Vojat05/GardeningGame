package com.vojat.garden;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel{
    private int positionX = 100, positionY = 100;
    private byte frames = 0;
    private long lastCheck = 0;
    private float directionX = 0.15f, directionY = 0.15f;
    private Color color = new Color(0, 10, 0);
    private Random rand;
    private int width;
    private int height;

    public GamePanel(int width, int height) { // width == window width ; height == window height
        rand = new Random();

        this.width = width;
        this.height = height;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Method to move the rectangle
        updatePosition();
        g.setColor(color);
        g.fillRect(positionX, positionY, 200, 150);

        // The FPS counter
        // I should Repair this by some time
        frames++;
        if (System.currentTimeMillis() - lastCheck >= 1000) {
            lastCheck = System.currentTimeMillis();
            if (frames < 0) {frames *= -1;} // Makes every FPS positive number, for some reason I had negative FPS
            System.out.println("FPS: " + frames);
            frames = 0;
        }

        // Repaint the scene
        repaint();
    }

    // This makes the rectangle go Brrr
    // It also reverses it's direction on a certian axis if it gets to the end of the screen
    // Change color on colision with a border
    private void updatePosition() {
        positionX += directionX;
        if (positionX > width - 200 || positionX < 0) {
            directionX *= -1;
            color = getRandColor();
        }

        positionY += directionY;
        if (positionY > height - 150 || positionY < 0) {
            directionY *= -1;
            color = getRandColor();
        }
    }

    // Generates random Color
    private Color getRandColor() {
        short r = (short) rand.nextInt(255);
        short g = (short) rand.nextInt(255);
        short b = (short) rand.nextInt(255);

        return new Color(r, g, b);
    }
}
