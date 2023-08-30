package com.vojat;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.vojat.menu.MenuPanel;
import com.vojat.menu.Window;

public class Main {
    public static final boolean debug = false;
    public static final int sizeX = 1920, sizeY = 1080;
    public static Window window;
    public static void main(String[] args) {

        GraphicsDevice gDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        Window frame = new Window(gDevice.getDisplayMode().getWidth(), gDevice.getDisplayMode().getHeight());
        window = frame;
        new MenuPanel(window);

    }
}
