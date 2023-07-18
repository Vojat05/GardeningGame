package com.vojat.menu;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.vojat.Main;
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

        pack();
        setSize(new Dimension(Main.sizeX, Main.sizeY));

        if (width == Main.sizeX && height == Main.sizeY) setExtendedState(JFrame.MAXIMIZED_BOTH);

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
}
