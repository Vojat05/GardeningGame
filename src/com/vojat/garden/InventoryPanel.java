package com.vojat.garden;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class InventoryPanel extends JPanel{
    private JLabel item = new JLabel();

    public InventoryPanel(int windowWidth, int windowHeight, GamePanel gamePanel, Player dad) {

        {
            setBackground(new Color(40, 40, 40));
            setBounds(0, windowHeight-75, windowWidth, 75);
        }
        item.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.RED));
        repaintItem(dad);
    }

    public void repaintItem(Player dad) {                                                                  // Repaint the selected item
        item.setIcon(new ImageIcon(new ImageIcon(Game.textures[dad.selectedItem]).getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
        add(item);
    }

    public static void repaintItem(int index, JLabel label) {
        label.setIcon(new ImageIcon(new ImageIcon(Game.textures[index]).getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
        label.setBorder(new LineBorder(Color.BLACK, 2));
    }

    public static void repaintItem(JLabel label, String fileAdress) {
        label.setIcon(new ImageIcon(new ImageIcon(fileAdress).getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)));
    }

    public static Icon createIcon(String fileAdress, int sizeX, int sizeY) {
        Image pic = new ImageIcon(fileAdress).getImage().getScaledInstance(sizeX, sizeY, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(pic);
    }
}
