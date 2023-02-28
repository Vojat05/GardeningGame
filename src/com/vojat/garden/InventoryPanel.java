package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class InventoryPanel extends JPanel{
    private JLabel item = new JLabel();

    public InventoryPanel(int windowWidth, int windowHeight, GamePanel gamePanel, Player dad) {

        {
            setBackground(new Color(40, 40, 40));
            setPreferredSize(new Dimension(windowWidth, 75));
        }
        item.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.RED));
        repaintItem(dad);
    }

    public void repaintItem(Player dad) {                                                                  // Repaint the selected item
        ImageIcon image = new ImageIcon(GamePanel.textures[dad.selectedItem]);
        Image pic = image.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon(pic);
        item.setIcon(image);
        add(item);
    }

    public static void repaintItem(int index, JLabel label) {
        ImageIcon image = new ImageIcon(GamePanel.textures[index]);
        Image pic = image.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon(pic);
        label.setIcon(image);
        label.setBorder(new LineBorder(Color.BLACK, 2));
    }

    public static void repaintItem(JLabel label, String fileAdress) {
        ImageIcon image = new ImageIcon(fileAdress);
        Image pic = image.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon(pic);
        label.setIcon(image);
    }

    public static ImageIcon createIcon(String fileAdress, int sizeX, int sizeY) {
        Image pic = new ImageIcon(fileAdress).getImage().getScaledInstance(sizeX, sizeY, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(pic);
    }
}
