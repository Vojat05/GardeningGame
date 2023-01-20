package com.vojat.garden;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.vojat.inputs.KeyboardInput;

public class GamePanel extends JPanel{
    private int windowWidth, windowHeight;
    private float positionX = 10f, positionY = 10f;

    public GamePanel(int windowWidth, int windowHeight) { // width == window width ; height == window height
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        addKeyListener(new KeyboardInput(this));
    }

    // Method to move stuff
    // Currently moves the rectangle
    public void moveY(float speed) {
        if (positionY <= windowHeight - 200) {
            positionY += speed;
        }
    }

    public void moveX(float speed) {
        if (positionX <= windowWidth - 225) {
            positionX += speed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        moveX(2f);
        moveY(1f);
        g.setColor(new Color(0, 0, 0));
        g.fillRect((int) positionX, (int) positionY, 200, 150);
    }
}
