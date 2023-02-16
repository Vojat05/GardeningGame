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
        JFrame menuWindow = new JFrame();

        JButton start = new JButton("Start");
        {
            start.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    menuWindow.dispose();
                    new Game(1920, 1080);
                }
            });
        }

        JPanel buttonPanel = new JPanel();
        {
            buttonPanel.setPreferredSize(new Dimension(200, 600));
            buttonPanel.setBackground(Color.CYAN);
            buttonPanel.add(start);
        }

        MenuPanel menu = new MenuPanel(1920, 1080, buttonPanel);
        {
            menuWindow.setPreferredSize(new Dimension(1920, 1080));
            menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuWindow.setResizable(false);
            menuWindow.setTitle("Dad The Gardener");

            menuWindow.add(menu);
            menuWindow.pack();
            menuWindow.setVisible(true);
        }
        // new Game(1920, 1080);
    }
}
