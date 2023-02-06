package com.vojat.garden;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vojat.Enums.*;
import com.vojat.inputs.KeyboardInput;
import com.vojat.inputs.MouseInput;

public class GamePanel extends JPanel{
    private ArrayList<Flower> flowers = new ArrayList<>();
    Player dad = new Player(this, 200, 200);
    public byte[][] map = new byte[8][15];      // [Y][X] coords  -> Now it's a total of 120 spots to place a flower
    public String[] textures = {"res/Water.png", "res/Red_Tulip.png", "res/Blue_Rose.png"};      // Array of texture paths, mush be bigger by one then number of flowers
    public InventoryPanel inventoryPanel;

    public GamePanel(int windowWidth, int windowHeight) { // width == window width ; height == window height
        
        dad.setLimit(windowWidth, windowHeight);
        setFocusable(true);     // Sets the JPanel focusable, it is later packed into the JFrame

        {       // Passing information for the game window, visible by the pack method
            setPanelSize(windowWidth, windowHeight-50);
            setBackground();
            setBorder(new LineBorder(Color.BLACK));
        }

        {       // Adding the listeners
            addKeyListener(new KeyboardInput(this, dad));
            addMouseListener(new MouseInput(dad, this));
        }
    }

    protected void setIPanel(InventoryPanel iPanel) {       // Sets the inventory panel field (just for the repaint method to be functional in the listener)
        this.inventoryPanel = iPanel;
    }

    protected void setPanelSize(int width, int height) {
        setPreferredSize(new Dimension(width, height));
    }

    private void setBackground() {
        setBackground(new Color(84, 194, 4));
    }

    public void summonFlower(Flower flower) {
        flowers.add(flower);
    }

    public void waterFlower(Flower flower, int positionX, int positionY) {
        for (Flower plant : flowers) {
            if (plant.IN_MAP_X == positionX && plant.IN_MAP_Y == positionY) {
                if (plant.STATUS.equals("Alive")) {
                    plant.TIME_TO_DIE = System.currentTimeMillis() + Values.TODIE.value;
                    plant.TIME_TO_DISSAPEAR = System.currentTimeMillis() + Values.TODISSAPEAR.value;
                    plant.CURRENT_TEXTURE = plant.setTexture(textures[plant.FLOWER_TEXTURE_NUMBER]);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try{
            for (int i=0; i<flowers.size(); i++) {      // Drawing all the placed plants the old school way
                Flower plant = flowers.get(i);
                if (plant.TIME_TO_DISSAPEAR >= System.currentTimeMillis()) {
                    if (plant.TIME_TO_DIE <= System.currentTimeMillis()) {
                        plant.CURRENT_TEXTURE = plant.setTexture("res/Cat_Texture_F.png");
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

            g.drawImage(dad.currentTexture, dad.LOCATION_X, dad.LOCATION_Y, 128, 128, null);       // The Dad Image has a resolution of 32 x 32 pixels
        } catch(NullPointerException npe) {
            System.err.println(ErrorList.ERR_NPE.message);
        }
    }

    public void saveGame() {;}

    public void loadGame(byte[][] map, HashMap<Integer, String> decoder) {;}
}
