package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Settings extends JPanel{
    public static boolean visible = false;

    public Settings(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        setPreferredSize(new Dimension(sizeX, sizeY));
        setBackground(Color.DARK_GRAY);
        setVisible(visible);

        JButton back = new JButton("Back");
        {
            MenuPanel.buttonSetup(back, 150, 40);
            back.addActionListener((e) -> {
                changeVisibility();
                buttonPanel.setVisible(true);
                spacer.setVisible(true);
            });
        }
        add(back);
    }

    public void changeVisibility() {
        visible = visible ? false : true;
        setVisible(visible);
    }
}
