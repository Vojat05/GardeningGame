package com.vojat.garden;

import javax.swing.JFrame;

public class Window extends JFrame {
    private GamePanel gamePanel;
    private Player dad;

    public Window(GamePanel gamePanel, Player dad) {
        this.gamePanel = gamePanel;
        this.dad = dad;

        setTitle("Dad The Gardener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        add(gamePanel);
        pack();     // Create a window that fits the JPanel passed in here
        validate();
        setVisible(true);
    }

    public void validate() {
        super.validate();

        gamePanel.setPanelSize(getSize().width, getSize().height);
        dad.setLimit(getSize().width, getSize().height);

        if (dad.LOCATION_X > dad.windowLimitX-120) {
            dad.LOCATION_X = dad.windowLimitX-130;
        }

        if (dad.LOCATION_Y > dad.windowLimitY-120) {
            dad.LOCATION_Y = dad.windowLimitY-150;
        }
    }
}
