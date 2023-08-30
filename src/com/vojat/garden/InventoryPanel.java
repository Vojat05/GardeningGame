package com.vojat.garden;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class InventoryPanel extends JPanel{
    
    /*
     * --------------------------------------------------------------------------------
     * Inventory panel variable // The panel below the game panel
     * --------------------------------------------------------------------------------
     */

    public Font HPfont;                                                     // Custom font for the HP value text
    private Color HPBorderColor = Color.WHITE;                              // The color of the outer HP border
    private JLabel item = new JLabel();                                     // The label for the selected item
    private Player dad;                                                     // The player character

    /*
     * --------------------------------------------------------------------------------
     * Inventory panel constructor used for buildin up the menu
     * --------------------------------------------------------------------------------
     */

    public InventoryPanel(int windowWidth, int windowHeight, GamePanel gamePanel, Player dad) {
        this.dad = dad;

        {
            setBackground(new Color(40, 40, 40));
            setBounds(0, windowHeight-75, windowWidth, 75);
        }

        // Sets up the custom HP font
        try {

            HPfont = Font.createFont(Font.TRUETYPE_FONT, new File("res/Fonts/customFont.ttf"));

        } catch (FontFormatException | IOException e) {

            e.printStackTrace();

        }

        item.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.RED));
        repaintItem(dad);
    }

    /*
     * --------------------------------------------------------------------------------
     * Static repaint methods used throughout the game
     * --------------------------------------------------------------------------------
     */

    // Repaint the selected item
    public void repaintItem(Player dad) {

        item.setIcon(new ImageIcon(new ImageIcon("res/Pics/" + dad.inventory.get(dad.selectedItem) + ".png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
        add(item);

    }

    public static void repaintItem(int index, JLabel label, Player dad) {

        label.setIcon(new ImageIcon(new ImageIcon("res/Pics/" + dad.inventory.get(index) + ".png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
        label.setBorder(new LineBorder(Color.BLACK, 2));

    }

    public static void repaintItem(JLabel label, String fileAdress) {

        label.setIcon(new ImageIcon(new ImageIcon(fileAdress).getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)));

    }

    // Creates the icon with a certian size from an image path
    public static Icon createIcon(String fileAdress, int sizeX, int sizeY) {

        Image pic = new ImageIcon(fileAdress).getImage().getScaledInstance(sizeX, sizeY, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(pic);

    }

    public Color setHPBorderColor(Color color) {

        return this.HPBorderColor = color;

    }

    /*
     * --------------------------------------------------------------------------------
     * The inventory panel repaint method
     * --------------------------------------------------------------------------------
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        /*
        * --------------------------------------------------------------------------------
        * Drawing the HP bar
        * --------------------------------------------------------------------------------
        */

        g2d.setPaint(new Color(239, 244, 245, 180));
        
        // The icon block in front
        int[] iconX = {20, 44, 44, 20, 20, 30, 32, 32, 30, 20};
        int[] iconY = {15, 15, 63, 63, 51, 50, 48, 29, 27, 27};

        g2d.fillPolygon(iconX, iconY, iconX.length);

        // The HP bar background
        int[] barBgX = {46, 402, 382, 262, 260, 260, 46};
        int[] barBgY = {15, 15, 49, 49, 51, 63, 63};

        g2d.fillPolygon(barBgX, barBgY, barBgX.length);

        // The HP bar sloped edge smoothening
        g2d.setPaint(new Color(239, 244, 245, 100));
        g2d.drawLine(402, 15, 382, 48);

        g2d.setPaint(new Color(239, 244, 245, 50));
        g2d.drawLine(402, 16, 383, 47);

        // The HP bar value
        if (dad.HP != 0) {

            g2d.setPaint(dad.HP <= 60 ? dad.HP <= 20 ? new Color(0xfa2311) : new Color(0xf9c52e) : new Color(0x3bf02f));
    
            int[] hpBarValueX = {65, 378, 371, 249, 244, 65};
            int[] hpBarValueY = {28, 28, 37, 37, 47, 47};
    
            hpBarValueX[1] = 65 + Math.round(Math.round(3.13 * dad.HP));
            hpBarValueX[2] = 60 + Math.round(Math.round(3.13 * dad.HP));
    
            if (dad.HP == 1) {

                hpBarValueX[3] = 65;
                hpBarValueX[4] = 65;
                
            } else if (dad.HP < 60) {
                
                hpBarValueX[3] = 60 + Math.round(Math.round(3.13 * dad.HP));
                hpBarValueX[4] = 55 + Math.round(Math.round(3.13 * dad.HP));

            }
    
            g2d.fillPolygon(hpBarValueX, hpBarValueY, hpBarValueX.length);

        }

        // The HP bar border black
        g2d.setStroke(new BasicStroke(2));
        g2d.setPaint(Color.DARK_GRAY);

        int[] borderBlackX = {64, 379, 372, 249, 244, 64};
        int[] borderBlackY = {27, 27, 38, 38, 48, 48};

        g2d.drawPolygon(borderBlackX, borderBlackY, borderBlackX.length);

        // The HP bar border white
        g2d.setStroke(new BasicStroke(1));
        g2d.setPaint(HPBorderColor);

        int[] borderWhiteX = {62, 382, 373, 250, 245, 62};
        int[] borderWhiteY = {25, 25, 39, 39, 49, 49};

        g2d.drawPolygon(borderWhiteX, borderWhiteY, borderWhiteX.length);


        /*
        * --------------------------------------------------------------------------------
        * Drawing the HP integer values
        * --------------------------------------------------------------------------------
        */

        g2d.setPaint(new Color(239, 244, 245, 120));

        // The HP value background box
        int[] bgGrayX = {264, 380, 382, 382, 378, 265, 262, 262};
        int[] bgGrayY = {51, 51, 53, 67, 69, 69, 67, 53};

        g2d.fillPolygon(bgGrayX, bgGrayY, bgGrayX.length);

        // The HP value text
        g2d.setPaint(Color.white);
        g2d.setStroke(new BasicStroke(1));

        g2d.setFont(HPfont.deriveFont(15f));
        g2d.drawString((dad.HP == 100 ? "100" : " " + dad.HP) + " / 100" , 298, 66);
        
    }
}
