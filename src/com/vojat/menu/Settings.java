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

public class Settings extends JPanel {

    /*
     * --------------------------------------------------------------------------------
     * Setting menu variiables
     * --------------------------------------------------------------------------------
     */

    public static boolean visible = false;                                          // Determines wheather the settings menu is visible
    private static KeyboardInput in;                                                // The keyboard input for the controls changes
    private static JSONEditor jEditor;                                              // JSON Editor for saving the controls changes
    private static ArrayList<JPanel> blocks = new ArrayList<>();                    // ArrayList for the control blocks
    private static ArrayList<JLabel> keys = new ArrayList<>();                      // Arraylist for the set key labels
    private static JPanel options = new JPanel();                                   // The panel on which all the controls live on
    private static String[][] inputs = {                                            // The inputs for control message
        {"pause", "Pause the game"}, 
        {"up", "Move Up"}, 
        {"down", "Move Down"}, 
        {"left", "Move Left"}, 
        {"right", "Move Right"}, 
        {"open", "Open inventory"}, 
        {"next", "Select next inventory item"}, 
        {"previous", "Select previous inventory item"}};

    /*
     * --------------------------------------------------------------------------------
     * Constructor building the settings menu
     * --------------------------------------------------------------------------------
     */

    public Settings(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        setBackground(null);
        setVisible(visible);
        setFocusable(true);

        try {
            jEditor = new JSONEditor("src/com/vojat/Data/Controls.json");
            jEditor.readFile();
        } catch(FileNotFoundException fne) {
            System.err.println(ErrorList.ERR_404.message);
        }

        // Panel for the buttons on the left side
        JPanel buttons = new JPanel();
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

        // The black line between the controls and the button panel
        JPanel line = new JPanel();
        {
            line.setPreferredSize(new Dimension(5, 800));
            line.setBackground(Color.BLACK);
            line.setVisible(true);
        }

        JButton back = new JButton("Back");
        {
            MenuPanel.buttonSetup(back, 150, 40, false);
            back.addActionListener((e) -> {
                changeVisibility(buttonPanel, spacer);
                for (int i=0; i<blocks.size(); i++) {

                    // Removes the key labels from each block & the block from the options panel
                    blocks.get(i).remove(keys.get(i));
                    options.remove(blocks.get(i));
                }

                // Clears the JLabel & JPanel ArrayLists
                keys.clear();
                blocks.clear();
            });
            back.setBackground(new Color(25, 25, 25));
            back.setForeground(Color.WHITE);
        }

        // Goes through each JLabel and writes it's value into the controls JSON file one at a time
        JButton save = new JButton("Save");
        {
            MenuPanel.buttonSetup(save, 150, 40, false);
            save.addActionListener((e) -> {
                for (int i=0; i<keys.size(); i++) {
                    jEditor.change(inputs[i][0], keys.get(i).getText());
                }
            });
            save.setBackground(new Color(25, 25, 25));
            save.setForeground(Color.WHITE);
        }

        JButton restore = new JButton("Restore Controls");
        {
            MenuPanel.buttonSetup(restore, 150, 40, false);
            restore.addActionListener((e) -> {
                try {
                    JSONEditor jEditor2 = new JSONEditor("src/com/vojat/Data/ControlsDefault.json");
                    jEditor2.readFile();
                    int picker = 0;
                    for (int i=0; i<keys.size(); i++) {
                        if (i == 1 || i == 5) {
                            picker++;
                        }
                        jEditor.change(inputs[i][0], jEditor2.readData(jEditor2.JSONObjects.get(picker), inputs[i][0]));
                        keys.get(i).setText(jEditor2.readData(jEditor2.JSONObjects.get(picker), inputs[i][0]));
                    }
                } catch (FileNotFoundException fe) {
                    System.err.println(ErrorList.ERR_404.message);
                }
            });
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

    // Creates each block in the options for the text to be Re-rendered for each opening
    public void createDataBlocks() {
        int getter = 0;
        
        for (int i=0; i<8; i++) {
            if (i == 1 || i == 5) {
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

            // Gets the current value from the controls JSON
            JLabel key = new JLabel(jEditor.readData(jEditor.JSONObjects.get(getter), inputs[i][0]), SwingConstants.CENTER);
            {
                key.setFont(new Font("Calibri", Font.BOLD, 40));
                key.setForeground(Color.CYAN);
                key.setPreferredSize(new Dimension(100, 135));
                key.setOpaque(true);
            }

            // Changes the displayed key for the specific block (setting). It doesn't save it
            JButton button = new JButton("Change");
            {
                button.addActionListener((e) -> getKey(button, key));
                button.setFocusPainted(false);
                button.setBorder(new LineBorder(new Color(255, 209, 0), 1));
                button.setForeground(new Color(255, 209, 0));
                button.setPreferredSize(new Dimension(100, 50));
            }

            // Checks if the number of a currently made block is divisable by 2 and makes it lighter or darker
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
            keys.add(key);
        }
    }

    /*
     * --------------------------------------------------------------------------------
     * Controls key change methods
     * --------------------------------------------------------------------------------
     */

    // Creates the keypress listener to listen for a pressed key
    private void getKey(JButton button, JLabel label) {
        in = new KeyboardInput(this, button, label);
        button.addKeyListener(in);
        label.setText("?");
    }

    // Ends the keyboard listening and changes the label text
    public void setKey(char data, JButton button, KeyboardInput in, JLabel label) {
        button.removeKeyListener(in);
        label.setText(("" + data).toUpperCase());
    }

    // Changes the visibility of this panel and it's interactability
    public void changeVisibility(JPanel buttonPanel, JPanel spacer) {
        visible = visible ? false : true;
        setVisible(visible);
        buttonPanel.setVisible(!visible);
        spacer.setVisible(!visible);
    }
}
