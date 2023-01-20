package com.vojat.garden;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.vojat.inputs.KeyboardInput;

public class GamePanel extends JPanel{
    private int windowWidth, windowHeight;
    private float positionX = 10f, positionY = 10f;
    private BufferedImage playerTexture;

    public GamePanel(int windowWidth, int windowHeight) { // width == window width ; height == window height
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        // Passing information for the game window, visible by the pack method
        importImages();
        setPanelSize();
        setBackgroundColor();

        addKeyListener(new KeyboardInput(this));
    }

    private void importImages() {
        try {
            playerTexture = ImageIO.read(new FileInputStream("src/com/vojat/res/Dad_Texture.png"));
        } catch (IOException ioe) {
            System.err.println("IOException has occured");
            ioe.printStackTrace();
        }
    }

    private void setPanelSize() {
        Dimension dimension = new Dimension(windowWidth, windowHeight);
        setPreferredSize(dimension);
    }

    private void setBackgroundColor() {
        setBackground(new Color(147, 225, 41));
    }

    // Method to move stuff
    // Currently moves the rectangle
    public void moveY(float speed) {
        if (positionY <= windowHeight-128) {
            positionY += speed;
        }
    }

    public void moveX(float speed) {
        if (positionX <= windowWidth-128) {
            positionX += speed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // moveX(2f);
        // moveY(1f);
        g.drawImage(playerTexture, (int) positionX, (int) positionY, 128, 128, null);
    }
}
