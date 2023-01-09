package com.vojat.garden;

import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
    private int windowWidth;
    private int windowHeight;

    public GamePanel(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(windowWidth/2-100, windowHeight/2-50, 200, 100);
    }
}
