package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Load extends JPanel{
    public static boolean visible = false;
    private static JPanel saves = new JPanel();
    private static ArrayList<JPanel> blocks = new ArrayList<>();

    public Load(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        setBackground(null);
        setVisible(visible);
        setFocusable(true);

        JButton back = new JButton("Back");
        {
            MenuPanel.buttonSetup(back, 150, 40);
            back.addActionListener((e) -> {
                changeVisibility(buttonPanel, spacer);
                for (int i=0; i<blocks.size(); i++) {
                    saves.remove(blocks.get(i));
                }
                blocks.clear();
            });
            back.setBackground(new Color(25, 25, 25));
            back.setForeground(Color.WHITE);
        }

        JPanel space = new JPanel();        // Spacer
        {
            space.setPreferredSize(new Dimension(200, 150));
            space.setBackground(null);
        }

        JPanel line = new JPanel();             // The black line in the midle
        {
            line.setPreferredSize(new Dimension(5, 800));
            line.setBackground(Color.BLACK);
            line.setVisible(true);
        }

        JPanel buttons = new JPanel();
        {
            buttons.setPreferredSize(new Dimension(200, sizeY-20));
            buttons.setBackground(null);
            buttons.add(space);
            buttons.add(back);
        }

        {
            saves.setLayout(new GridLayout(2, 3, 50, 50));
            saves.setPreferredSize(new Dimension(sizeX-300, sizeY-200));
            saves.setBackground(null);
            saves.setVisible(true);
        }

        add(buttons);
        add(line);
        add(saves);
    }

    public void createDataBlocks() {
        for (int i=0; i<6; i++) {
            JPanel saveBlock = new JPanel();
            
            saves.add(saveBlock);
            blocks.add(saveBlock);

            if (i+1 == 3 || i+1 == 6) {
                createButton(saveBlock);
            }

            if (saveBlock.getComponentCount() == 0) {
                saveBlock.setBackground(new Color(30, 30, 30, 40));
            } else {
                saveBlock.setBackground(new Color(107, 252, 3, 60));
            }
        }
    }

    private void buttonPress(JButton button) {
        System.out.println("Load save number " + button.getText().charAt(button.getText().length()-1));
    }

    public void createButton(JPanel panel) {
        JButton button = new JButton("Load save " + (blocks.indexOf(panel)+1));
        button.addActionListener((e) -> buttonPress(button));
        button.setFocusPainted(false);

        panel.add(button);
    }

    public void changeVisibility(JPanel buttonPanel, JPanel spacer) {
        visible = visible ? false : true;
        setVisible(visible);
        buttonPanel.setVisible(!visible);
        spacer.setVisible(!visible);
    }
}
