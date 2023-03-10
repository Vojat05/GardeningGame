package com.vojat.garden;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Color;

import javax.sound.sampled.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.Enums.*;
import com.vojat.inputs.*;
import com.vojat.menu.Window;

public class GamePanel extends JPanel{
    private ArrayList<Flower> flowers = new ArrayList<>();
    Player dad = new Player(this, 200, 200);
    public InventoryPanel inventoryPanel;
    public JPanel fullInv = new JPanel();
    public boolean inventoryVisible = true;

    public GamePanel(int windowWidth, int windowHeight, Window window) {                                                           // width == window width ; height == window height
        dad.setLimit(windowWidth, windowHeight);
        setFocusable(true);                                                                              // Sets the JPanel focusable, it is later packed into the JFrame

        {                                                                                                           // Passing information for the game window, visible by the pack method
            setPreferredSize(new Dimension(windowWidth, windowHeight-50));
            setBackground();
            setBorder(new LineBorder(Color.BLACK));
        }

        {                                                                                                           // Adding the listeners
            addKeyListener(new KeyboardInput(this, dad, window));
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
                    plant.CURRENT_TEXTURE = plant.setTexture(Game.textures[plant.FLOWER_TEXTURE_NUMBER]);
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
                        if (plant.STATUS.equals("Alive")) {
                            plant.CURRENT_TEXTURE = plant.setTexture(plant.DEAD_TEXTURE);
                            plant.STATUS = "Dead";

                            try {
                                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("res/Audio/MagicSound.wav"));
                                Clip clip = AudioSystem.getClip();
                                clip.open(audioStream);
                                clip.start();
                                System.gc();
                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                System.err.println("Audio error has occured!");
                                e.printStackTrace();
                            }
                        }
                    } else if (plant.TIME_TO_DIE - System.currentTimeMillis() <= Values.TOCHANGE.value) {
                        plant.CURRENT_TEXTURE = plant.setTexture(plant.THIRSTY_TEXTURE);
                    }
                    g.drawImage(plant.CURRENT_TEXTURE, plant.LOCATION_X, plant.LOCATION_Y, 128, 128, null);
                } else {
                    flowers.remove(plant);
                    Game.map[plant.IN_MAP_Y][plant.IN_MAP_X] = 0;
                }
            }

            g.drawImage(dad.currentTexture, dad.LOCATION_X, dad.LOCATION_Y, 128, 128, null); // Resize of the dad texture into 128 x 128 pixels
        } catch(NullPointerException npe) {
            System.err.println(ErrorList.ERR_NPE.message);
        }
    }
}
