package com.vojat.garden;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MainPanel extends JPanel{
    public MainPanel(GamePanel gamePanel, InventoryPanel inventoryPanel) {
        {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(gamePanel);
            add(inventoryPanel);
        }
    }

    protected void setPanelSize(int width, int height) {
        setPreferredSize(new Dimension(width, height));
    }
}
