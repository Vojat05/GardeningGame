package com.vojat.garden;

import javax.swing.JFrame;

public class Window {
    private JFrame window; 

    public Window(GamePanel frame, int width, int height) {
        window = new JFrame();

        window.setSize(width, height); // 2K == 2560x1440 ; Full HD == 1920x1080 ;; Resolution is stored in Main class
        window.setTitle("Dad The Gardener");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null); // Center the window on screen
        //window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        window.add(frame);
        window.setVisible(true);
    }
}
