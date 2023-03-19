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
public class KeyboardInput implements KeyListener{

    private Player dad;
    private GamePanel gamePanel;
    private boolean up = true, down = true, left = true, right = true;
    private int speed = 4;
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
                    up = false;
                }
                dad.moveUP(-speed);
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "down"))) {
                if (down) {
                    dad.setTexture("res/Pics/Dad_Texture_F.png");
                    down = false;
                }
                dad.moveUP(speed);
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "left"))) {
                if (left) {
                    dad.setTexture("res/Pics/Dad_Texture_L.png");
                    left = false;
                }
                dad.moveSIDE(-speed);
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "right"))) {
                if (right) {
                    dad.setTexture("res/Pics/Dad_Texture_R.png");
                    right = false;
                }
                dad.moveSIDE(speed);
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(0), "exit"))) {
                System.out.println("Game saved");
                window.setElements(new MenuPanel(1920, 1080, window));
                Game.stopGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (dad != null) {
            if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "up"))) {
                up = true;
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "down"))) {
                down = true;
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "left"))) {
                left = true;
            } else if (KeyEvent.getKeyText(e.getKeyCode()).equals(jEditor.readData(jEditor.JSONObjects.get(1), "right"))) {
                right = true;
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
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (settings != null) {
            settings.pressed(e.getKeyChar(), button, this, label);
        }
    }
}
