package com.vojat.garden;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JPanel;

import com.vojat.inputs.KeyboardInput;
import com.vojat.inputs.MouseInput;
import com.vojat.Errors.ErrorList;

public class GamePanel extends JPanel{
    private ArrayList<Flower> flowers = new ArrayList<>();
    Player dad = new Player(this, 200, 200);

    public GamePanel(int windowWidth, int windowHeight) { // width == window width ; height == window height
        dad.setLimit(windowWidth, windowHeight);
        setFocusable(true);     // Sets the JPanel focusable, it is later packed into the JFrame

        {       // Passing information for the game window, visible by the pack method
            setPanelSize(windowWidth, windowHeight);
            setBackground();
        }

        {       // Adding the listeners
            addKeyListener(new KeyboardInput(dad));
            addMouseListener(new MouseInput(dad));
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
            for (Flower plant : flowers) {      // Drawing all the placed plants
                g.drawImage(plant.CURRENT_TEXTURE, plant.LOCATION_X, plant.LOCATION_Y, 128, 128, null);
            }

            g.drawImage(dad.currentTexture, dad.LOCATION_X, dad.LOCATION_Y, 128, 128, null);       // The Dad Image has a resolution of 32 x 32 pixels
        } catch(NullPointerException npe) {
            System.err.println(ErrorList.ERR_NPE.message);
        }
    }
}
