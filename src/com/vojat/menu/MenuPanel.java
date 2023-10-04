package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vojat.Main;
import com.vojat.garden.Game;
import com.vojat.garden.InventoryPanel;

public class MenuPanel extends JPanel{
    
    /*
     * --------------------------------------------------------------------------------
     * The main menu panel
     * --------------------------------------------------------------------------------
     */

    public MenuPanel(Window window) {
        setFocusable(true);
        requestFocus();
        setOpaque(true);

        // This is the entire main menu background
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(Main.sizeX, Main.sizeY));


        JPanel buttonPanel = new JPanel();
        
        // Creates a spacer used for the button panel offset
        JPanel spacer = new JPanel();
        {
            spacer.setPreferredSize(new Dimension(Main.sizeX-250, 100));
            spacer.setBackground(null);
        }

        Settings settings = new Settings(Main.sizeX, Main.sizeY, buttonPanel, spacer);
        Load loadMenu = new Load(Main.sizeX, Main.sizeY, buttonPanel, spacer);
        
        // Create the start new game button
        JButton start = new JButton(InventoryPanel.createIcon("res/" + Game.texturePack + "/Pics/Buttons/New.png", 150, 40));
        {
            start.addActionListener((e) -> {
                new Game(1920, 1075, window);
            });
            buttonSetup(start, 150, 40, true);
        }

        // Create the load game button
        JButton load = new JButton(InventoryPanel.createIcon("res/" + Game.texturePack + "/Pics/Buttons/Load.png", 150, 40));
        {
            load.addActionListener((e) -> {
                loadMenu.changeVisibility(buttonPanel, spacer);
                loadMenu.createDataBlocks(window);
            });
            buttonSetup(load, 150, 40, true);
        }

        // Create the options button
        JButton options = new JButton(InventoryPanel.createIcon("res/" + Game.texturePack + "/Pics/Buttons/Options.png", 150, 40));
        {
            options.addActionListener((e) -> {
                settings.changeVisibility(buttonPanel, spacer);
                settings.createDataBlocks();
            });
            buttonSetup(options, 150, 40, true);
        }

        // Exit button
        JButton exit = new JButton(InventoryPanel.createIcon("res/" + Game.texturePack + "/Pics/Buttons/Exit.png", 150, 40));
        {
            exit.addActionListener((e) -> {
                window.dispose();
                System.exit(0);
            });
            buttonSetup(exit, 150, 40, true);
        }

        /*
         * --------------------------------------------------------------------------------
         * Bundeling all the emelents together
         * --------------------------------------------------------------------------------
         */

        {
            JLabel logo = new JLabel();
            InventoryPanel.repaintItem(logo, "res/" + Game.texturePack + "/Pics/Game_Logo.png");

            JPanel spacer1 = new JPanel();
            spacer1.setBackground(null);
            spacer1.add(logo);

            JPanel spacer2 = new JPanel();
            spacer2.setBackground(null);
            spacer2.setPreferredSize(new Dimension(200, 200));


            buttonPanel.setPreferredSize(new Dimension(200, 1000));
            buttonPanel.setBackground(null);
            buttonPanel.add(spacer2);
            buttonPanel.add(spacer1);
            buttonPanel.add(start);
            buttonPanel.add(load);
            buttonPanel.add(options);
            buttonPanel.add(exit);
        }

        add(buttonPanel);
        add(spacer);
        add(settings);
        add(loadMenu);

        window.setElements(this);
    }

    /*
     * --------------------------------------------------------------------------------
     * Method for setting up the buttons
     * --------------------------------------------------------------------------------
     */

    public static void buttonSetup(JButton button, int sizeX, int sizeY, Boolean border) {
        button.setPreferredSize(new Dimension(sizeX, sizeY));
        button.setFocusPainted(false);
        button.setFocusable(false);
        if (border) button.setBorder(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Smoothening render hint
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Drawing the background
        g2d.setPaint(Color.DARK_GRAY);
        g2d.fillRect(0, 0, Window.width, Window.height);

        // Drawing the Game version
        g2d.setPaint(Color.WHITE);
        g2d.setFont(Game.font.deriveFont(24f));
        System.out.println(Game.version.length());
        g2d.drawString(Game.version, 1886 - (6 * Game.version.length()), 1040);
    }
}
