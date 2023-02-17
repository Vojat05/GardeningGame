package com.vojat.garden;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main{
    public static void main(String[] args) {
        JFrame menuWindow = new JFrame();                                                                   // Create the menu window

        JButton start = new JButton("Start Game");                                                         // Create the start game button
        {
            start.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    menuWindow.dispose();
                    new Game(1920, 1080);
                }
            });
            start.setPreferredSize(new Dimension(150, 40));
            start.setFocusPainted(false);
        }

        JButton load = new JButton("Load Game");
        {
            load.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Game load button presed");
                }
            });
            load.setPreferredSize(new Dimension(150, 40));
            load.setFocusPainted(false);
        }

        JButton exit = new JButton("Quit");
        {
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    menuWindow.dispose();
                }
            });
            exit.setPreferredSize(new Dimension(150, 40));
            exit.setFocusPainted(false);
        }


        JPanel buttonPanel = new JPanel();                                                                  // Create the button panel
        {
            buttonPanel.setPreferredSize(new Dimension(200, 600));
            buttonPanel.setBackground(Color.CYAN);

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
}
