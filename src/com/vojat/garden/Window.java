package com.vojat.garden;

import javax.swing.JFrame;

public class Window extends JFrame {

    public Window(GamePanel gamePanel, int width, int height) {

        setSize(width, height); // 2K == 2560x1440 ; Full HD == 1920x1080 ;; Resolution is stored in Main class
        setTitle("Dad The Gardener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen if it isn't maximized
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        add(gamePanel);
        setVisible(true);
    }
}
