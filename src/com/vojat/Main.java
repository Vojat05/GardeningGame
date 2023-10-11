package com.vojat;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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

            error("Your display resolution is too low, FullHD ( 1920x1080 ) is minimum");

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
            System.out.println("Language: " + Game.langFileName);
            Game.texturePack = jsonEditor.readData("Texture-Pack");
            System.out.println("Texture-Pack: " + Game.texturePack);
            Game.version = jsonEditor.readData("Version");
            System.out.println("Version: " + Game.version);
            Game.FPS_SET = Byte.parseByte(jsonEditor.readData("FPS"));
            System.out.println("FPS: " + Game.FPS_SET);
            Game.setDayLasts(Integer.parseInt(jsonEditor.readData("Day-Lasts")));
            System.out.println("Day-Lasts: " + Game.dayLasts());
            Game.setNightLasts(Integer.parseInt(jsonEditor.readData("Night-Lasts")));
            System.out.println("Night-Lasts: " + Game.nightLasts());
            Game.dayNightTransitionSpeed = Double.parseDouble(jsonEditor.readData("Cycle-Transition-Value"));
            System.out.println("Cycle-Transition-Value: " + Game.dayNightTransitionSpeed);
            Game.volumeTransitionSpeed = Float.parseFloat(jsonEditor.readData("Volume-Transition-Value"));
            System.out.println("Volume-Transition-Value: " + Game.volumeTransitionSpeed);

            Game.font = Font.createFont(Font.TRUETYPE_FONT, new File("res/" + Game.texturePack + "/Fonts/customFont.ttf"));

        } catch (FontFormatException | IOException e) {
            
            e.printStackTrace();
            error("Config.json file not found");

        }

        Window frame = new Window(sizeX, sizeY);
        window = frame;
        new MenuPanel(window);

    }

    private static void error(String message) {

        JFrame lowResFrame = new JFrame("ERROR::");
        lowResFrame.setSize(500, 300);
        lowResFrame.getContentPane().setBackground(new Color(48, 48, 48));
        lowResFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel errMsg1 = new JLabel(message, SwingConstants.CENTER);
        errMsg1.setForeground(Color.RED);
        errMsg1.setFont(new Font("Helvetica", 0, 15));

        lowResFrame.add(errMsg1);
        lowResFrame.setLocationRelativeTo(null);
        lowResFrame.setVisible(true);

    }
}
