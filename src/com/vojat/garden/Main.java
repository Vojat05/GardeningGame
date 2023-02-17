package com.vojat.garden;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main{
    public static void main(String[] args) {
        JFrame menuWindow = new JFrame();                                                                       // Create the menu window

        JButton start = new JButton("New Game");                                                         // Create the start game button
        {
            start.addActionListener((e) -> {
                menuWindow.dispose();
                new Game(1920, 1080);
            });
            buttonSetup(start, 150, 40);
        }

        JButton load = new JButton("Load Game");
        {
            load.addActionListener((e) -> System.out.println("Load game button"));
            buttonSetup(load, 150, 40);
        }

        JButton exit = new JButton("Quit");
        {
            exit.addActionListener((e) -> menuWindow.dispose());
            buttonSetup(exit, 150, 40);
        }


        JPanel buttonPanel = new JPanel();                                                                  // Create the button panel
        {
            JPanel spacer = new JPanel();
            JLabel logo = new JLabel();
            InventoryPanel.repaintItem(logo, "res/MrUgly.png");
            spacer.setBackground(null);
            spacer.add(logo);
            buttonPanel.setPreferredSize(new Dimension(200, 1000));
            buttonPanel.setBackground(null);

            buttonPanel.add(spacer);
            buttonPanel.add(start);
            buttonPanel.add(load);
            buttonPanel.add(exit);
        }

        MenuPanel menu = new MenuPanel(1920, 1080, buttonPanel);                   // Create the entire menu as an object of MenuPanel
        {
            menuWindow.setPreferredSize(new Dimension(1920, 1080));
            menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuWindow.setResizable(false);
            menuWindow.setTitle("Dad The Gardener");

            menuWindow.add(menu);
            menuWindow.pack();
            menuWindow.setVisible(true);
        }
    }

    private static void buttonSetup(JButton button, int sizeX, int sizeY) {
        button.setPreferredSize(new Dimension(sizeX, sizeY));
        button.setFocusPainted(false);
    }
}
