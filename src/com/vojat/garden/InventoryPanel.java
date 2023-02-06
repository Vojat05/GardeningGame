package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InventoryPanel extends JPanel{
    private JLabel item = new JLabel();

    public InventoryPanel(int windowWidth, int windowHeight, GamePanel gamePanel, Player dad) {

        {
            setBackground(new Color(40, 40, 40));
            setPreferredSize(new Dimension(windowWidth, 75));
        }
        item.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.RED));
        repaintItem(gamePanel, dad);
    }

    public void repaintItem(GamePanel gamePanel, Player dad) {      // Repaint the selected item
        ImageIcon image = new ImageIcon(gamePanel.textures[dad.selectedItem]);
        Image pic = image.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon(pic);
        item.setIcon(image);
        add(item);
    }

}
