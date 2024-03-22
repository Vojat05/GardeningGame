package com.vojat.inputs;

import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.KeyEvent;

import com.vojat.Main;
import com.vojat.Data.JSONEditor;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Console;
import com.vojat.garden.Game;
import com.vojat.garden.GamePanel;
import com.vojat.garden.Player;
import com.vojat.menu.Settings;

/*
 * Class implementing KeyListener interface
 * Interface must have its every method overriden
 * When extending an abstract class, you don't have to override every method
 */

public class KeyboardInput implements KeyListener {

    /*
     * --------------------------------------------------------------------------------
     * Keyboard input variables
     * --------------------------------------------------------------------------------
     */

    private Player dad;                                                                     // The player
    private GamePanel gamePanel;                                                            // Game panel
    private boolean up = false, down = false, left = false, right = false;                      // Determines wheather the player is moving in set direction
    private Settings settings;                                                              // Settings panel used for the change key response
    private JButton button;                                                                 // Button for the settings change key
    private JLabel label;                                                                   // The label to be repainted after the key is changed
    private JSONEditor jEditor;                                                             // JSON Editor for getting the control keys
    private HashMap<String, String> keyMap = new HashMap<>();                               // A hash map of the currently binded keys

    /*
     * --------------------------------------------------------------------------------
     * Constructor used by the game panel
     * --------------------------------------------------------------------------------
     */

    public KeyboardInput(GamePanel gamePanel, Player dad) {

        this.gamePanel = gamePanel;
        this.dad = dad;

        try {

            // Sets up the JSON Objects from the controls JSON
            jEditor = new JSONEditor("../com/vojat/Data/Controls.json");
            jEditor.readFile(true);
            loadKeys();

        } catch (FileNotFoundException e) {

            System.err.println(ErrorList.ERR_404.message);
            Game.error("File not found", 3);

        }
    }

    /*
     * --------------------------------------------------------------------------------
     * Constructor used by the settings panel
     * --------------------------------------------------------------------------------
     */

    public KeyboardInput(Settings settings, JButton button, JLabel label) {

        this.settings = settings;
        this.button = button;
        this.label = label;

    }

    /*
     * --------------------------------------------------------------------------------
     * In-game movement methods
     * --------------------------------------------------------------------------------
     */

    @Override
    public void keyPressed(KeyEvent e) {

        if (settings != null) settings.setKey(KeyEvent.getKeyText(e.getKeyCode()), button, this, label);
        if (dad == null) return;

        // Toggle the console
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            Console.toggleShow();
            return;
        }

        // Entering the console command
        if (Console.isVisible()) {

            // Execute the command if ENTER is pressed
            if (e.getKeyCode() == KeyEvent.VK_ENTER && Console.commandPrompt.contains(";")) {
                Console.addCommand(Console.commandPrompt);
                return;
            }

            // Delete the last character if BACKSPACE is pressed
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && Console.commandPrompt.length() > 0) {
                Console.commandPrompt = Console.commandPrompt.substring(0, Console.commandPrompt.length() - 1);
                return;
            }

            // ASCII check if the entered value is in the allowed range
            char c = e.getKeyChar();
            if ((c == 0x20 || c == 0x3B) || (c >= 0x30 && c <= 0x39) || (c >= 0x41 && c <= 0x5A) || (c >= 0x61 && c <= 0x7A)) Console.commandPrompt += c;
            
            return;
        }

        // Quit if player has 0 HP
        if (dad.HP == 0) return;

        // Exit the current alert or other window
        if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("exit"))) {

            if (gamePanel.saveMenuOpen) {

                gamePanel.hideSaveMenu();
                dad.LOCATION_X = 208;
                dad.LOCATION_Y = 120;
                Game.alertMessage = "None";
                Game.togglePauseGame();

            } else if (gamePanel.skinMenuOpen) {

                gamePanel.skinMenuOpen = false;
                Game.togglePauseGame();

            } else if (!Game.pause && !Game.alert) {

                Game.togglePauseGame();
                Game.alert("Are you sure you want to quit?");

            } else if (Game.pause && Game.alert) {

                Game.togglePauseGame();
                Game.alert = false;

            } else if (Game.pause && !Game.alert) Game.alert("Are you sure you want to quit?");

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("pause"))) {

            if (!Game.alert) Game.togglePauseGame();

        }

        // Exit couch via using a shift key
        if (e.getKeyCode() == KeyEvent.VK_SHIFT && !gamePanel.dad.canMove()) {

            if ((gamePanel.dad.reachLevel & 0xf) == 0) return;

            if (gamePanel.dad.LOCATION_X == 894) {
                            
                gamePanel.dad.LOCATION_X = 825;
                gamePanel.dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_L" + gamePanel.dad.getTextureModifier() + ".png");
            
            }
            else {
                
                gamePanel.dad.LOCATION_Y = 335;
                gamePanel.dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + gamePanel.dad.getTextureModifier() + ".png");
            
            }

            up = false;
            down = false;
            left = false;
            right = false;

            gamePanel.dad.setMove(true);
        }

        if (!dad.canMove()) return;

        if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("up"))) {

            if (!up) {

                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_B" + dad.getTextureModifier() + ".png");
                up = true;
                
            }
            
            dad.VECTORY = -dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("down"))) {

            if (!down) {

                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + dad.getTextureModifier() + ".png");
                down = true;
                
            }
            
            dad.VECTORY = dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("left"))) {

            if (!left) {

                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_L" + dad.getTextureModifier() + ".png");
                left = true;
                
            }
            
            dad.VECTORX = -dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("right"))) {

            if (!right) {

                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_R" + dad.getTextureModifier() + ".png");
                right = true;
                
            }
            
            dad.VECTORX = dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("open")) && !Game.pause) gamePanel.changeVisibility(gamePanel.fullInv);
        else {

            for (byte i=0; i<10; i++) {

                if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("slot" + i))) {

                    dad.selectedItem = i;
                    if (gamePanel.hasFocus()) gamePanel.inventoryPanel.repaint();

                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (dad == null || dad.HP == 0 || !dad.canMove()) return;
        if (!up && !down && !left && !right) { gamePanel.dad.VECTORX = 0; gamePanel.dad.VECTORY = 0; }
        if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("up"))) {

            up = false;
            if (!down) dad.VECTORY = .0;
            else if (dad.VECTORY < 0) dad.VECTORY = dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("down"))) {

            down = false;
            if (!up) dad.VECTORY = .0;
            else if (dad.VECTORY > 0) dad.VECTORY = -dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("left"))) {

            left = false;
            if (!right) dad.VECTORX = .0;
            else if (dad.VECTORX < 0) dad.VECTORX = dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(keyMap.get("right"))) {

            right = false;
            if (!left) dad.VECTORX = .0;
            else if (dad.VECTORX > 0) dad.VECTORX = -dad.speed;

        }

        if (dad.VECTORX != .0 || dad.VECTORY != .0) {

            if (dad.VECTORX == .0) {

                if (dad.VECTORY > 0) retexture("down"); 
                else retexture("up");

            } else if (dad.VECTORY == .0) {

                if (dad.VECTORX > 0) retexture("right"); 
                else retexture("left");

            }
        }
    }

    /*
     * --------------------------------------------------------------------------------
     * Determines the new controls key and saves the information into memory
     * --------------------------------------------------------------------------------
     */

    @Override
    public void keyTyped(KeyEvent e) {;}

    public void loadKeys() throws FileNotFoundException {

        JSONEditor jse = new JSONEditor("../com/vojat/Data/Controls.json");
        jse.readFile(true);
        if (Main.debug) System.out.println("DEBUG:\nJSON Objects NO.: " + jse.getNumberOfJSONObjects() + "\n");

        for (int i=0; i<jse.getNumberOfJSONObjects(); i++) {

            String[][] data = jse.JSONObjects.get(i).getData();

            for (int j=0; j<jse.JSONObjects.get(i).getNumberOfValues(); j++) {
    
                keyMap.put(data[j][0], data[j][1]);
                if (Main.debug) System.out.println("I: " + i + "\nJ: " + j + "\nObject: " + jse.JSONObjects.get(i).NAME + "\nValues number: " + jse.JSONObjects.get(i).getNumberOfValues() + "\nKey: " + data[j][0] + " | Value: " + data[j][1] + "\n");

            }
        }
    }

    /*
     * --------------------------------------------------------------------------------
     * Changes the player texture
     * --------------------------------------------------------------------------------
     */

    private void retexture(String direction) {

        switch (direction) {

            case "up":
                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_B" + dad.getTextureModifier() + ".png");
                break;

            case "down":
                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + dad.getTextureModifier() + ".png");
                break;

            case "left":
                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_L" + dad.getTextureModifier() + ".png");
                break;

            case "right":
                dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_R" + dad.getTextureModifier() + ".png");
                break;
                
        }
    }

    public void resetMovement() {

        up = false;
        down = false;
        left = false;
        right = false;
        
    }
}
