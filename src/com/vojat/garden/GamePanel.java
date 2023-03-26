package com.vojat.garden;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.Enums.*;
import com.vojat.inputs.*;
import com.vojat.menu.Window;

public class GamePanel extends JPanel {
    Player dad = new Player(this, 200, 200);
    public InventoryPanel inventoryPanel;
    public JPanel fullInv = new JPanel();
    public boolean inventoryVisible = true;

    // width == window width & height == window height
    public GamePanel(int windowWidth, int windowHeight, Window window) {
        dad.setLimit(windowWidth, windowHeight);
        setFocusable(true);         // Sets the JPanel focusable, it is later packed into the JFrame

        {   // Passing information for the game window, visible by the pack method
            setPreferredSize(new Dimension(windowWidth, windowHeight-50));
            setBackground();
            setBorder(new LineBorder(Color.BLACK));
        }

        {   // Adding the listeners
            addKeyListener(new KeyboardInput(this, dad, window));
            addMouseListener(new MouseInput(dad));
        }

        {   // Adding the inventory table
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



    // Sets the inventory panel field (just for the repaint method to be functional in the listener)
    public void setIPanel(InventoryPanel iPanel) {
        this.inventoryPanel = iPanel;
    }

    // Method that sets the background to a desired thing
    private void setBackground() {
        setBackground(new Color(90, 180, 4));
    }

    // Adds a flower to flowers ArrayList
    public void summonFlower(Flower flower) {
        Game.flowers.add(flower);
    }

    // Changes the visibility of an inventory table
    public void changeVisibility(JPanel panel, boolean value) {
        panel.setVisible(value);
        inventoryVisible = inventoryVisible ? false : true;
    }

    // Finds the plant in the flowers ArrayList and runs checks if it's dead or not, if passed, restores texture and resets death timer
    public void waterFlower(Flower flower, int positionX, int positionY) {
        for (Flower plant : Game.flowers) {
            if (plant.LOCATION_X == positionX && plant.LOCATION_Y == positionY) {
                if (plant.STATUS.equals("Alive")) {
                    switch (plant.TYPE) {
                        case "tulip":
                            plant.TIME_TO_DIE = System.currentTimeMillis() + Values.TODIE_REDTULIP.value;
                            plant.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Values.TODIE_REDTULIP.value + 5000;
                            break;
                        
                        case "rose":
                            plant.TIME_TO_DIE = System.currentTimeMillis() + Values.TODIE_ROSE.value;
                            plant.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Values.TODIE_ROSE.value + 5000;
                            break;
                    }
                    plant.CURRENT_TEXTURE = plant.setTexture(plant.ALIVE_TEXTURE);
                }
            }
        }
    }



    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Drawing all the placed plants by a for loop to edit the plants
        try {
            for (int i=0; i<Game.flowers.size(); i++) {
                Flower plant = Game.flowers.get(i);
                if (plant.TIME_TO_DISSAPEAR >= System.currentTimeMillis()) {
                    if (plant.TIME_TO_DIE <= System.currentTimeMillis()) {
                        if (plant.STATUS.equals("Alive")) {
                            plant.CURRENT_TEXTURE = plant.setTexture(plant.DEAD_TEXTURE);
                            plant.STATUS = "Dead";
                            Game.playMusic("res/Audio/MagicSound.wav");
                        }
                    } else if (plant.TIME_TO_DIE - System.currentTimeMillis() <= Values.TOCHANGE.value) {
                        plant.CURRENT_TEXTURE = plant.setTexture(plant.THIRSTY_TEXTURE);
                    }
                    g.drawImage(plant.CURRENT_TEXTURE, plant.LOCATION_X*128, plant.LOCATION_Y*128, 128, 128, null);
                } else {
                    Game.flowers.remove(plant);
                    Game.map[plant.LOCATION_Y][plant.LOCATION_X] = 0;
                }
            }

            g.drawImage(dad.currentTexture, dad.LOCATION_X, dad.LOCATION_Y, 128, 128, null);    // Resize of the dad texture into 128 x 128 pixels
        } catch(NullPointerException npe) {
            System.err.println(ErrorList.ERR_NPE.message);
        }
    }
}
