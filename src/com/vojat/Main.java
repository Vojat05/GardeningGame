package com.vojat;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.vojat.Data.JSONEditor;
import com.vojat.garden.Game;
import com.vojat.garden.GamePanel;
import com.vojat.menu.MenuPanel;
import com.vojat.menu.Window;

public class Main {
    public static boolean debug = false, overrideResolution = true, fullscreen = true, tutorial, donate = true;
    public static short width, height, sizeX, sizeY;
    public static float musicVolume;
    public static Window window;
    private static int[] resolution;
    public static void main(String[] args) {

        /*
         * --------------------------------------------------------------------------------
         * System monitor resolution check
         * --------------------------------------------------------------------------------
         */

        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        sizeX = (short) sSize.getWidth();
        sizeY = (short) sSize.getHeight();

        if (sizeX < 1920 || sizeY < 1080) {

            buildError("Your display resolution is too low, FullHD ( 1920x1080 ) is minimum");

            System.out.println("Your display resolution is too low // FullHD ( 1920x1080 ) is minimum");
            return;
            
        }

        /*
         * --------------------------------------------------------------------------------
         * Getting the essential game settings from the config.json file
         * --------------------------------------------------------------------------------
         */

        String configResRaw = "";
        try {

            JSONEditor jsonEditor = new JSONEditor("../../res/Config.json");
            
            Main.debug = Boolean.parseBoolean(jsonEditor.readData("Debugging"));
            Main.overrideResolution = Boolean.parseBoolean(jsonEditor.readData("Override-Resolution-Bool"));
            Main.fullscreen = Boolean.parseBoolean(jsonEditor.readData("FullScreen"));
            Main.musicVolume = Float.parseFloat(jsonEditor.readData("Music-Volume"));
            Main.tutorial = Boolean.parseBoolean(jsonEditor.readData("Show-Tutorial"));
            Main.donate = Boolean.parseBoolean(jsonEditor.readData("Donate"));

            if (overrideResolution) configResRaw = jsonEditor.readData("Override-Resolution");

            Game.langFileName = jsonEditor.readData("Language");
            Game.texturePack = jsonEditor.readData("Texture-Pack");
            Game.version = jsonEditor.readData("Version");
            Game.FPS_SET = Byte.parseByte(jsonEditor.readData("FPS"));
            Game.setDayLasts(Short.parseShort(jsonEditor.readData("Day-Lasts")));
            Game.setNightLasts(Short.parseShort(jsonEditor.readData("Night-Lasts")));
            Game.dayNightTransitionSpeed = Double.parseDouble(jsonEditor.readData("Cycle-Transition-Value"));
            Game.volumeTransitionSpeed = Float.parseFloat(jsonEditor.readData("Volume-Transition-Value"));
            GamePanel.overlay = Boolean.parseBoolean(jsonEditor.readData("Enable-Overlay"));

            Game.font = Font.createFont(Font.TRUETYPE_FONT, new File("../../res/" + Game.texturePack + "/Fonts/customFont.ttf"));

        } catch (FontFormatException | IOException e) {
            
            e.printStackTrace();
            buildError("Config.json file not found");

        }

        // Plugging in the resolution calculated values
        if (overrideResolution) {
            String widthString = "";
            String heightString = "";
            boolean write = true;

            for (int i=0; i<configResRaw.length(); i++) {
                if (configResRaw.charAt(i) == 'x') write = write ? false : true;
                else if (write) widthString += configResRaw.charAt(i);
                else heightString += configResRaw.charAt(i);
            }

            width = Short.parseShort(widthString);
            height = Short.parseShort(heightString);
        } else {
            width = sizeX;
            height = sizeY;
        }

        System.out.println("Width: " + width + "\nHeight: " + height);

        // Calculate the resolution to perfectly fit the game map
        resolution = Window.calculateResolution(width, height, 16, 9);

        window = new Window(resolution[0], resolution[1]);
        new MenuPanel(window);

    }

    private static void buildError(String message) {

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
