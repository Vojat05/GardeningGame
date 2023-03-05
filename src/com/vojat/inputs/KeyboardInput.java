package com.vojat.inputs;

import java.awt.event.KeyListener;

import javax.swing.JButton;

import java.awt.event.KeyEvent;

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
    private int speed = 6;
    private Window window;
    private Settings settings;
    private JButton button;

    public KeyboardInput(GamePanel gamePanel, Player dad, Window window) {
        this.gamePanel = gamePanel;
        this.dad = dad;
        this.window = window;
    }

    public KeyboardInput(Settings settings, JButton button) {
        this.settings = settings;
        this.button = button;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (dad != null) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    if (up) {
                        dad.setTexture("res/Pics/Dad_Texture_B.png");
                        up = false;
                    }
                    dad.moveUP(-speed);
                    break;
                
                case KeyEvent.VK_S:
                    if (down) {
                        dad.setTexture("res/Pics/Dad_Texture_F.png");
                        down = false;
                    }
                    dad.moveUP(speed);
                    break;
                
                case KeyEvent.VK_A:
                    if (left) {
                        dad.setTexture("res/Pics/Dad_Texture_L.png");
                        left = false;
                    }
                    dad.moveSIDE(-speed);
                    break;
    
                case KeyEvent.VK_D:
                    if (right) {
                        dad.setTexture("res/Pics/Dad_Texture_R.png");
                        right = false;
                    }
                    dad.moveSIDE(speed);
                    break;
    
                case KeyEvent.VK_ESCAPE:
                    Game.saveGame();
                    System.out.println("Game saved");
                    window.setElements(new MenuPanel(1920, 1080, window));
                    Game.stopGame();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (dad != null) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    up = true;
                    break;
                
                case KeyEvent.VK_S:
                    down = true;
                    break;
                
                case KeyEvent.VK_A:
                    left = true;
                    break;
    
                case KeyEvent.VK_D:
                    right = true;
                    break;
                
                case KeyEvent.VK_E:
                    if (dad.selectedItem+1 < dad.inventory.length) {
                        dad.selectedItem++;
                    } else {
                        dad.selectedItem = 0;
                    }
                    gamePanel.inventoryPanel.repaintItem(dad);
                    break;
    
                case KeyEvent.VK_Q:
                    if (dad.selectedItem > 0) {
                        dad.selectedItem--;
                    } else {
                        dad.selectedItem = (byte) (dad.inventory.length - 1);
                    }
                    gamePanel.inventoryPanel.repaintItem(dad);
                    break;
    
                case KeyEvent.VK_T:
                    gamePanel.changeVisibility(gamePanel.fullInv, gamePanel.inventoryVisible);
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (settings != null) {
            settings.pressed(e.getKeyChar(), button, this);
        }
    }
}
