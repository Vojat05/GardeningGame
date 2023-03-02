package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Settings extends JPanel{
    public static boolean visible = false;

    public Settings(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        JPanel buttons = new JPanel();
        {
            buttons.setPreferredSize(new Dimension(200, 500));
            buttons.setBackground(null);
            buttons.setVisible(true);
        }
        JPanel options = new JPanel();
        {
            options.setLayout(new GridLayout(7, 1));
            options.setPreferredSize(new Dimension(sizeX-300, sizeY-60));
            options.setBackground(null);
            options.setVisible(true);
        }
        JPanel line = new JPanel();
        {
            line.setPreferredSize(new Dimension(5, 800));
            line.setBackground(Color.BLACK);
            line.setVisible(true);
        }

        setPreferredSize(new Dimension(sizeX, sizeY));
        setBackground(Color.DARK_GRAY);
        setVisible(visible);

        JButton back = new JButton("Back");
        {
            MenuPanel.buttonSetup(back, 150, 40);
            back.addActionListener((e) -> changeVisibility(buttonPanel, spacer));
        }
        JButton save = new JButton("Save");
        {
            MenuPanel.buttonSetup(save, 150, 40);
            save.addActionListener((e) -> System.out.println("Save game settings into external file"));
        }
        JButton restore = new JButton("Restore Controls");
        {
            MenuPanel.buttonSetup(restore, 150, 40);
            restore.addActionListener((e) -> System.out.println("Restore controls to default"));
        }

        {
            buttons.add(back);
            buttons.add(restore);
            buttons.add(save);
        }

        {
            add(buttons);
            add(line);
            add(options);
        }

        for (int i=0; i<7; i++) {
            JButton button = new JButton("Button " + i);
            options.add(button);
        }
    }

    public void changeVisibility(JPanel buttonPanel, JPanel spacer) {
        visible = visible ? false : true;
        setVisible(visible);
        buttonPanel.setVisible(!visible);
        spacer.setVisible(!visible);
    }
}
