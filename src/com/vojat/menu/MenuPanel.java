package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vojat.garden.Game;
import com.vojat.garden.InventoryPanel;

public class MenuPanel extends JPanel{
    
    public MenuPanel(int windowWidth, int windowHeight) {
        setFocusable(true);
        setOpaque(true);
        setBackground(Color.DARK_GRAY);                 // This is the entire main menu background
        setPreferredSize(new Dimension(windowWidth, windowHeight));




        
        JFrame menuWindow = new JFrame();                                                                       // Create the menu window
        JPanel buttonPanel = new JPanel();
        JPanel spacer = new JPanel();
        {
            spacer.setPreferredSize(new Dimension(windowWidth-250, 100));
            spacer.setBackground(null);
        }
        Settings settings = new Settings(1920, 1080, buttonPanel, spacer);
        
        JButton start = new JButton(InventoryPanel.createIcon("res/New.png", 150, 40));                                                         // Create the start game button
        {
            start.addActionListener((e) -> {
                menuWindow.dispose();
                new Game(1920, 1080);
            });
            buttonSetup(start, 150, 40);
        }

        JButton load = new JButton(InventoryPanel.createIcon("res/Load.png", 150, 40));
        {
            load.addActionListener((e) -> System.out.println("Open saves menu"));
            buttonSetup(load, 150, 40);
        }

        JButton options = new JButton(InventoryPanel.createIcon("res/Options.png", 150, 40));
        {
            options.addActionListener((e) -> settings.changeVisibility(buttonPanel, spacer));
            buttonSetup(options, 150, 40);
        }

        JButton exit = new JButton(InventoryPanel.createIcon("res/Exit.png", 150, 40));
        {
            exit.addActionListener((e) -> menuWindow.dispose());
            buttonSetup(exit, 150, 40);
        }
        {
            JPanel spacer1 = new JPanel();
            JPanel spacer2 = new JPanel();
            JLabel logo = new JLabel();
            InventoryPanel.repaintItem(logo, "res/Game_Logo.png");
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
        {
            menuWindow.setPreferredSize(new Dimension(1920, 1080));
            menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuWindow.setResizable(false);
            menuWindow.setTitle("Dad The Gardener");

            menuWindow.add(this);
            menuWindow.pack();
            menuWindow.setVisible(true);
        }

        add(buttonPanel);
        add(spacer);
        add(settings);
    }

    public static void buttonSetup(JButton button, int sizeX, int sizeY) {
        button.setPreferredSize(new Dimension(sizeX, sizeY));
        button.setFocusPainted(false);
        button.setFocusable(false);
    }
}
