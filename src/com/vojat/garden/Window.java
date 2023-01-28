package com.vojat.garden;

import javax.swing.JFrame;

public class Window extends JFrame {

    public Window(GamePanel gamePanel) {

        setTitle("Dad The Gardener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        add(gamePanel);
        pack();     // Create a window that fits the JPanel passed in this constructor
        setVisible(true);
    }
}
