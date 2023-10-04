package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;


public class MainPanel extends JPanel {
    public MainPanel(GamePanel gamePanel, InventoryPanel inventoryPanel) {
        {
            setBackground(new Color(40, 40, 40));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(gamePanel);
            add(inventoryPanel);
        }
    }

    public void setPanelSize(int width, int height) {
        
        setPreferredSize(new Dimension(width, height));
        
    }
}
