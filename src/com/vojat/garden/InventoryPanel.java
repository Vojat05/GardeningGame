package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class InventoryPanel extends JPanel{
    public InventoryPanel(int windowWidth, int windowHeight) {
        {
            setBackground(new Color(40, 40, 40));
            setPreferredSize(new Dimension(windowWidth, 75));
        }
    }
}
