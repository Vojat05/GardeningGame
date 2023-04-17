package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vojat.garden.Game;
import com.vojat.garden.InventoryPanel;

public class MenuPanel extends JPanel{
    
    public MenuPanel(int windowWidth, int windowHeight, Window window) {
        setFocusable(true);
        requestFocus();
        setOpaque(true);
        setBackground(Color.DARK_GRAY);                 // This is the entire main menu background
        setPreferredSize(new Dimension(windowWidth, windowHeight));





        JPanel buttonPanel = new JPanel();
        JPanel spacer = new JPanel();       // Creates a spacer used for the button panel offset
        {
            spacer.setPreferredSize(new Dimension(windowWidth-250, 100));
            spacer.setBackground(null);
        }
        Settings settings = new Settings(windowWidth, windowHeight, buttonPanel, spacer);
        Load loadMenu = new Load(windowWidth, windowHeight, buttonPanel, spacer);
        
        JButton start = new JButton(InventoryPanel.createIcon("res/Pics/New.png", 150, 40));     // Create the start new game button
        {
            start.addActionListener((e) -> {
                new Game(1920, 1075, window);
            });
            buttonSetup(start, 150, 40, true);
        }

        JButton load = new JButton(InventoryPanel.createIcon("res/Pics/Load.png", 150, 40));     // Create the load game button
        {
            load.addActionListener((e) -> {
                loadMenu.changeVisibility(buttonPanel, spacer);
                loadMenu.createDataBlocks(window);
            });
            buttonSetup(load, 150, 40, true);
        }

        JButton options = new JButton(InventoryPanel.createIcon("res/Pics/Options.png", 150, 40));       // Create the options button
        {
            options.addActionListener((e) -> {
                settings.changeVisibility(buttonPanel, spacer);
                settings.createDataBlocks();
            });
            buttonSetup(options, 150, 40, true);
        }

        JButton exit = new JButton(InventoryPanel.createIcon("res/Pics/Exit.png", 150, 40));     // Exit button
        {
            exit.addActionListener((e) -> {
                window.dispose();
                System.exit(0);
            });
            buttonSetup(exit, 150, 40, true);
        }

        {
            JLabel logo = new JLabel();
            InventoryPanel.repaintItem(logo, "res/Pics/Game_Logo.png");

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

    public static void buttonSetup(JButton button, int sizeX, int sizeY, Boolean border) {
        button.setPreferredSize(new Dimension(sizeX, sizeY));
        button.setFocusPainted(false);
        button.setFocusable(false);
        if (border) button.setBorder(null);
    }
}
