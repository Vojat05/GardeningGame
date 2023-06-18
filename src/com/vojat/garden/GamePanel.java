package com.vojat.garden;

import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.Random;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.Enums.ErrorList;
import com.vojat.inputs.*;
import com.vojat.menu.Window;

public class GamePanel extends JPanel {
    public Player dad = new Player(this, 240, 200);
    public InventoryPanel inventoryPanel;
    public JPanel fullInv = new JPanel();
    public JPanel saveMenu = new JPanel();
    public boolean changeGrass = true;

    // width == window width & height == window height
    public GamePanel(int windowWidth, int windowHeight, Window window) {
        dad.setLimit(windowWidth, windowHeight-50);
        setFocusable(true);         // Sets the JPanel focusable, it is later packed into the JFrame

        {   // Passing information for the game window, visible by the pack method
            setBounds(0, 0, windowWidth, windowHeight-50);
            setPreferredSize(new Dimension(windowWidth, windowHeight-50));
            setBackground(null);        // new Color(90, 180, 4)
            setBorder(new LineBorder(Color.BLACK));
        }

        {   // Adding the listeners
            addKeyListener(new KeyboardInput(this, dad, window));
            addMouseListener(new MouseInput(this));
        }

        {   // Adding the inventory table
            fullInv.setBorder(new LineBorder(Color.BLACK));
            fullInv.setPreferredSize(new Dimension(windowWidth - 20, 80));
            fullInv.setBackground(new Color(0, 0, 0, 50));
            for (int i=0; i<dad.inventory.size(); i++) {
                JLabel item = new JLabel();
                InventoryPanel.repaintItem(i, item);
                fullInv.add(item);
            }
            fullInv.setVisible(false);
            add(fullInv);
        }

        {   // Creates savemenu visible after getting in bed
            saveMenu.setBorder(new LineBorder(Color.BLACK));
            saveMenu.setPreferredSize(new Dimension(240, 590));
            saveMenu.setBackground(new Color(50, 50, 50));
            FlowLayout saveMenLayout = new FlowLayout(FlowLayout.CENTER, 40, 30);
            saveMenu.setLayout(saveMenLayout);
            for (int i=0; i<=6; i++) {
                JButton button = new JButton(i == 0 ? "Close" : "Save " + i);
                button.setPreferredSize(new Dimension(200, 50));
                if (i != 0) button.addActionListener((e) -> saveButton(button)); else button.addActionListener((e) -> {Game.pauseGame(); dad.LOCATION_X = 80; dad.LOCATION_Y = 120; changeVisibility(saveMenu);});
                saveMenu.add(button);
            }
            saveMenu.setVisible(false);
            add(saveMenu);
        }
    }



    // Sets the inventory panel field (just for the repaint method to be functional in the listener)
    public void setIPanel(InventoryPanel iPanel) {
        this.inventoryPanel = iPanel;
    }

    // Adds a flower to flowers ArrayList
    public void summonFlower(Flower flower) {
        Game.flowers.add(flower);
    }

    private void saveButton(JButton button) {
        try {
            Game.saveGame("src/com/vojat/Data/Saves/Save" + button.getText().substring(button.getText().length()-1, button.getText().length()) + ".json", dad);
        } catch (FileNotFoundException f) {
            System.err.println(ErrorList.ERR_404.message);
        } finally {
            Game.pauseGame();
        }
        changeVisibility(saveMenu);
    }

    // Changes the visibility of an inventory table
    public void changeVisibility(JPanel panel) {
        panel.setVisible(panel.isVisible() ? false : true);
    }

    // Finds the plant in the flowers ArrayList and runs checks if it's dead or not, if passed, restores texture and resets death timer
    public void waterFlower(Flower flower, int positionX, int positionY) {
        for (Flower plant : Game.flowers) {
            if (plant.LOCATION_X == positionX && plant.LOCATION_Y == positionY) {

                if (plant.STATUS.equals("Dead")) {
                    return;
                }

                for (int i=0; i<Game.flowerTypes.length; i++) {
                    if (plant.TYPE == Game.flowerTypes[i][0]) {
                        plant.TIME_TO_DIE = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]);
                        plant.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Integer.parseInt(Game.flowerTypes[i][1]) + 5000;
                        break;
                    }
                }
                plant.CURRENT_TEXTURE = plant.setTexture(plant.ALIVE_TEXTURE);
            }
        }
    }

    // Drawing terrain based on the map
    private void drawTerrain(byte[][] map, Graphics g) {
        // Drawing the grass textures and other static objects (well, house ...)
        if (dad.level == 0) {
            for (int i=0; i<map.length; i++) {
                for (int j=0; j<map[0].length; j++) {
                    if (map[i][j] <= 1) {
                        if (changeGrass) {
                            Random rnd = new Random();
                            map[i][j] = (byte) (rnd.nextInt(0, 2));
                        }
                        g.drawImage(new ImageIcon(Game.groundTextures[map[i][j]]).getImage(), 128*j, 128*i, 128, 128, null);   // Draw the grass
                    } else if (map[i][j] >= 4) {
                        g.drawImage(new ImageIcon(Game.groundTextures[map[i][j]]).getImage(), 128*j, 128*i, 128, 128, null);   // Draw the objects like a well
                    }
                }
            }
        } else {
            for (int i=0; i<map.length; i++) {
                for (int j=0; j<map[0].length; j++) {
                    g.drawImage(new ImageIcon(Game.houseTextures[map[i][j]]).getImage(), 128*j, 128*i, 128, 128, null);
                }
            }
        }
        

        try {
            if (dad.level == 0) {
                // Draw the house
                g.drawImage(new ImageIcon(Game.groundTextures[map[0][1]]).getImage(), 128, 0, 256, 256, null);
                changeGrass = false;

                // Drawing all the placed plants by a for loop to edit the plants
                for (int i=0; i<Game.flowers.size(); i++) {
                    Flower plant = Game.flowers.get(i);
                    if (plant.TIME_TO_DISSAPEAR >= System.currentTimeMillis()) {
                        if (plant.TIME_TO_DIE <= System.currentTimeMillis()) {
                            if (plant.STATUS.equals("Alive")) {
                                plant.CURRENT_TEXTURE = plant.setTexture(plant.DEAD_TEXTURE);
                                plant.STATUS = "Dead";
                                Game.playSound("res/Audio/MagicSound.wav");
                            }
                        } else if (plant.TIME_TO_DIE - System.currentTimeMillis() <= Game.flowerChange) {
                            plant.CURRENT_TEXTURE = plant.setTexture(plant.THIRSTY_TEXTURE);
                        }
                        g.drawImage(plant.CURRENT_TEXTURE, plant.LOCATION_X*128, plant.LOCATION_Y*128, 128, 128, null);     // Draw the flower
                    } else {
                        Game.flowers.remove(plant);
                        map[plant.LOCATION_Y][plant.LOCATION_X] = 0;
                    }
                }
            }

            // Drawing the player character in 128 x 128
            g.drawImage(dad.currentTexture, (int) dad.LOCATION_X, (int) dad.LOCATION_Y, 128, 128, null);
        } catch(NullPointerException npe) {
            System.err.println(ErrorList.ERR_NPE.message);
        }
    }



    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        drawTerrain(dad.level == 0 ? Game.map : Game.houseMap, g);
    }
}
