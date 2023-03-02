package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Settings extends JPanel{
    public static boolean visible = false;

    public Settings(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        setBackground(Color.DARK_GRAY);
        setVisible(visible);

        JPanel buttons = new JPanel();          // Panel for the buttons on the left side
        {
            buttons.setPreferredSize(new Dimension(200, 500));
            buttons.setBackground(null);
            buttons.setVisible(true);
        }
        JPanel options = new JPanel();          // Panel for changing the specific options
        {
            options.setLayout(new GridLayout(7, 1));
            options.setPreferredSize(new Dimension(sizeX-300, sizeY-60));
            options.setBackground(null);
            options.setVisible(true);
        }
        JPanel line = new JPanel();             // The black line in the midle
        {
            line.setPreferredSize(new Dimension(5, 800));
            line.setBackground(Color.BLACK);
            line.setVisible(true);
        }

        JButton back = new JButton("Back");
        {
            MenuPanel.buttonSetup(back, 150, 40);
            back.addActionListener((e) -> changeVisibility(buttonPanel, spacer));
            back.setBackground(new Color(25, 25, 25));
            back.setForeground(Color.WHITE);
        }
        JButton save = new JButton("Save");
        {
            MenuPanel.buttonSetup(save, 150, 40);
            save.addActionListener((e) -> System.out.println("Save game settings into external file"));
            save.setBackground(new Color(25, 25, 25));
            save.setForeground(Color.WHITE);
        }
        JButton restore = new JButton("Restore Controls");
        {
            MenuPanel.buttonSetup(restore, 150, 40);
            restore.addActionListener((e) -> System.out.println("Restore controls to default"));
            restore.setBackground(new Color(25, 25, 25));
            restore.setForeground(Color.WHITE);
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
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
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
