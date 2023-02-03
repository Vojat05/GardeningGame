package com.vojat.garden;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JPanel;

import com.vojat.inputs.KeyboardInput;
import com.vojat.inputs.MouseInput;
import com.vojat.Errors.ErrorList;

public class GamePanel extends JPanel{
    private ArrayList<Flower> flowers = new ArrayList<>();
    Player dad = new Player(this, 200, 200);
    public byte[][] map = new byte[8][15];      // [Y][X] coords  -> Now it's a total of 120 spots to place a flower
    public HashMap<Integer, String> decoder = new HashMap<Integer, String>();

    public GamePanel(int windowWidth, int windowHeight) { // width == window width ; height == window height

        {       // Filling up the decoder info
            decoder.put(0, null);
            decoder.put(1, "Flower");
        }

        dad.setLimit(windowWidth, windowHeight);
        setFocusable(true);     // Sets the JPanel focusable, it is later packed into the JFrame

        {       // Passing information for the game window, visible by the pack method
            setPanelSize(windowWidth, windowHeight);
            setBackground();
        }

        {       // Adding the listeners
            addKeyListener(new KeyboardInput(dad));
            addMouseListener(new MouseInput(dad, this));
        }
    }

    protected void setPanelSize(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
    }

    private void setBackground() {
        setBackground(new Color(84, 194, 4));
    }

    public void summonFlower(Flower flower) {
        flowers.add(flower);
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

    public void saveProgress() {;}

    public void loadProgress(byte[][] map, HashMap<Integer, String> decoder) {;}
}
