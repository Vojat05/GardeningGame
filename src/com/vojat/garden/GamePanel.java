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
        setFocusable(true);

        // Passing information for the game window, visible by the pack method
        playerTexture = importImages("res/Dad_Texture_F.png");
        setPanelSize();
        setBackgroundColor();

        addKeyListener(new KeyboardInput(this));
    }

    public BufferedImage importImages(String path) {

        try {
            playerTexture = ImageIO.read(new FileInputStream(path));
            System.out.println("I Draw !!");
        } catch (IOException ioe) {
            System.err.println("IOException has occured");
            ioe.printStackTrace();
        }
        return playerTexture;
    }

    private void setPanelSize() {
        Dimension dimension = new Dimension(windowWidth, windowHeight);
        setPreferredSize(dimension);
    }

    private void setBackgroundColor() {
        setBackground(new Color(84, 194, 4));
    }

    // Moving the player
    public void moveY(float speed) {
        if (positionY > windowHeight-128) {
            positionY = windowHeight-128;
        } else if (positionY < 0) {
            positionY = 0;
        } else {
            positionY += speed;
        }
    }

    public void moveX(float speed) {
        if (positionX > windowWidth-128) {
            positionX = windowWidth-128;
        } else if (positionX < 0) {
            positionX = 0;
        } else {
            positionX += speed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(playerTexture, (int) positionX, (int) positionY, 128, 128, null);       // The Dad Image has a resolution of 32 x 32 pixels
    }
}

/*
 * 20/1/2023
 * Adding and setting up the Dad character and his texture
 * 
 * 23/1/2023
 * Making the Get image from path to BufferedImage format method usable for multiple images
 * 
 */
