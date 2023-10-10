package com.vojat.inputs;

import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.KeyEvent;

import com.vojat.Data.JSONEditor;
import com.vojat.Enums.ErrorList;
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
    private boolean up = true, down = true, left = true, right = true;                      // Determines wheather the player's texture should be repainted in a specific direction
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
            jEditor = new JSONEditor("src/com/vojat/Data/Controls.json");
            jEditor.readFile(true);
            jEditor.readData(jEditor.JSONObjects.get(1), "up");
            jEditor.readData(jEditor.JSONObjects.get(1), "down");

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

        if (dad == null || dad.HP == 0) return;

        if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(0), "exit"))) {

            if (gamePanel.saveMenuOpen) {

                gamePanel.hideSaveMenu();
                dad.LOCATION_X = 208;
                dad.LOCATION_Y = 120;
                Game.alertMessage = "None";
                Game.pauseGame();

            } else if (!Game.pause && !Game.alert) {

                Game.pauseGame();
                Game.alert("Are you sure you want to quit?", gamePanel);

            } else if (Game.pause && Game.alert) {

                Game.pauseGame();
                Game.alert = false;

            } else if (Game.pause && !Game.alert) Game.alert("Are you sure you want to quit?", gamePanel);

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(0), "pause"))) {

            if (!Game.alert) Game.pauseGame();

        }

        // Exit couch via using a shift key
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {

            System.out.println("Shift pressed");

            if (gamePanel.dad.level == 0) return;
            if (gamePanel.dad.canMove()) return;

            if (gamePanel.dad.LOCATION_X == 894) {
                            
                gamePanel.dad.LOCATION_X = 825;
                gamePanel.dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_L" + gamePanel.dad.getTextureModifier() + ".png");
            
            }
            else {
                
                gamePanel.dad.LOCATION_Y = 335;
                gamePanel.dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + gamePanel.dad.getTextureModifier() + ".png");
            
            }

            gamePanel.dad.setMove(true);
        }

        if (!dad.canMove()) return;

        if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "up"))) {

            if (up) {

                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_B" + dad.getTextureModifier() + ".png");
                up = false;
                
            }
            
            dad.VECTORY = -dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "down"))) {

            if (down) {

                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + dad.getTextureModifier() + ".png");
                down = false;
                
            }
            
            dad.VECTORY = dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "left"))) {

            if (left) {

                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_L" + dad.getTextureModifier() + ".png");
                left = false;
                
            }
            
            dad.VECTORX = -dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "right"))) {

            if (right) {

                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_R" + dad.getTextureModifier() + ".png");
                right = false;
                
            }
            
            dad.VECTORX = dad.speed;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(KeyEvent.getKeyText(e.getKeyCode()));

        if (dad == null || dad.HP == 0) return;
        if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "up"))) {

            up = true;
            if (down) dad.VECTORY = .0;
            else dad.VECTORY = dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "down"))) {

            down = true;
            if (up) dad.VECTORY = .0;
            else dad.VECTORY = -dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "left"))) {

            left = true;
            if (right) dad.VECTORX = .0;
            else dad.VECTORX = dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "right"))) {

            right = true;
            if (left) dad.VECTORX = .0;
            else dad.VECTORX = -dad.speed;

        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot0"))) dad.selectedItem = 0;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot1"))) dad.selectedItem = 1;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot2"))) dad.selectedItem = 2;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot3"))) dad.selectedItem = 3;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot4"))) dad.selectedItem = 4;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot5"))) dad.selectedItem = 5;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot6"))) dad.selectedItem = 6;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot7"))) dad.selectedItem = 7;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot8"))) dad.selectedItem = 8;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "slot9"))) dad.selectedItem = 9;
        else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "open")) && !Game.pause) gamePanel.changeVisibility(gamePanel.fullInv);

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
    public void keyTyped(KeyEvent e) {

        if (settings != null) settings.setKey(e.getKeyChar(), button, this, label);

    }
/*
    public void loadKeys() throws FileNotFoundException {

        JSONEditor jse = new JSONEditor("src/com/vojat/Data/Controls.json");

        for (int i=0; i<jse.JSONObjects.get(0).getNumberOfValues(); i++) {

            keyMap.put(null, null)
        }
    }
*/
    /*
     * --------------------------------------------------------------------------------
     * Changes the player texture
     * --------------------------------------------------------------------------------
     */

    private void retexture(String direction) {

        switch (direction) {

            case "up":
                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_B" + dad.getTextureModifier() + ".png");
                break;

            case "down":
                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_F" + dad.getTextureModifier() + ".png");
                break;

            case "left":
                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_L" + dad.getTextureModifier() + ".png");
                break;

            case "right":
                dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_R" + dad.getTextureModifier() + ".png");
                break;
                
        }
    }
}
