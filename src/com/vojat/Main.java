package com.vojat;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import com.vojat.Data.JSONEditor;
import com.vojat.garden.Game;
import com.vojat.menu.MenuPanel;
import com.vojat.menu.Window;

public class Main {
    public static final boolean debug = false;
    public static final int sizeX = 1920, sizeY = 1080;
    public static boolean maximize = false;
    public static Window window;
    public static void main(String[] args) {

        /*
         * --------------------------------------------------------------------------------
         * System monitor resolution check
         * --------------------------------------------------------------------------------
         */

        GraphicsDevice gDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gDevice.getDisplayMode().getWidth() < sizeX || gDevice.getDisplayMode().getHeight() < sizeY) {

            System.out.println("Your display resolution is too low // FullHD ( 1920x1080 ) is minimum");
            return;
            
        } else if (gDevice.getDisplayMode().getWidth() == 1920 && gDevice.getDisplayMode().getHeight() == 1080) maximize = true;

        /*
         * --------------------------------------------------------------------------------
         * Getting the essential game settings from the config file
         * --------------------------------------------------------------------------------
         */

        try {

            JSONEditor jsonEditor = new JSONEditor("res/Config.json");
            Game.langFileName = jsonEditor.readData("Language");
            Game.texturePack = jsonEditor.readData("Texture-Pack");
            Game.version = jsonEditor.readData("Version");

            Game.font = Font.createFont(Font.TRUETYPE_FONT, new File("res/" + Game.texturePack + "/Fonts/customFont.ttf"));

        } catch (FontFormatException | IOException e) {
            
            e.printStackTrace();

        }

        Window frame = new Window(sizeX, sizeY);
        window = frame;
        new MenuPanel(window);

    }
}
