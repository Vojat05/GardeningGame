package com.vojat.inputs;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import com.vojat.garden.GamePanel;
import com.vojat.garden.Player;


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

    public KeyboardInput(GamePanel gamePanel, Player dad) {
        this.gamePanel = gamePanel;
        this.dad = dad;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (up) {
                    dad.setTexture("res/Dad_Texture_B.png");
                    up = false;
                }
                dad.moveUP(-speed);
                break;
            
            case KeyEvent.VK_DOWN:
                if (down) {
                    dad.setTexture("res/Dad_Texture_F.png");
                    down = false;
                }
                dad.moveUP(speed);
                break;
            
            case KeyEvent.VK_LEFT:
                if (left) {
                    dad.setTexture("res/Dad_Texture_L.png");
                    left = false;
                }
                dad.moveSIDE(-speed);
                break;

            case KeyEvent.VK_RIGHT:
                if (right) {
                    dad.setTexture("res/Dad_Texture_R.png");
                    right = false;
                }
                dad.moveSIDE(speed);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            
            case KeyEvent.VK_LEFT:
                left = true;
                break;

            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            
            case KeyEvent.VK_E:
                if (dad.selectedItem+1 < dad.inventory.length) {
                    dad.selectedItem++;
                } else {
                    dad.selectedItem = 0;
                }
                gamePanel.inventoryPanel.repaintItem(gamePanel, dad);
                break;

            case KeyEvent.VK_Q:
                if (dad.selectedItem > 0) {
                    dad.selectedItem--;
                } else {
                    dad.selectedItem = (byte) (dad.inventory.length - 1);
                }
                gamePanel.inventoryPanel.repaintItem(gamePanel, dad);
                break;

            case KeyEvent.VK_T:
                System.out.println("Selected item: " + dad.inventory[dad.selectedItem]);
                gamePanel.changeVisibility(gamePanel.fullInv, gamePanel.inventoryVisible);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {;}
}
