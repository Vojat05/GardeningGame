package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vojat.Data.JSONEditor;
import com.vojat.Enums.ErrorList;
import com.vojat.inputs.KeyboardInput;

public class Settings extends JPanel{
    public static boolean visible = false;
    private static KeyboardInput in;

    public Settings(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        setBackground(Color.DARK_GRAY);
        setVisible(visible);
        setFocusable(true);

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
            save.addActionListener((e) -> {
                try {
                    new JSONEditor("src/com/vojat/Data/Controls.json");
                } catch(FileNotFoundException fne) {
                    System.err.println(ErrorList.ERR_404.message);
                }
            });
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
            JPanel block = new JPanel();
            JLabel name = new JLabel("&Name");
            {
                name.setBackground(Color.DARK_GRAY);
                name.setForeground(Color.YELLOW);
                name.setPreferredSize(new Dimension(500, 100));
                name.setOpaque(true);
            }
            JLabel key = new JLabel("&Key");
            {
                key.setBackground(Color.DARK_GRAY);
                key.setForeground(Color.CYAN);
                key.setPreferredSize(new Dimension(100, 100));
                key.setOpaque(true);
            }
            JButton button = new JButton("Press");
            {
                button.addActionListener((e) -> getKey(button, key));
                button.setBackground(Color.LIGHT_GRAY);
            }

            block.setBackground(new Color(i*5, i*15, i*30, 50));
            block.add(button);
            block.add(name);
            block.add(key);

            options.add(block);
        }
    }

    private void getKey(JButton button, JLabel label) {
        in = new KeyboardInput(this, button, label);
        button.addKeyListener(in);
    }

    public void pressed(char data, JButton button, KeyboardInput in, JLabel label) {
        button.removeKeyListener(in);
        label.setText(("" + data).toUpperCase());
    }

    public void changeVisibility(JPanel buttonPanel, JPanel spacer) {
        visible = visible ? false : true;
        setVisible(visible);
        buttonPanel.setVisible(!visible);
        spacer.setVisible(!visible);
    }
}
