package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.vojat.Data.JSONEditor;
import com.vojat.Enums.ErrorList;
import com.vojat.inputs.KeyboardInput;

public class Settings extends JPanel{
    public static boolean visible = false;
    private static KeyboardInput in;
    private static JSONEditor jEditor;
    private static ArrayList<JPanel> blocks = new ArrayList<>();
    private static JPanel options = new JPanel();

    public Settings(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        setBackground(Color.DARK_GRAY);
        setVisible(visible);
        setFocusable(true);

        try {
            jEditor = new JSONEditor("src/com/vojat/Data/Controls.json");
        } catch(FileNotFoundException fne) {
            System.err.println(ErrorList.ERR_404.message);
        }

        JPanel buttons = new JPanel();          // Panel for the buttons on the left side
        {
            buttons.setPreferredSize(new Dimension(200, 500));
            buttons.setBackground(null);
            buttons.setVisible(true);
        }
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
            back.addActionListener((e) -> {
                changeVisibility(buttonPanel, spacer);
                for (int i=0; i<blocks.size(); i++) {
                    options.remove(blocks.get(i));
                }
            });
            back.setBackground(new Color(25, 25, 25));
            back.setForeground(Color.WHITE);
        }
        JButton save = new JButton("Save");
        {
            MenuPanel.buttonSetup(save, 150, 40);
            save.addActionListener((e) -> System.out.println("Save button pressed"));
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
    }

    public void createBlocks() {
        int getter = 1;
        String[][] inputs = {{"up", "Move Up"}, {"down", "Move Down"}, {"left", "Move Left"}, {"right", "Move Right"}, {"open", "Open inventory"}, {"next", "Select next inventory item"}, {"previous", "Select previous inventory item"}};
        for (int i=0; i<7; i++) {
            if (i == 4) {
                getter++;
            }
            JPanel block = new JPanel();
            JLabel name = new JLabel(inputs[i][1], SwingConstants.CENTER);
            {
                name.setFont(new Font("Calibri", Font.BOLD, 20));
                name.setBackground(Color.DARK_GRAY);
                name.setForeground(Color.YELLOW);
                name.setPreferredSize(new Dimension(500, 135));
                name.setOpaque(true);
            }
            JLabel key = new JLabel(jEditor.read(jEditor.JSONObjects.get(getter), inputs[i][0]), SwingConstants.CENTER);
            {
                key.setFont(new Font("Calibri", Font.BOLD, 40));
                key.setForeground(Color.CYAN);
                key.setPreferredSize(new Dimension(100, 135));
                key.setOpaque(true);
            }
            JButton button = new JButton("Change");
            {
                button.addActionListener((e) -> getKey(button, key));
                button.setFocusPainted(false);
                button.setBorder(new LineBorder(new Color(255, 209, 0), 1));
                button.setForeground(new Color(255, 209, 0));
                button.setPreferredSize(new Dimension(100, 50));
            }

            if (i % 2 == 0) {
                block.setBackground(new Color(50, 50, 50, 50));
                button.setBackground(new Color(60, 60, 60));
                key.setBackground(new Color(60, 60, 60));
            } else {
                block.setBackground(new Color(30, 30, 30, 50));
                button.setBackground(new Color(56, 56, 56));
                key.setBackground(new Color(56, 56, 56));
            }
            block.add(button);
            block.add(name);
            block.add(key);

            options.add(block);
            blocks.add(block);
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
