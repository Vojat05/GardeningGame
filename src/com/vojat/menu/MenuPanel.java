package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vojat.garden.Game;
import com.vojat.garden.InventoryPanel;
import com.vojat.garden.Window;

public class MenuPanel extends JPanel{
    
    public MenuPanel(int windowWidth, int windowHeight, Window window) {
        setFocusable(true);
        setOpaque(true);
        setBackground(Color.DARK_GRAY);                 // This is the entire main menu background
        setPreferredSize(new Dimension(windowWidth, windowHeight));





        JPanel buttonPanel = new JPanel();
        JPanel spacer = new JPanel();
        {
            spacer.setPreferredSize(new Dimension(windowWidth-250, 100));
            spacer.setBackground(null);
        }
        Settings settings = new Settings(1920, 1080, buttonPanel, spacer);
        
        JButton start = new JButton(InventoryPanel.createIcon("res/Pics/New.png", 150, 40));     // Create the start new game button
        {
            start.addActionListener((e) -> {
                mapReset();
                new Game(1920, 1080, window);
            });
            buttonSetup(start, 150, 40);
        }

        JButton load = new JButton(InventoryPanel.createIcon("res/Pics/Load.png", 150, 40));     // Create the load game button
        {
            load.addActionListener((e) -> System.out.println("Open saves menu"));
            buttonSetup(load, 150, 40);
        }

        JButton options = new JButton(InventoryPanel.createIcon("res/Pics/Options.png", 150, 40));       // Create the options button
        {
            options.addActionListener((e) -> settings.changeVisibility(buttonPanel, spacer));
            buttonSetup(options, 150, 40);
        }

        JButton exit = new JButton(InventoryPanel.createIcon("res/Pics/Exit.png", 150, 40));     // Button for closing the game
        {
            exit.addActionListener((e) -> {
                window.dispose();
                System.exit(0);
            });
            buttonSetup(exit, 150, 40);
        }

        {
            JPanel spacer1 = new JPanel();
            JPanel spacer2 = new JPanel();
            JLabel logo = new JLabel();
            InventoryPanel.repaintItem(logo, "res/Pics/Game_Logo.png");
            spacer1.setBackground(null);
            spacer2.setBackground(null);
            spacer2.setPreferredSize(new Dimension(200, 200));
            spacer1.add(logo);
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

        window.setElements(this);
    }

    public static void buttonSetup(JButton button, int sizeX, int sizeY) {
        button.setPreferredSize(new Dimension(sizeX, sizeY));
        button.setFocusPainted(false);
        button.setFocusable(false);
    }

    private void mapReset() {
        for (int i=0; i<Game.map.length; i++) {
            for (int j=0; j<Game.map[0].length; j++) {
                Game.map[i][j] = 0;
            }
        }
    }
}
