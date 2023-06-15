package com.vojat.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vojat.Main;
import com.vojat.Data.JSONEditor;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Game;
import com.vojat.garden.InventoryPanel;
import com.vojat.inputs.MouseInput;

public class Load extends JPanel {
    public static boolean visible = false;
    private static JPanel saves = new JPanel();
    private static ArrayList<JPanel> blocks = new ArrayList<>();
    private static JPanel buttonPanelT, spacerT;

    public Load(int sizeX, int sizeY, JPanel buttonPanel, JPanel spacer) {
        buttonPanelT = buttonPanel;
        spacerT = spacer;
        setBackground(null);
        setVisible(visible);
        setFocusable(true);

        JButton back = new JButton("Back");
        {
            MenuPanel.buttonSetup(back, 150, 40, false);
            back.addActionListener((e) -> {
                changeVisibility(buttonPanel, spacer);
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

    public void createDataBlocks(Window window) {        // Creates the 6 Save blocks
        for (int i=0; i<6; i++) {
            JPanel saveBlock = new JPanel();
            JPanel spacer = new JPanel();
            {
                spacer.setPreferredSize(new Dimension(400, 350));
                spacer.setBackground(new Color(60, 60, 60, 40));
            }

            // If the specific file has a save point, it shows an image
            if (new File("src/com/vojat/Data/Saves/Save" + (i+1) + ".json").isFile()) {
                JLabel saveFilePicture = new JLabel();
                {
                    InventoryPanel.repaintItem(saveFilePicture, "res/Pics/save.png");
                    spacer.add(saveFilePicture);
                }
            }
            saves.add(saveBlock);
            blocks.add(saveBlock);
            saveBlock.add(spacer);
            
            createButton(saveBlock, window);
            saveBlock.setBackground(new Color(30, 30, 30, 40));
        }
    }

    // This method is called when the load button is pressed
    private void buttonPress(JButton button, int saveNumber, Window window) {
        try {
            if (!(new File("src/com/vojat/Data/Saves/Save" + saveNumber + ".json").isFile())) {
                if (Main.debug) {
                    System.out.println("Creating File");
                    JSONEditor.createFile("src/com/vojat/Data/Saves/Save" + saveNumber + ".json");
                }
            } else {
                changeVisibility(buttonPanelT, spacerT);
                new Game(1920, 1075, window);
                Game.loadGame("src/com/vojat/Data/Saves/Save" + saveNumber + ".json");
                MouseInput.setAssignPlantNum(Game.flowers.size());
            }
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_404.message);
        }
    }

    public void createButton(JPanel panel, Window window) {        // Creates a button to be passed to the block
        JButton button = new JButton(InventoryPanel.createIcon("res/Pics/Load.png", 150, 40));
        int saveNumber = blocks.indexOf(panel)+1;
        button.addActionListener((e) -> buttonPress(button, saveNumber, window));
        button.setPreferredSize(new Dimension(150, 40));
        button.setFocusPainted(false);
        button.setBorder(null);

        panel.add(button);
    }

    public void changeVisibility(JPanel buttonPanel, JPanel spacer) {
        visible = visible ? false : true;
        setVisible(visible);
        buttonPanel.setVisible(!visible);
        spacer.setVisible(!visible);
        for (int i=0; i<blocks.size(); i++) {
            saves.remove(blocks.get(i));
        }
        blocks.clear();
    }
}
