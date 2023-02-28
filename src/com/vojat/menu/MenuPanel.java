package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class MenuPanel extends JPanel{
    
    public MenuPanel(int windowWidth, int windowHeight, JPanel buttonPanel) {

        setFocusable(true);
        setOpaque(true);
        setBackground(Color.DARK_GRAY);                 // This is the entire main menu background
        setPreferredSize(new Dimension(windowWidth, windowHeight));

        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(windowWidth-250, 100));
        spacer.setBackground(null);

        add(buttonPanel);
        add(spacer);
    }
}
