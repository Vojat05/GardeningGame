package com.vojat.garden;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import com.vojat.inputs.KeyboardInput;

public class GamePanel extends JPanel {
    private float positionX = 0f, positionY = 0f;
    private float directionX = 0.15f, directionY = 0.15f;
    private Color color = new Color(0, 0, 0);
    private Random rand;
    private long lastCheck = 0;
    private int fps = 0;
    private int windowWidth;
    private int windowHeight;

    public GamePanel(int windowWidth, int windowHeight) { // width == window width ; height == window height
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        rand = new Random();

        addKeyListener(new KeyboardInput());

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updatePosition();
        g.setColor(color);
        g.fillRect((int) positionX, (int) positionY, 200, 150);

        
        // The FPS counter
        fps++;
        if (System.currentTimeMillis() - lastCheck >= 1000) {
            lastCheck = System.currentTimeMillis();
            System.out.println("FPS: " + fps);
            fps = 0;
        }

        super.repaint();
    }

    /*
     * This makes the rectangle go Brrr
     * It also reverses it's direction on a certian axis if it gets to the end of the screen
     * Change color on colision with a border
     */
    private void updatePosition() {
        positionX += directionX;
        if (positionX > windowWidth - 200 || positionX < 0) {
            directionX *= -1;
            color = getRandColor();
        }

        positionY += directionY;
        if (positionY > windowHeight - 150 || positionY < 0) {
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
