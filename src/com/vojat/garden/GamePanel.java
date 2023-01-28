package com.vojat.garden;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JPanel;

import com.vojat.inputs.KeyboardInput;

public class GamePanel extends JPanel{
    private int windowWidth, windowHeight;
    Player dad = new Player();

    public GamePanel(int windowWidth, int windowHeight) { // width == window width ; height == window height
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        dad.setLimit(windowWidth, windowHeight);
        setFocusable(true);

        // Passing information for the game window, visible by the pack method
        setPanelSize();
        setBackgroundColor();

        addKeyListener(new KeyboardInput(dad));
    }

    private void setPanelSize() {
        Dimension dimension = new Dimension(windowWidth, windowHeight);
        setPreferredSize(dimension);
    }

    private void setBackgroundColor() {
        setBackground(new Color(84, 194, 4));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(dad.currentTexture, dad.LOCATION_X, dad.LOCATION_Y, 128, 128, null);       // The Dad Image has a resolution of 32 x 32 pixels
    }
}
