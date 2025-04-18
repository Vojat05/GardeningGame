package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.vojat.Rendering.Render;


public class MainPanel extends JPanel {
    public MainPanel(Render render, InventoryPanel inventoryPanel) {
        {
            setBackground(new Color(40, 40, 40));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(render);
            add(inventoryPanel);
        }
    }

    public void setPanelSize(int width, int height) {
        
        setPreferredSize(new Dimension(width, height));
        
    }
}
