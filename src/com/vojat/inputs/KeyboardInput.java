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

    private Player dad;
    private GamePanel gamePanel;
    private boolean up = true, down = true, left = true, right = true;
    private int speed = 1;
    private Window window;
    private Settings settings;
    private JButton button;
    private JLabel label;
    private JSONEditor jEditor;

    public KeyboardInput(GamePanel gamePanel, Player dad, Window window) {
        this.gamePanel = gamePanel;
        this.dad = dad;
        this.window = window;
        try {
            jEditor = new JSONEditor("src/com/vojat/Data/Controls.json");
            jEditor.readFile();
            jEditor.readData(jEditor.JSONObjects.get(1), "up");
            jEditor.readData(jEditor.JSONObjects.get(1), "down");
        } catch (FileNotFoundException e) {
            System.err.println(ErrorList.ERR_404.message);
        }
    }

    public KeyboardInput(Settings settings, JButton button, JLabel label) {
        this.settings = settings;
        this.button = button;
        this.label = label;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (dad != null) {
            if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "up"))) {
                if (up) {
                    dad.setTexture("res/Pics/Dad_Texture_B.png");
                    dad.VECTORY = -speed;
                    up = false;
                }
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "down"))) {
                if (down) {
                    dad.setTexture("res/Pics/Dad_Texture_F.png");
                    dad.VECTORY = speed;
                    down = false;
                }
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "left"))) {
                if (left) {
                    dad.setTexture("res/Pics/Dad_Texture_L.png");
                    dad.VECTORX = -speed;
                    left = false;
                }
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "right"))) {
                if (right) {
                    dad.setTexture("res/Pics/Dad_Texture_R.png");
                    dad.VECTORX = speed;
                    right = false;
                }
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(0), "exit"))) {
                Game.killGame();
                window.setElements(new MenuPanel(1920, 1080, window));
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(0), "pause"))) {
                Game.pauseGame();
                if (Game.pause) {
                    Game.clip.stop();
                } else {
                    Game.clip.start();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (dad != null) {
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
                if (dad.selectedItem+1 < dad.inventory.length) {
                    dad.selectedItem++;
                } else {
                    dad.selectedItem = 0;
                }
                gamePanel.inventoryPanel.repaintItem(dad);
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "previous"))) {
                if (dad.selectedItem > 0) {
                    dad.selectedItem--;
                } else {
                    dad.selectedItem = (byte) (dad.inventory.length - 1);
                }
                gamePanel.inventoryPanel.repaintItem(dad);
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(2), "open"))) {
                gamePanel.changeVisibility(gamePanel.fullInv, gamePanel.inventoryVisible);
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
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (settings != null) {
            settings.pressed(e.getKeyChar(), button, this, label);
        }
    }

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
