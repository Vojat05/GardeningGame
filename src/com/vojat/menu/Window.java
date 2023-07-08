package com.vojat.menu;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.vojat.garden.MainPanel;
import com.vojat.garden.Player;

public class Window extends JFrame {

    /*
     * --------------------------------------------------------------------------------
     * Window variables
     * --------------------------------------------------------------------------------
     */

    private MainPanel mainPanel;                                                            // The main menu panel
    private Player dad;                                                                     // The player character
    private ArrayList<JComponent> components = new ArrayList<JComponent>();                 // Arraylist of all of it's components

    /*
     * --------------------------------------------------------------------------------
     * Window constructor to setup the window & window change methods
     * --------------------------------------------------------------------------------
     */

    public Window() {
        setTitle("Dad The Gardener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void setElements(JComponent arg) {
        for (JComponent component : components) {
            remove(component);
        }
        components.clear();

        add(arg);
        components.add(arg);
        pack();
        setVisible(true);
        setSize(new Dimension(1920, 1080));
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
