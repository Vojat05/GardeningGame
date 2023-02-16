package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class MenuPanel extends JPanel{
    
    public MenuPanel(int windowWidth, int windowHeight, JPanel buttonPanel) {

        setFocusable(true);
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(windowWidth, windowHeight));

        add(buttonPanel);
    }
}
