package com.vojat.inputs;

import java.awt.event.KeyListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.KeyEvent;

import com.vojat.Data.JSONEditor;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Game;
import com.vojat.garden.GamePanel;
import com.vojat.garden.Player;
import com.vojat.menu.MenuPanel;
import com.vojat.menu.Settings;
import com.vojat.menu.Window;


/*
 * Class implementing KeyListener interface
 * Interface must have its every method overriden
 * When extending an abstract class, you don't have to override every method
 */
public class KeyboardInput implements KeyListener {

    /*
     * ----------------------------------------------------------------
     * Keyboard input variables
     * ----------------------------------------------------------------
     */

    private Player dad;                                                                     // The player
    private GamePanel gamePanel;                                                            // Game panel
    private boolean up = true, down = true, left = true, right = true;                      // Determines wheather the player's texture should be repainted in a specific direction                                                             
    private Window window;                                                                  // Window on which the game panel is
    private Settings settings;                                                              // Settings panel used for the change key response
    private JButton button;                                                                 // Button for the settings change key
    private JLabel label;                                                                   // The label to be repainted after the key is changed
    private JSONEditor jEditor;                                                             // JSON Editor for getting the control keys

    /*
     * ----------------------------------------------------------------
     * Constructor used by the game panel
     * ----------------------------------------------------------------
     */

    public KeyboardInput(GamePanel gamePanel, Player dad, Window window) {
        this.gamePanel = gamePanel;
        this.dad = dad;
        this.window = window;

        try {

            // Sets up the JSON Objects from the controls JSON
            jEditor = new JSONEditor("src/com/vojat/Data/Controls.json");
            jEditor.readFile();
            jEditor.readData(jEditor.JSONObjects.get(1), "up");
            jEditor.readData(jEditor.JSONObjects.get(1), "down");
        } catch (FileNotFoundException e) {
            System.err.println(ErrorList.ERR_404.message);
        }
    }

    /*
     * ----------------------------------------------------------------
     * Constructor used by the settings panel
     * ----------------------------------------------------------------
     */

    public KeyboardInput(Settings settings, JButton button, JLabel label) {
        this.settings = settings;
        this.button = button;
        this.label = label;
    }

    /*
     * ----------------------------------------------------------------
     * In-game movement methods
     * ----------------------------------------------------------------
     */

    @Override
    public void keyPressed(KeyEvent e) {
        if (dad == null) return;
        if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "up"))) {
            if (up) {
                dad.setTexture("res/Pics/Dad_Texture_B.png");
                up = false;
            }
            dad.VECTORY = -dad.speed;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "down"))) {
            if (down) {
                dad.setTexture("res/Pics/Dad_Texture_F.png");
                down = false;
            }
            dad.VECTORY = dad.speed;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "left"))) {
            if (left) {
                dad.setTexture("res/Pics/Dad_Texture_L.png");
                left = false;
            }
            dad.VECTORX = -dad.speed;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "right"))) {
            if (right) {
                dad.setTexture("res/Pics/Dad_Texture_R.png");
                right = false;
            }
            dad.VECTORX = dad.speed;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(0), "exit"))) {
            Game.killGame();
            window.setElements(new MenuPanel(1920, 1080, window));
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(0), "pause"))) {
            Game.pauseGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (dad == null) return;
        if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "up"))) {
            up = true;
            dad.VECTORY = .0;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "down"))) {
            down = true;
            dad.VECTORY = .0;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "left"))) {
            left = true;
            dad.VECTORX = .0;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "right"))) {
            right = true;
            dad.VECTORX = .0;
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "next"))) {
            if (dad.selectedItem+1 < dad.inventory.size()) {
                dad.selectedItem++;
            } else {
                dad.selectedItem = 0;
            }
            gamePanel.inventoryPanel.repaintItem(dad);
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "previous"))) {
            if (dad.selectedItem > 0) {
                dad.selectedItem--;
            } else {
                dad.selectedItem = (byte) (dad.inventory.size() - 1);
            }
            gamePanel.inventoryPanel.repaintItem(dad);
        } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "open"))) {
            gamePanel.changeVisibility(gamePanel.fullInv);
        }
        if (dad.VECTORX != .0 || dad.VECTORY != .0) {
            if (dad.VECTORX == .0) {
                if (dad.VECTORY > 0) {
                    retexture("down");
                } else {
                    retexture("up");
                }
            } else if (dad.VECTORY == .0) {
                if (dad.VECTORX > 0) {
                    retexture("right");
                } else {
                    retexture("left");
                }
            }
        }
    }

    /*
     * ----------------------------------------------------------------
     * Determines the new controls key
     * ----------------------------------------------------------------
     */

    @Override
    public void keyTyped(KeyEvent e) {
        if (settings != null) {
            settings.setKey(e.getKeyChar(), button, this, label);
        }
    }

    /*
     * ----------------------------------------------------------------
     * Changes the player texture
     * ----------------------------------------------------------------
     */

    private void retexture(String direction) {
        switch (direction) {
            case "up":
                dad.setTexture("res/Pics/Dad_Texture_B.png");
                break;

            case "down":
                dad.setTexture("res/Pics/Dad_Texture_F.png");
                break;

            case "left":
                dad.setTexture("res/Pics/Dad_Texture_L.png");
                break;

            case "right":
                dad.setTexture("res/Pics/Dad_Texture_R.png");
                break;
        }
    }
}
