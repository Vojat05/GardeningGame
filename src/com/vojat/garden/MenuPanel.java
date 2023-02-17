package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class MenuPanel extends JPanel{
    
    public MenuPanel(int windowWidth, int windowHeight, JPanel buttonPanel) {

        setFocusable(true);
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(windowWidth, windowHeight));

        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(windowWidth-250, 100));
        spacer.setBackground(null);

        add(buttonPanel);
        add(spacer);
    }
}
