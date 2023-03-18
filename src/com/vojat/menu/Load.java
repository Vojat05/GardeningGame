package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Load extends JPanel{
    public static boolean visible = false;
    private static JPanel saves = new JPanel();

    public Load(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        setBackground(null);
        setVisible(visible);
        setFocusable(true);

        {
            saves.setLayout(new GridLayout(6, 1));
            saves.setPreferredSize(new Dimension(sizeX-300, sizeY-40));
            saves.setBackground(Color.BLACK);
            saves.setVisible(true);
        }

        JButton back = new JButton("Back");
        {
            MenuPanel.buttonSetup(back, 150, 40);
            back.addActionListener((e) -> {
                changeVisibility(buttonPanel, spacer);
            });
            back.setBackground(new Color(25, 25, 25));
            back.setForeground(Color.WHITE);
        }

        JPanel space = new JPanel();
        {
            space.setPreferredSize(new Dimension(200, sizeY-100));
            space.setBackground(null);
        }
        JPanel buttons = new JPanel();
        {
            buttons.setPreferredSize(new Dimension(200, sizeY-20));
            buttons.setBackground(null);
            buttons.add(space);
            buttons.add(back);
        }

        add(buttons);
        add(saves);
    }

    public void changeVisibility(JPanel buttonPanel, JPanel spacer) {
        visible = visible ? false : true;
        setVisible(visible);
        buttonPanel.setVisible(!visible);
        spacer.setVisible(!visible);
    }
}
