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
    public static boolean debug = false;
    public static int width = 1920, height = 1080;
    public static int sizeX, sizeY;
    public static boolean maximize = false;
    public static Window window;
    private static int[] resolution;
    public static void main(String[] args) {

        /*
         * --------------------------------------------------------------------------------
         * System monitor resolution check
         * --------------------------------------------------------------------------------
         */

        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        sizeX = (int) sSize.getWidth();
        sizeY = (int) sSize.getHeight();

        // Calculate the resolution to perfectly fit the game map
        resolution = calculateResolution(width, height, 15, 8);

        if (sizeX < 1920 || sizeY < 1080) {

            buildError("Your display resolution is too low, FullHD ( 1920x1080 ) is minimum");

            System.out.println("Your display resolution is too low // FullHD ( 1920x1080 ) is minimum");
            return;
            
        } else if (sizeX == 1920 && sizeY == 1080) maximize = true;

        /*
         * --------------------------------------------------------------------------------
         * Getting the essential game settings from the config file
         * --------------------------------------------------------------------------------
         */

        try {

            JSONEditor jsonEditor = new JSONEditor("../../res/Config.json");
            
            Main.debug = Boolean.parseBoolean(jsonEditor.readData("Debugging"));

            Game.firstStart = Boolean.parseBoolean(jsonEditor.readData("Show-Tutorial"));
            Game.langFileName = jsonEditor.readData("Language");
            Game.texturePack = jsonEditor.readData("Texture-Pack");
            Game.version = jsonEditor.readData("Version");
            Game.FPS_SET = Byte.parseByte(jsonEditor.readData("FPS"));
            Game.setDayLasts(Integer.parseInt(jsonEditor.readData("Day-Lasts")));
            Game.setNightLasts(Integer.parseInt(jsonEditor.readData("Night-Lasts")));
            Game.dayNightTransitionSpeed = Double.parseDouble(jsonEditor.readData("Cycle-Transition-Value"));
            Game.volumeTransitionSpeed = Float.parseFloat(jsonEditor.readData("Volume-Transition-Value"));
            GamePanel.overlay = Boolean.parseBoolean(jsonEditor.readData("Enable-Overlay"));

            Game.font = Font.createFont(Font.TRUETYPE_FONT, new File("../../res/" + Game.texturePack + "/Fonts/customFont.ttf"));

        } catch (FontFormatException | IOException e) {
            
            e.printStackTrace();
            buildError("Config.json file not found");

        }

        Window frame = new Window(resolution[0], resolution[1]);
        window = frame;
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

    /**
     * Recursive function to calculate the screen resolution to match a specified ratio.
     * @param width The starting screen width input.
     * @param height The starting screen height input.
     * @param ratioX From <code>X : Y</code> the value of <code>X</code>.
     * @param ratioY From <code>X : Y</code> the value of <code>Y</code>.
     * @return An integer array containing <code>{ width, height }</code>.
     */
    public static int[] calculateResolution(int width, int height, int ratioX, int ratioY) {
        // The break condition
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
