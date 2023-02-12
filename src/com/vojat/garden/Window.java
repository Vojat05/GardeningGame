package com.vojat.garden;

import javax.swing.JFrame;

public class Window extends JFrame {
    private MainPanel mainPanel;
    private Player dad;

    public Window(MainPanel mainPanel, Player dad) {
        this.mainPanel = mainPanel;
        this.dad = dad;

        setTitle("Dad The Gardener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        add(mainPanel);
        pack();                                                                         // Create a window that fits the all stuff the needs to fit
        validate();
        setVisible(true);
    }

    public void validate() {
        super.validate();

        mainPanel.setPanelSize(getSize().width, getSize().height);
        dad.setLimit(getSize().width, getSize().height);

        if (dad.LOCATION_X > Player.windowLimitX-120) {
            dad.LOCATION_X = Player.windowLimitX-130;
        }

        if (dad.LOCATION_Y > Player.windowLimitY-120) {
            dad.LOCATION_Y = Player.windowLimitY-150;
        }
    }
}
