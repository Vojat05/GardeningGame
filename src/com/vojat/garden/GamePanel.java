package com.vojat.garden;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.Enums.*;
import com.vojat.inputs.KeyboardInput;
import com.vojat.inputs.MouseInput;

public class GamePanel extends JPanel{
    private ArrayList<Flower> flowers = new ArrayList<>();
    Player dad = new Player(this, 200, 200);
    public static byte[][] map = new byte[8][15];                                                                   // [Y][X] coords  -> Now it's a total of 120 spots to place a flower
    public static String[] textures = {"res/Water_Can.png", "res/Red_Tulip.png", "res/Blue_Rose.png"};              // Array of texture paths, mush be bigger by one then number of flowers and in the same order as player inventory
    public InventoryPanel inventoryPanel;
    public JPanel fullInv = new JPanel();
    public boolean inventoryVisible = true;

    public GamePanel(int windowWidth, int windowHeight) {                                                           // width == window width ; height == window height
        dad.setLimit(windowWidth, windowHeight);
        setFocusable(true);                                                                              // Sets the JPanel focusable, it is later packed into the JFrame

        {                                                                                                           // Passing information for the game window, visible by the pack method
            setPreferredSize(new Dimension(windowWidth, windowHeight-50));
            setBackground();
            setBorder(new LineBorder(Color.BLACK));
        }

        {                                                                                                           // Adding the listeners
            addKeyListener(new KeyboardInput(this, dad));
            addMouseListener(new MouseInput(dad));
        }

        {                                                                                                           // Adding the inventory table
            fullInv.setBorder(new LineBorder(Color.BLACK));
            fullInv.setPreferredSize(new Dimension(windowWidth - 20, 80));
            fullInv.setBackground(new Color(0, 0, 0, 50));
            for (int i=0; i<dad.inventory.length; i++) {
                JLabel item = new JLabel();
                InventoryPanel.repaintItem(i, item);
                fullInv.add(item);
            }
            fullInv.setVisible(false);
            add(fullInv);
        }
    }




    public void setIPanel(InventoryPanel iPanel) {                                                               // Sets the inventory panel field (just for the repaint method to be functional in the listener)
        this.inventoryPanel = iPanel;
    }

    private void setBackground() {                                                                                  // Method that sets the background to a desired thing
        setBackground(new Color(84, 194, 4));
    }

    public void summonFlower(Flower flower) {                                                                       // Adds a flower to flowers ArrayList
        flowers.add(flower);
    }

    public void changeVisibility(JPanel panel, boolean value) {                                                     // Changes the visibility of an inventory table
        panel.setVisible(value);
        inventoryVisible = inventoryVisible ? false : true;
    }

    public void waterFlower(Flower flower, int positionX, int positionY) {                                          // Finds the plant in the flowers ArrayList and runs checks if it's dead or not, if passed, restores texture and resets death timer
        for (Flower plant : flowers) {
            if (plant.IN_MAP_X == positionX && plant.IN_MAP_Y == positionY) {
                if (plant.STATUS.equals("Alive")) {
                    switch(plant.TYPE) {
                        case "tulip":
                            plant.TIME_TO_DIE = System.currentTimeMillis() + Values.TODIE_REDTULIP.value;
                            plant.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Values.TODISSAPEAR_REDTULIP.value;
                            break;
                        
                        case "rose":
                            plant.TIME_TO_DIE = System.currentTimeMillis() + Values.TODIE_ROSE.value;
                            plant.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Values.TODISSAPEAR_ROSE.value;
                            break;
                    }
                    plant.CURRENT_TEXTURE = plant.setTexture(textures[plant.FLOWER_TEXTURE_NUMBER]);
                }
            }
        }
    }



    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try{
            for (int i=0; i<flowers.size(); i++) {                                                                  // Drawing all the placed plants by for loop to edit the plants
                Flower plant = flowers.get(i);
                if (plant.TIME_TO_DISSAPEAR >= System.currentTimeMillis()) {
                    if (plant.TIME_TO_DIE <= System.currentTimeMillis()) {
                        plant.CURRENT_TEXTURE = plant.setTexture("res/MrUgly.png");
                        plant.STATUS = "Dead";
                    } else if (plant.TIME_TO_DIE - System.currentTimeMillis() <= Values.TOCHANGE.value) {
                        plant.CURRENT_TEXTURE = plant.setTexture("res/Land.png");
                    }
                    g.drawImage(plant.CURRENT_TEXTURE, plant.LOCATION_X, plant.LOCATION_Y, 128, 128, null);
                } else {
                    flowers.remove(plant);
                    map[plant.IN_MAP_Y][plant.IN_MAP_X] = 0;
                }
            }

            g.drawImage(dad.currentTexture, dad.LOCATION_X, dad.LOCATION_Y, 128, 128, null); // Resize of the dad texture into 128 x 128 pixels
        } catch(NullPointerException npe) {
            System.err.println(ErrorList.ERR_NPE.message);
        }
    }

    public void saveGame() {;}

    public void loadGame(byte[][] map, HashMap<Integer, String> decoder) {;}
}
