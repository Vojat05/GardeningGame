package com.vojat.menu;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.vojat.garden.MainPanel;
import com.vojat.garden.Player;

public class Window extends JFrame {
    private MainPanel mainPanel;
    private Player dad;
    private ArrayList<JComponent> components = new ArrayList<JComponent>();

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
    }

    @Override
    public void validate() {        // dad & mainPanel are always null !!
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
