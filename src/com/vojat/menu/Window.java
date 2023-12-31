package com.vojat.menu;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.vojat.Main;
import com.vojat.garden.Game;
import com.vojat.garden.MainPanel;
import com.vojat.garden.Player;

public class Window extends JFrame {

    /*
     * --------------------------------------------------------------------------------
     * Window variables
     * --------------------------------------------------------------------------------
     */

    public static int width, height;                                                        // The device screen width and height in pixels
    private MainPanel mainPanel;                                                            // The main menu panel
    private Player dad;                                                                     // The player character
    private ArrayList<JComponent> components = new ArrayList<JComponent>();                 // Arraylist of all of it's components

    /*
     * --------------------------------------------------------------------------------
     * Window constructor to setup the window & window change methods
     * --------------------------------------------------------------------------------
     */

    public Window(int screenWidth, int screenHeight) {
        width = screenWidth;
        height = screenHeight;
        
        setTitle("Dad The Gardener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Icons/tulip.png").getImage());

        pack();
        setSize(new Dimension(Main.width, Main.height));

        setResizable(false);
    }

    public void setElements(JComponent arg) {
        for (JComponent component : components) {
            remove(component);
        }
        components.clear();

        add(arg);
        components.add(arg);
        setVisible(true);
    }

    // dad & mainPanel are always null !!
    @Override
    public void validate() {
        super.validate();
        if (dad != null & mainPanel != null) {
            mainPanel.setPanelSize(getSize().width, getSize().height);
            dad.setLimit(getSize().width, getSize().height);

            if (dad.LOCATION_X > Player.windowLimitX-120) {
                dad.LOCATION_X = Player.windowLimitX-130;
            }

            if (dad.LOCATION_Y > Player.windowLimitY-120) {
                dad.LOCATION_Y = Player.windowLimitY-150;
            }
        }
    }

    /**
     * Recursive function to calculate the screen resolution to match a specified ratio.
     * @param width The starting screen width input.
     * @param height The starting screen height input.
     * @param ratioX From <code>X : Y</code> the value of <code>X</code>.
     * @param ratioY From <code>X : Y</code> the value of <code>Y</code>.
     * @return An integer array containing <code>{ width, height }</code>.
     */
    public static int[] calculateResolution(int width, int height, int ratioX, int ratioY) {
        // Fail break condition
        if (width == 0 || height == 0) System.exit(1);

        // Second break condition
        if (width % ratioX == 0 && height % ratioY == 0) {
            int[] resolution = new int[2];
            resolution[0] = width;
            resolution[1] = height;
            return resolution;
        }

        return calculateResolution(width % ratioX == 0 ? width : (width - 1), height % ratioY == 0 ? height : (height - 1), ratioX, ratioY);
    }
}
