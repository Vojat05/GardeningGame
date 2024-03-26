package com.vojat.garden;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.menu.Window;

public class InventoryPanel extends JPanel{
    
    /*
     * --------------------------------------------------------------------------------
     * Inventory panel variable // The panel below the game panel
     * --------------------------------------------------------------------------------
     */

    public Color SColor = new Color(0xfadc05);                              // Color for the stamina border to flash red when out of stamina
    private Color HPBorderColor = Color.WHITE;                              // The color of the outer HP border
    private JPanel spacer = new JPanel();                                   // Sizing for the inventory panel, later find better solution
    private Player dad;                                                     // The player character

    /*
     * --------------------------------------------------------------------------------
     * Inventory panel constructor used for buildin up the menu
     * --------------------------------------------------------------------------------
     */

    public InventoryPanel(GamePanel gamePanel, Player dad) {
        this.dad = dad;

        {
            addMouseWheelListener(gamePanel.getMouseInput());
            setBackground(new Color(40, 40, 40));
            setBounds(0, Window.height-75, Window.width, 80);
        }

        spacer.setPreferredSize(new Dimension(10, 70));
        spacer.setOpaque(false);
        add(spacer);
    }

    /*
     * --------------------------------------------------------------------------------
     * Static repaint methods used throughout the game
     * --------------------------------------------------------------------------------
     */

    // Repaint the inventory ( T )
    public static void repaintItem(int index, JLabel label, Player dad) {

        // Terrible fix, repair later
        label.setIcon(new ImageIcon(new ImageIcon("../../res/" + Game.texturePack + "/Pics/" + (dad.inventory.get(index).charAt(0) == 'w' ? "Player" : "Flowers") + "/" + dad.inventory.get(index) + ".png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
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
        int[] iconX = {10, 12, 28, 30, 30, 28, 12, 10};
        int[] iconY = {31, 29, 29, 31, 46, 48, 48, 46};

        g2d.fillPolygon(iconX, iconY, iconX.length);
        
        // Drawing the heart symbol inside the block
        g2d.setPaint(new Color(0xfa2311));

        int[] heartX = {12, 14, 18, 20, 22, 26, 28, 28, 20, 12};
        int[] heratY = {34, 32, 32, 34, 32, 32, 34, 37, 45, 37};

        g2d.fillPolygon(heartX, heratY, heartX.length);

        // The icon border block in front
        g2d.setPaint(new Color(239, 244, 245, 180));

        int[] iconBlockX = {20, 44, 44, 20, 20, 30, 32, 32, 30, 20};
        int[] iconBlockY = {15, 15, 63, 63, 51, 50, 48, 29, 27, 27};

        g2d.fillPolygon(iconBlockX, iconBlockY, iconBlockX.length);

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
         * Drawing the HP bar integer values
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

        g2d.setFont(Game.font.deriveFont(15f));
        g2d.drawString((dad.HP == 100 ? "100" : dad.HP >= 10 ? " " + dad.HP : "  " + dad.HP) + " / 100" , 298, 66);

        /*
         * --------------------------------------------------------------------------------
         * Drawing the inventory spaces
         * --------------------------------------------------------------------------------
         */

        for (int i=0; i<10; i++) {

            g2d.setPaint(new Color(227, 231, 234, 220));
            g2d.setStroke(new BasicStroke(2));

            if (i == dad.selectedItem) {

                g2d.setStroke(new BasicStroke(6));

            }

            g2d.drawRect((int) (Window.width*.5) - 300 + 60*i, 10, 60, 60);
            if (i < dad.inventory.size()) g2d.drawImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/" + (i == 0 || i == 3 ? "Player" : "Icons") + "/" + dad.inventory.get(i) + ".png").getImage(), (int) (Window.width*.5) - 300 + 60*i, 10, 60, 60, null);
        }

        /*
         * --------------------------------------------------------------------------------
         * Drawing the Stamina bar
         * --------------------------------------------------------------------------------
         */

        g2d.setStroke(new BasicStroke(2));
        g2d.setPaint(new Color(239, 244, 245, 180));

        // The icon block in front
        int[] iconXS = {Window.width - 20, Window.width - 22, Window.width - 38, Window.width - 40, Window.width - 40, Window.width - 38, Window.width - 22, Window.width - 20};
        int[] iconYS = {31, 29, 29, 31, 46, 48, 48, 46};

        g2d.fillPolygon(iconXS, iconYS, iconXS.length);

        // Drawing the lightning bolt symbol inside the block
        g2d.setPaint(new Color(0xfaef05));

        int[] heartXS = {Window.width - 25, Window.width - 30, Window.width - 30, Window.width - 35, Window.width - 30, Window.width - 30};
        int[] heratYS = {37, 37, 32, 40, 40, 45};

        g2d.fillPolygon(heartXS, heratYS, heartXS.length);

        // The icon border block in front
        g2d.setPaint(new Color(239, 244, 245, 180));

        int[] iconBlockXS = {Window.width - 30, Window.width - 54, Window.width - 54, Window.width - 30, Window.width - 30, Window.width - 40, Window.width - 42, Window.width - 42, Window.width - 40, Window.width - 30};
        Collections.reverse(Arrays.asList(iconBlockXS));
        int[] iconBlockYS = {15, 15, 63, 63, 51, 50, 48, 29, 27, 27};

        g2d.fillPolygon(iconBlockXS, iconBlockYS, iconBlockXS.length);

        // The HP bar background
        int[] barBgXS = {Window.width - 56, Window.width - 412, Window.width - 392, Window.width - 272, Window.width - 270, Window.width - 270, Window.width - 56};
        Collections.reverse(Arrays.asList(barBgXS));
        int[] barBgYS = {15, 15, 49, 49, 51, 63, 63};

        g2d.fillPolygon(barBgXS, barBgYS, barBgXS.length);

        // The HP bar sloped edge smoothening
        g2d.setPaint(new Color(239, 244, 245, 100));
        g2d.drawLine(Window.width - 412, 15, Window.width - 392, 48);

        g2d.setPaint(new Color(239, 244, 245, 50));
        g2d.drawLine(Window.width - 412, 16, Window.width - 393, 47);

        // The HP bar value
        if (dad.stamina != 0) {

            g2d.setPaint(SColor);
    
            int[] staminaBarX = {Window.width - 75, Window.width - 388, Window.width - 381, Window.width - 259, Window.width - 254, Window.width - 75};
            int[] staminaBarY = {28, 28, 37, 37, 47, 47};
    
            staminaBarX[1] = Window.width - (75 + Math.round(Math.round(3.13 * dad.stamina)));
            staminaBarX[2] = Window.width - (70 + Math.round(Math.round(3.13 * dad.stamina)));
    
            if (dad.stamina == 1) {

                staminaBarX[3] = Window.width - 75;
                staminaBarX[4] = Window.width - 75;
                
            } else if (dad.stamina < 60) {
                
                staminaBarX[3] = Window.width - (70 + Math.round(Math.round(3.13 * dad.stamina)));
                staminaBarX[4] = Window.width - (65 + Math.round(Math.round(3.13 * dad.stamina)));

            }
    
            g2d.fillPolygon(staminaBarX, staminaBarY, staminaBarX.length);

        }

        // The Stamina bar border black
        g2d.setStroke(new BasicStroke(2));
        g2d.setPaint(Color.DARK_GRAY);

        int[] borderBlackXS = {Window.width - 74, Window.width - 389, Window.width - 382, Window.width - 259, Window.width - 254, Window.width - 74};
        int[] borderBlackYS = {27, 27, 38, 38, 48, 48};

        g2d.drawPolygon(borderBlackXS, borderBlackYS, borderBlackXS.length);

        // The Stamina bar border white
        g2d.setStroke(new BasicStroke(1));
        g2d.setPaint(Color.WHITE);

        int[] borderWhiteXS = {Window.width - 72, Window.width - 392, Window.width - 383, Window.width - 260, Window.width - 255, Window.width - 72};
        int[] borderWhiteYS = {25, 25, 39, 39, 49, 49};

        g2d.drawPolygon(borderWhiteXS, borderWhiteYS, borderWhiteXS.length);


        /*
         * --------------------------------------------------------------------------------
         * Drawing the Stamina bar integer values
         * --------------------------------------------------------------------------------
         */

        g2d.setPaint(new Color(239, 244, 245, 120));

        // The HP value background box
        int[] bgGrayXS = {Window.width - 274, Window.width - 390, Window.width - 392, Window.width - 392, Window.width - 388, Window.width - 275, Window.width - 272, Window.width - 272};
        int[] bgGrayYS = {51, 51, 53, 67, 69, 69, 67, 53};

        g2d.fillPolygon(bgGrayXS, bgGrayYS, bgGrayXS.length);

        // The HP value text
        g2d.setPaint(Color.white);
        g2d.setStroke(new BasicStroke(1));

        g2d.setFont(Game.font.deriveFont(15f));
        g2d.drawString((dad.stamina == 100 ? "100" : dad.stamina >= 10 ? " " + dad.stamina : "  " + dad.stamina ) + " / 100" , Window.width - 355, 66);
    }
}
